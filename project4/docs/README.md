# Monte Carlo Board Simulation

**Author:** Matheus Marcelino

This project is a Monte Carlo simulation of a single player moving around a standard 40-square Monopoly board. The aim is to evaluate how often each square is landed on and whether the player's _jail-exit strategy_ influences the distribution of landings. Dice movement, the doubles rule, the Go to Jail space, and the Chance and Community Chest cards are all modeled. Money, property, and rent are left out because they do not affect where the player lands.

---

## Language and Version

- **Language:** Java
- **Version:** Java 21 (uses records, switch expressions, and `SequencedCollection` methods)

---

## Project Structure

```
project4/
├── src/
│   ├── board/
│   │   ├── Board.java        | The fixed 40-square board layout
│   │   └── SquareType.java   | Behavioral category of each square
│   ├── cards/
│   │   ├── Card.java         | A single Chance / Community Chest card
│   │   ├── CardType.java     | The movement effect a card produces
│   │   ├── Deck.java         | A shuffled deck with draw and discard piles
│   │   └── DeckType.java     | Which of the two decks is being built
│   └── sim/
│       ├── Player.java       | Position, jail status, and held cards
│       ├── Strategy.java     | The two jail-exit strategies compared
│       ├── Simulation.java   | One reproducible run of the simulation
│       └── BatchRunner.java  | Driver: 20 runs, 80 datasets, and the summary
├── data/                     | Generated CSV snapshots and summary.txt
└── docs/
    └── README.md
```

---

## Files

### `board` package

- **`Board.java`** — The game board, a static array of 40 `Square` entries, numbered from 0 (GO) to 39 (Boardwalk) in clockwise order. It is not instantiable. All simulations use a single configuration, accessed via the static `getName(int)` and `getType(int)` methods.
- **`SquareType.java`** — An enum of behavioral classifications: `CHANCE`, `COMMUNITY_CHEST`, and `GO_TO_JAIL` trigger effects; `RAILROAD` and `UTILITY` are destinations for the "advance to nearest" cards; `PROPERTY`, `GO`, `TAX`, `JAIL`, and `FREE_PARKING` do not affect movement.

### `cards` package

- **`Card.java`** — An immutable entity that contains a `CardType` and a `targetIndex`. The convenience constructor sets the target value to -1 for each card whose effect does not involve moving to a specified square.
- **`CardType.java`** — The movement effect of a card. All monetary-only cards are grouped into `NO_OP`. Only effects with positional impact are modeled separately.
- **`Deck.java`** — A collection with an `ArrayDeque` draw pile and an `ArrayList` discard pile. Drawing from an empty draw pile shuffles the discarded cards into a new draw pile, similar to how the physical game shuffles the deck. Shuffling uses the `Random` supplied by the simulation, so the order of the deck is reproducible from the seed. Its `main` function shows a simple test: 100 cards are drawn and the sizes of the two piles are printed to show the reshuffle.
- **`DeckType.java`** — Determines which of the two decks `Deck` will construct.

### `sim` package

- **`Player.java`** — The variable attributes of the individual player: location on the board, jail status, failed attempts at doubles, number of doubles in a row for the current turn, and a list of decks to which any held Get Out of Jail Free cards must be returned.
- **`Strategy.java`** — The only variable that the experiment changes: `IMMEDIATE_EXIT` (**A**) or `TRY_DOUBLES` (**B**).
- **`Simulation.java`** — One full run. Two decks, the player, a `long[40]` array of landing counts, and takes turns until told to stop. All randomness is derived from the seed passed to the constructor, so any run can be exactly reproduced.
- **`BatchRunner.java`** — The main driver and entry point. Performs the entire experiment and records every data set with a comparative analysis.

---

## Simulation Rules

On each turn, the player rolls two dice, moves forward the appropriate number of squares clockwise, and resolves the square landed on. The square where the player finishes the turn is the one that counts.

