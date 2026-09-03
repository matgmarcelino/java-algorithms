/*
 * Implements the core single-player Monte Carlo simulation: rolling two dice,
 * moving around the board, resolving Chance, Community Chest and Go to Jail
 * squares, applying the configured jail-exit strategy, and tallying how often
 * each of the 40 squares is landed on. Runs are reproducible from a seed.
 */

package sim;

import board.Board;
import board.SquareType;
import cards.Card;
import cards.CardType;
import cards.Deck;
import cards.DeckType;

import java.util.Random;

/**
 * A single-player Monopoly movement simulation used to measure how often each
 * board square is landed on.
 *
 * <p>The player takes turns rolling two dice and moving, resolving Chance,
 * Community Chest and Go to Jail squares, and following the configured
 * {@link Strategy} for leaving jail. After each turn the square the player ends
 * on is tallied in {@link #getLandingCounts()}. All randomness derives from the
 * seed passed to the constructor, so a run is fully reproducible.
 */
public class Simulation {
    private final Deck chanceDeck;
    private final Deck communityChestDeck;
    private final Player player;
    private final long seed;
    private final Random random;
    private final Strategy strategy;
    private final long[] landingCounts = new long[40];

    /**
     * Creates a simulation with fresh, shuffled decks and a player at GO.
     *
     * @param strategy the jail-exit strategy to follow
     * @param seed     the RNG seed; determines dice rolls and deck shuffles
     */
    public Simulation(Strategy strategy, long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.strategy = strategy;

        chanceDeck = new Deck(DeckType.CHANCE, random);
        communityChestDeck = new Deck(DeckType.COMMUNITY_CHEST, random);
        player = new Player();
    }

    /**
     * @return a single die roll, 1&ndash;6
     */
    private int rollDie() {
        return random.nextInt(6) + 1;
    }

    /**
     * Moves the player the given number of spaces, wrapping around the board.
     * Negative values move backward.
     *
     * @param spaces the number of spaces to move
     */
    private void moveForward(int spaces) {
        player.position = Math.floorMod(player.position + spaces, 40);
    }

    /**
     * Sends the player to jail (position 10) and resets the jail counter.
     */
    private void sendToJail() {
        player.position = 10;
        player.inJail = true;
        player.jailTurnsServed = 0;
    }

    /**
     * Clears the player's jail status and resets the jail counter.
     */
    private void leaveJail() {
        player.inJail = false;
        player.jailTurnsServed = 0;
    }

    /**
     * Applies the effect of the square the player is standing on: draws a card,
     * sends the player to jail, or does nothing for ordinary squares.
     *
     * @param position the board index the player has landed on
     */
    private void resolveSquare(int position) {
        switch (Board.getType(position)) {
            case SquareType.CHANCE -> resolveCard(chanceDeck.draw(), chanceDeck);
            case SquareType.COMMUNITY_CHEST -> resolveCard(communityChestDeck.draw(), communityChestDeck);
            case SquareType.GO_TO_JAIL -> sendToJail();
            default -> {}
        }
    }

    /**
     * Finds the nearest square of the given type ahead of the player, searching
     * clockwise from the square immediately in front and wrapping around.
     *
     * @param type the square type to search for
     * @return the board index of the nearest matching square
     * @throws IllegalStateException if no square of that type exists
     */
    private int findNearest(SquareType type) {
        int currentPosition = Math.floorMod(player.position + 1, 40);
        for (int i = 0; i < 40; i++) {
            if (Board.getType(currentPosition) == type) {
                return currentPosition;
            }

            currentPosition = Math.floorMod(currentPosition + 1, 40);
        }
        throw new IllegalStateException("No square found");
    }

