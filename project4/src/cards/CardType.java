/*
 * Defines the CardType enum: the movement effect a drawn card produces. Only
 * effects that change the player's position are modeled; every money-only card
 * collapses into a single no-op type.
 */

package cards;

/**
 * The movement effect of a Chance or Community Chest card. Only effects that
 * change the player's position matter to the landing-frequency simulation;
 * every money-only card is modeled as {@link #NO_OP}.
 */
public enum CardType {
    /** Move to a specific named square (GO, Illinois, St. Charles, Boardwalk). */
    ADVANCE_TO,
    /** Advance to the nearest railroad square. */
    ADVANCE_TO_NEAREST_RAILROAD,
    /** Advance to the nearest utility square. */
    ADVANCE_TO_NEAREST_UTILITY,
    /** Move back three squares. */
    GO_BACK_3,
    /** Go directly to jail. */
    GO_TO_JAIL,
    /** Keep a Get Out of Jail Free card for later use. */
    GET_OUT_OF_JAIL_FREE,
    /** No movement effect; every money-only card collapses into this one type. */
    NO_OP
}
