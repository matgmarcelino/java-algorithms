/*
 * Implements a shuffled deck of Chance or Community Chest cards, including the
 * contents of both decks, drawing from the draw pile onto the discard pile,
 * reshuffling the discards when the draw pile runs out, and returning a held
 * Get Out of Jail Free card to the deck.
 */

package cards;

import java.util.*;

/**
 * A shuffled deck of Chance or Community Chest cards.
 *
 * <p>Cards are drawn from the top of the draw pile and placed on a discard pile
 * (except cards held by the player, such as Get Out of Jail Free). When the draw
 * pile is exhausted, the discard pile is reshuffled to form a new draw pile,
 * mirroring how the physical game recycles the deck. All shuffling uses the
 * caller-supplied {@link Random} so runs are reproducible from a seed.
 */
public class Deck {
    private final Deque<Card> drawPile;
    private final List<Card> discardPile;
    private final Random random;

    /**
     * Builds and shuffles a deck of the given type.
     *
     * @param deckType which deck to build ({@link DeckType#CHANCE} or
     *                 {@link DeckType#COMMUNITY_CHEST})
     * @param random   the source of randomness used to shuffle, shared with the
     *                 simulation so results are reproducible
     */
    public Deck(DeckType deckType, Random random) {
        this.random = random;
        List<Card> cards;
        drawPile = new ArrayDeque<>();
        discardPile = new ArrayList<>();

        if (deckType == DeckType.CHANCE) cards = buildChanceCards();
        else cards = buildCommunityChestCards();

        Collections.shuffle(cards, random);

        for (Card c : cards) {
            drawPile.offer(c);
        }
    }

    /**
     * Draws the top card. If the draw pile is empty, the discard pile is
     * reshuffled to become the new draw pile before drawing.
     *
     * @return the drawn card
     */
    public Card draw() {
        if (drawPile.isEmpty()) {
            Collections.shuffle(discardPile, random);
            drawPile.addAll(discardPile);
            discardPile.clear();
        }
        return drawPile.poll();
    }

    /**
     * Places a card on the discard pile.
     *
     * @param card the card to discard
     */
    public void discard(Card card) {
        discardPile.add(card);
    }

    /**
     * Returns the size of the draw pile.
     *
     * @return the number of cards remaining in the draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }

    /**
     * Returns the size of the discard pile.
     *
     * @return the number of cards currently in the discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }

    /**
     * Builds the 16-card Chance deck, unshuffled.
     *
     * @return the Chance cards in fixed order
     */
    private List<Card> buildChanceCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card(CardType.ADVANCE_TO, 0));                 // Advance to GO
        cards.add(new Card(CardType.ADVANCE_TO, 24));                // Advance to Illinois Avenue
        cards.add(new Card(CardType.ADVANCE_TO, 11));                // Advance to St. Charles Place
        cards.add(new Card(CardType.ADVANCE_TO_NEAREST_UTILITY));    // Advance to nearest Utility
        cards.add(new Card(CardType.ADVANCE_TO_NEAREST_RAILROAD));   // Advance to nearest Railroad
        cards.add(new Card(CardType.ADVANCE_TO_NEAREST_RAILROAD));   // Advance to nearest Railroad (again)
        cards.add(new Card(CardType.NO_OP));                         // Bank pays you dividend of $50
        cards.add(new Card(CardType.GET_OUT_OF_JAIL_FREE));          // Get Out of Jail Free
        cards.add(new Card(CardType.GO_BACK_3));                     // Go Back 3 Spaces
        cards.add(new Card(CardType.GO_TO_JAIL));                    // Go to Jail
        cards.add(new Card(CardType.NO_OP));                         // Make general repairs
        cards.add(new Card(CardType.NO_OP));                         // Speeding fine, pay $15
        cards.add(new Card(CardType.ADVANCE_TO, 5));                 // Take a trip to Reading Railroad
        cards.add(new Card(CardType.NO_OP));                         // Elected Chairman of the board.Board
        cards.add(new Card(CardType.NO_OP));                         // Your building loan matures
        cards.add(new Card(CardType.NO_OP));                         // You have won a crossword competition

        return cards;
    }

    /**
     * Builds the 16-card Community Chest deck, unshuffled. Only the movement
     * cards are modeled individually; the rest are {@link CardType#NO_OP}.
     *
     * @return the Community Chest cards in fixed order
     */
    private List<Card> buildCommunityChestCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card(CardType.ADVANCE_TO, 0));         // Advance to GO
        cards.add(new Card(CardType.GET_OUT_OF_JAIL_FREE));  // Get Out of Jail Free
        cards.add(new Card(CardType.GO_TO_JAIL));             // Go to Jail
        for (int i = 0; i < 13; i++) {
            cards.add(new Card(CardType.NO_OP));              // remaining money-only cards
        }

        return cards;
    }

    /**
     * Smoke test: draws and discards 100 cards from a Community Chest deck,
     * printing the draw and discard pile sizes so the reshuffle behavior can be
     * observed.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Deck deck = new Deck(DeckType.COMMUNITY_CHEST, new Random(42));
        for (int i = 1; i < 101; i++) {
            Card card = deck.draw();
            deck.discard(card);

            System.out.printf("Round: %-3d | cards.Card drawn: %30s | Draw Pile Size: %2d | Discard Pile Size: %2d%n",
                        i, card.toString(), deck.getDrawPileSize(), deck.getDiscardPileSize());
        }
    }
}
