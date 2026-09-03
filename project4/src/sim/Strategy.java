/*
 * Defines the Strategy enum: the two jail-exit strategies compared by the
 * experiment, leaving jail immediately versus rolling for doubles for up to
 * three turns first.
 */

package sim;

/**
 * The jail-exit strategy a simulation uses. This is the single variable the
 * experiment compares: whether the player pays to leave jail immediately or
 * rolls for doubles first.
 */
public enum Strategy {
    /** Leave jail on the first turn (labeled "A" in reports). */
    IMMEDIATE_EXIT,
    /** Roll for doubles for up to three turns before leaving jail (labeled "B"). */
    TRY_DOUBLES
}
