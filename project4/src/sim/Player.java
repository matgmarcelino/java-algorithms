/*
 * Holds the mutable state of the single simulated player: board position, jail
 * status, doubles rolled in a row, and any Get Out of Jail Free cards being
 * held.
 */

package sim;

import cards.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable state of the single simulated player: board position, jail status and
 * any held Get Out of Jail Free cards. Fields are package-private and mutated
 * directly by {@link Simulation}.
 */
public class Player {
    /** Current board index, 0-39. */
    int position;

    /** Whether the player is currently in jail. */
    boolean inJail;

    /** Number of FAILED doubles attempts so far ({@link Strategy#TRY_DOUBLES} only); max 3. */
    int jailTurnsServed;

    /** Doubles rolled in a row this turn; three in a row sends the player to jail. */
    int consecutiveDoubles;

    /**
     * Get Out of Jail Free cards the player is holding. Each entry is the deck
     * the card must be returned to when used.
     */
    List<Deck> heldGetOutOfJailFreeCards;

    /**
     * Creates a player at GO (position 0) holding no cards.
     */
    public Player() {
        this.heldGetOutOfJailFreeCards = new ArrayList<>();
    }
}
