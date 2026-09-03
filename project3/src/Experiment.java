/*
 * Defines the Experiment data type: an immutable record of an experiment's
 * name, weight, and rating, with accessors used by the selection strategies.
 */

/**
 * Represents a single scientific experiment that can be selected for a mission.
 * <p>
 * Each experiment has a fixed weight (in kilograms) and a rating that reflects
 * its scientific value. Instances are immutable.
 */
public class Experiment {
    private final String name;
    private final int weight;
    private final int rating;

    /**
     * Creates an experiment.
     *
     * @param name   the experiment's name
     * @param weight the experiment's weight in kilograms
     * @param rating the experiment's scientific value rating
     */
    public Experiment(String name, int weight, int rating) {
        this.name = name;
        this.weight = weight;
        this.rating = rating;
    }

    /**
     * Returns the experiment's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the experiment's weight.
     *
     * @return the weight in kilograms
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Returns the experiment's rating.
     *
     * @return the scientific value rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Returns a string representation in the form {@code [name, weight, rating]}.
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "[" + name + ", " + weight + ", " + rating + "]";
    }
}