    /**
     * Applies a drawn card's effect: moving the player and resolving the
     * resulting square, sending them to jail, or retaining a Get Out of Jail
     * Free card. Cards other than the retained one are returned to their deck's
     * discard pile.
     *
     * @param card   the card that was drawn
     * @param source the deck the card was drawn from
     */
    private void resolveCard(Card card, Deck source) {
        switch (card.cardType()) {
            case NO_OP -> source.discard(card);
            case GET_OUT_OF_JAIL_FREE -> player.heldGetOutOfJailFreeCards.add(source);
            case ADVANCE_TO_NEAREST_RAILROAD -> {
                player.position = findNearest(SquareType.RAILROAD);
                source.discard(card);
                resolveSquare(player.position);
            }
            case ADVANCE_TO_NEAREST_UTILITY -> {
                player.position = findNearest(SquareType.UTILITY);
                source.discard(card);
                resolveSquare(player.position);
            }
            case ADVANCE_TO -> {
                player.position = card.targetIndex();
                source.discard(card);
                resolveSquare(player.position);
            }
            case GO_BACK_3 -> {
                moveForward(-3);
                source.discard(card);
                resolveSquare(player.position);
            }
            case GO_TO_JAIL -> {
                sendToJail();
                source.discard(card);
            }
        }
    }

    /**
     * Plays out one turn while the player is in jail, following the configured
     * strategy. A held Get Out of Jail Free card is used first; otherwise
     * {@link Strategy#IMMEDIATE_EXIT} leaves at once, while
     * {@link Strategy#TRY_DOUBLES} rolls for doubles and is forced out after
     * three failed attempts. On release the player moves and resolves the
     * resulting square.
     */
    private void handleJailTurn() {
        if (!player.heldGetOutOfJailFreeCards.isEmpty()) {
            Deck deck = player.heldGetOutOfJailFreeCards.removeFirst();
            deck.discard(new Card(CardType.GET_OUT_OF_JAIL_FREE));
            leaveJail();
            exitJailAndMove();
            return;
        }

        if (strategy == Strategy.IMMEDIATE_EXIT) {
            leaveJail();
            exitJailAndMove();
            return;
        }

        if (player.jailTurnsServed >= 3) {
            leaveJail();
            exitJailAndMove();
            return;
        }

        int firstDice = rollDie();
        int secondDice = rollDie();
        if (firstDice == secondDice) {
            leaveJail();
            moveForward(firstDice + secondDice);
            resolveSquare(player.position);
        } else {
            player.jailTurnsServed++;
        }
    }

    /**
     * Rolls two dice, moves the just-released player, and resolves the square
     * they land on. Used when leaving jail without rolling doubles.
     */
    private void exitJailAndMove() {
        moveForward(rollDie() + rollDie());
        resolveSquare(player.position);
    }

    /**
     * Plays out one complete turn and tallies the square the player ends on.
     *
     * <p>If in jail, the turn is delegated to {@link #handleJailTurn()}.
     * Otherwise the player rolls and moves, taking another roll after each
     * doubles until a non-double is rolled, jail is reached, or a third
     * consecutive double sends them to jail.
     */
    private void takeTurn() {
        player.consecutiveDoubles = 0;

        if (player.inJail) {
            handleJailTurn();
            landingCounts[player.position]++;
            return;
        }

        while (true) {
            int firstDice = rollDie();
            int secondDice = rollDie();
            if (firstDice == secondDice) {
                player.consecutiveDoubles++;
                if (player.consecutiveDoubles == 3) {
                    sendToJail();
                    break;
                }
            }

            moveForward(firstDice + secondDice);
            resolveSquare(player.position);

            if (player.inJail) break;
            if (firstDice != secondDice) break;
        }

        landingCounts[player.position]++;
    }

    /**
     * Plays the given number of turns, accumulating landing counts.
     *
     * @param n the number of turns to play
     */
    public void runTurns(long n) {
        for (long i = 0; i < n; i++) takeTurn();
    }

    /**
     * Returns a snapshot of the landing counts by square. Modifying the returned
     * array does not affect the simulation.
     *
     * @return a copy of the per-square landing counts, length 40
     */
    public long[] getLandingCounts() {
        return landingCounts.clone();
    }

    /**
     * Returns the RNG seed.
     *
     * @return the seed this simulation was constructed with
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Returns the configured strategy.
     *
     * @return the jail-exit strategy this simulation follows
     */
    public Strategy getStrategy() {
        return strategy;
    }
}