- **Doubles** — Rolling doubles gets another roll. Rolling three doubles in a row sends the player directly to jail, regardless of what the rolls in between were.
- **Go to Jail (square 30)** — Sends the player to square 10 and switches the jail variable. Since the action occurs before the count is made, square 30 should never gain a single landing; this is explicitly checked at the end of the run.
- **Cards** — Landing on a Chance or Community Chest space means drawing from that deck. The `ADVANCE_TO`, `GO_BACK_3`, and the two "advance to nearest" cards move the player forward and then resolve the _new_ square, allowing one card to trigger another. Instead of being discarded, a Get Out of Jail Free card is kept by the player and returned to its original deck when used.
- **Nearest railroad / utility** — Searches starting from the square directly in front of the player, proceeding clockwise around the board.

### The two strategies

- **Strategy A — Immediate exit.** The player takes the penalty and leaves jail on the first turn, then rolls and moves as normal.
- **Strategy B — Try for doubles.** The player rolls for doubles up to three times. They are released and move normally after rolling doubles or after three failed attempts. Time spent in jail counts as turns, with each turn counting as a landing on square 10.

Both strategies use a remaining Get Out of Jail Free card first, before considering the jail-exit strategy.

---

## Experiment Design

`BatchRunner` runs each strategy for **10 independent runs**, with different random seeds (1001–1010 for A, 2001–2010 for B). Landing counts are taken at **1,000 / 10,000 / 100,000 / 1,000,000** turns. Each run continues from the last checkpoint rather than restarting, so the checkpoints are nested samples of a single run.

That produces **80 CSV files** in `data/`, named `strategy<A|B>_run_<NN>_n<turns>.csv`. Each file has a comment line with the strategy, run number, number of turns, and seed, followed by one line per square:

```
# strategy=A, run=1, n=1000000, seed=1001
index,name,count,percentage
0,GO,31347,3.1347
1,Mediterranean Avenue,20873,2.0873
...
```

The ten 1M-turn snapshots for each strategy are kept in memory for the final summary comparison between the two strategies.

### The noise floor

When comparing averages, the difference must be larger than the spread for any given square within either strategy's ten runs; otherwise the difference is within expected variance and is not reported. For each square, `spreads` stores the max minus min landing percentage across that strategy's ten runs, representing the noise floor. Any A vs. B difference smaller than this is not reported — only differences larger than it are summarized.

---

## Results

The full report is written to `data/summary.txt`. The most common squares after 1,000,000 turns:

**Jail dominates the distribution in both strategies.** Square 10 (Jail) is the most frequent overall, with 6.86% of turns in strategy A and 13.50% (roughly double) in strategy B, since turns spent in jail itself count toward that square's frequency.

| Rank | Strategy A (Immediate Exit) | Mean % | Strategy B (Try for Doubles) | Mean %  |
| ---- | --------------------------- | ------ | ----------------------------- | ------- |
| 1    | Jail / Just Visiting        | 6.8642 | Jail / Just Visiting          | 13.5041 |
| 2    | Illinois Avenue             | 3.1909 | GO                             | 2.9275  |
| 3    | GO                          | 3.1482 | Illinois Avenue                | 2.9221  |
| 4    | New York Avenue             | 3.1479 | Tennessee Avenue               | 2.8314  |
| 5    | B&O Railroad                | 3.0090 | B&O Railroad                   | 2.8213  |

Illinois Avenue, GO, and B&O Railroad are similarly favored in both strategies, which is unsurprising, as those are the destinations of the three Chance cards with direct travel instructions.

**The shift caused by strategy B versus A is even more significant outside of jail.** Ten squares differ by more than their standard error, the largest of which is square 10 itself, contributing a shift of 6.64 percentage points to its frequency. The other squares (Community Chest at 17, Pennsylvania Railroad, New York Avenue, Illinois Avenue, and others) favored by B are each between 0.20 and 0.38 percentage points lower under strategy A — turns spent in jail are turns not spent elsewhere, so the frequencies of the other squares decrease overall to compensate for the time no longer distributed across the rest of the board. The strategy itself does not favor any particular square outside of jail, aside from indirectly reducing the chance of landing on any given square by spending more turns in jail.

**Anomaly checks pass.** For each trial, the percentages across all squares sum to exactly 100%, and square 30 (Go to Jail) is reported with 0% frequency, since going to square 30 results in being sent directly to square 10 instead.
