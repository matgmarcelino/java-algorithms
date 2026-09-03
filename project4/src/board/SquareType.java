/*
 * Defines the SquareType enum: the behavioral category of a board square
 * (property, Chance, Community Chest, Go to Jail, railroad, utility, etc.),
 * which determines what happens when the player lands on it.
 */

package board;

/**
 * The behavioral category of a board square. Determines what happens when a
 * player lands on it (drawing a card, being sent to jail, etc.); squares with
 * no special effect are simply {@link #PROPERTY}, {@link #GO},
 * {@link #FREE_PARKING} and the like.
 */
public enum SquareType {
    /** An ordinary property; landing has no movement effect. */
    PROPERTY,
    /** Draw a Chance card. */
    CHANCE,
    /** Draw a Community Chest card. */
    COMMUNITY_CHEST,
    /** Sends the player to jail. */
    GO_TO_JAIL,
    /** The GO square (index 0). */
    GO,
    /** A tax square; no movement effect. */
    TAX,
    /** A railroad; targeted by "advance to nearest railroad" cards. */
    RAILROAD,
    /** The jail / just-visiting square (index 10). */
    JAIL,
    /** A utility; targeted by "advance to nearest utility" cards. */
    UTILITY,
    /** Free Parking; no movement effect. */
    FREE_PARKING
}
