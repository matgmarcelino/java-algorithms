/*
 * Defines the fixed 40-square Monopoly board: the name and behavioral type of
 * every square, plus the lookups the simulation uses to find a square's type
 * and the nearest railroad or utility ahead of a given position.
 */

package board;

/**
 * The fixed 40-square Monopoly board.
 *
 * <p>The layout is defined statically and shared by all simulations: squares
 * are addressed by their index (0 = GO, increasing clockwise to 39 = Boardwalk).
 * This class exposes read-only lookups of each square's name and type.
 */
public class Board {

    /** Not instantiable; the board is accessed through static lookups only. */
    private Board() {}

    /**
     * A single board square: its display name and behavioral type.
     */
        private record Square(String name, SquareType type) {
        /**
         * Creates a square.
         *
         * @param name the display name (e.g. {@code "Boardwalk"})
         * @param type the behavioral type of the square
         */
        private Square {
        }

            /**
             * @return the square's display name
             */
            @Override
            public String name() {
                return name;
            }

            /**
             * @return the square's behavioral type
             */
            @Override
            public SquareType type() {
                return type;
            }
        }

    private static final Square[] squares = {
            new Square("GO", SquareType.GO),                              // 0
            new Square("Mediterranean Avenue", SquareType.PROPERTY),      // 1
            new Square("Community Chest", SquareType.COMMUNITY_CHEST),    // 2
            new Square("Baltic Avenue", SquareType.PROPERTY),              // 3
            new Square("Income Tax", SquareType.TAX),                      // 4
            new Square("Reading Railroad", SquareType.RAILROAD),           // 5
            new Square("Oriental Avenue", SquareType.PROPERTY),            // 6
            new Square("Chance", SquareType.CHANCE),                       // 7
            new Square("Vermont Avenue", SquareType.PROPERTY),             // 8
            new Square("Connecticut Avenue", SquareType.PROPERTY),         // 9
            new Square("Jail / Just Visiting", SquareType.JAIL),           // 10
            new Square("St. Charles Place", SquareType.PROPERTY),          // 11
            new Square("Electric Company", SquareType.UTILITY),            // 12
            new Square("States Avenue", SquareType.PROPERTY),              // 13
            new Square("Virginia Avenue", SquareType.PROPERTY),            // 14
            new Square("Pennsylvania Railroad", SquareType.RAILROAD),      // 15
            new Square("St. James Place", SquareType.PROPERTY),            // 16
            new Square("Community Chest", SquareType.COMMUNITY_CHEST),    // 17
            new Square("Tennessee Avenue", SquareType.PROPERTY),           // 18
            new Square("New York Avenue", SquareType.PROPERTY),            // 19
            new Square("Free Parking", SquareType.FREE_PARKING),           // 20
            new Square("Kentucky Avenue", SquareType.PROPERTY),            // 21
            new Square("Chance", SquareType.CHANCE),                       // 22
            new Square("Indiana Avenue", SquareType.PROPERTY),             // 23
            new Square("Illinois Avenue", SquareType.PROPERTY),            // 24
            new Square("B&O Railroad", SquareType.RAILROAD),               // 25
            new Square("Atlantic Avenue", SquareType.PROPERTY),            // 26
            new Square("Ventnor Avenue", SquareType.PROPERTY),             // 27
            new Square("Water Works", SquareType.UTILITY),                 // 28
            new Square("Marvin Gardens", SquareType.PROPERTY),             // 29
            new Square("Go to Jail", SquareType.GO_TO_JAIL),                // 30
            new Square("Pacific Avenue", SquareType.PROPERTY),             // 31
            new Square("North Carolina Avenue", SquareType.PROPERTY),      // 32
            new Square("Community Chest", SquareType.COMMUNITY_CHEST),    // 33
            new Square("Pennsylvania Avenue", SquareType.PROPERTY),        // 34
            new Square("Short Line", SquareType.RAILROAD),                 // 35
            new Square("Chance", SquareType.CHANCE),                       // 36
            new Square("Park Place", SquareType.PROPERTY),                 // 37
            new Square("Luxury Tax", SquareType.TAX),                      // 38
            new Square("Boardwalk", SquareType.PROPERTY)                   // 39
    };

    /**
     * Returns the behavioral type of the square at the given board position.
     *
     * @param position the board index, 0-39
     * @return the type of that square
     */
    public static SquareType getType(int position) {
        return squares[position].type();
    }

    /**
     * Returns the display name of the square at the given board position.
     *
     * @param position the board index, 0-39
     * @return the name of that square
     */
    public static String getName(int position) {
        return squares[position].name();
    }
}
