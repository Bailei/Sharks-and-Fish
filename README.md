Sharks-and-Fish
===============

A simulation of an ocean containing sharks and fish

The ocean is rectangular, but the edges are connected together in a topological
donut or torus. This means that the top(North) and bottom (South) edges are 
considered adjacent. The ocean is divided into square cells, which are indexed
 as follows (for a 4x3 ocean):

            		         ------> x

                     |   -----------------------------
                     |   | 0, 0 | 1, 0 | 2, 0 | 3, 0 |
                   y |   -----------------------------
                     |   | 0, 1 | 1, 1 | 2, 1 | 3, 1 |
                     v   -----------------------------
                         | 0, 2 | 1, 2 | 2, 2 | 3, 2 |
                         -----------------------------

Note that the origin is in the upper left; the x-coordinate increases as you
move right, and the y-coordinate increases as you go down.  (This conforms to
Java's graphics commands, though you won't need to use them directly in this
project.)  You can also refer to locations such as (4, 0) or (-4, 3), which are
both the same as (0, 0) in a 4x3 ocean.  (More generally, the coordinates in
an ixj ocean are taken modulo i for the x-coordinate, which is horizontal, and
modulo j for the y-coordinate, which is vertical.)  Any pair of integers will
give you a valid position in the grid by "wrapping around" at the edges.

There are two kinds of entities in this ocean:  sharks and fish.  The sharks
and fish breed, eat, and die in the ocean.  Each cell of the grid can be
occupied by a single shark or fish, or it can be empty.

Part I:  Simulating Sharks and Fish
===================================

An ocean is described by its size and the initial placement of sharks and fish
in the ocean.  It is also described by a parameter called the "starveTime" for
a shark.  This is the number of simulation timesteps that a shark can live
through without eating.

The simulation proceeds in timesteps.  A "timestep" is a _transition_ from one
ocean to the next.  (Don't confuse timesteps with oceans; every timestep starts
with one ocean and ends with another.)  The rules for how the ocean looks at
the end of a timestep depend only on the occupants of the cells at the
beginning of the timestep.  Therefore, to obtain correct behavior, you will
often be working with two copies of the ocean simultaneously; one representing
the ocean at the beginning of the timestep, and the other representing the
ocean at the end of the timestep. 

The contents of any particular cell at the end of a timestep depend only on the
contents of that cell and its eight neighbors at the beginning of the timestep.
The "neighbors" are the eight adjacent cells:  the cells immediately to the
north, south, east, and west, as well as the four diagonal neighbors.  Here are
the rules.

1) If a cell contains a shark, and any of its neighbors is a fish, then the
shark eats during the timestep, and it remains in the cell at the end of the
timestep with its hunger completely gone.  (We may have multiple sharks sharing
the same fish.  This is fine; miraculously, they all get enough to eat.)

2) If a cell contains a shark, and none of its neighbors is a fish, it gets
hungrier during the timestep.  If this timestep is the (starveTime + 1)th
consecutive timestep the shark has gone through without eating, then the shark
dies (disappears).  Otherwise, it remains in the cell.  An example
demonstrating this rule appears below.

3) If a cell contains a fish, and all of its neighbors are either empty or are
other fish, then the fish stays where it is.

4) If a cell contains a fish, and one of its neighbors is a shark, then the
fish is eaten by a shark, and therefore disappears.

5) If a cell contains a fish, and two or more of its neighbors are sharks, then
a new shark is born in that cell.  Sharks are well-fed at birth; _after_ they
are born, they can survive an additional starveTime timesteps without eating.
(But they will die at the end of starveTime + 1 consecutive timesteps without
eating.)

6) If a cell is empty, and fewer than two of its neighbors are fish, then the
cell remains empty.

7) If a cell is empty, at least two of its neighbors are fish, and at most one
of its neighbors is a shark, then a new fish is born in that cell.

8) If a cell is empty, at least two of its neighbors are fish, and at least two
of its neighbors are sharks, then a new shark is born in that cell.  (The new
shark is well-fed at birth, even though it hasn't eaten a fish yet.)

starveTime Examples
-------------------
The following example demonstrates exactly when sharks die.  Suppose the
starveTime is 3.  Suppose the initial ocean, Ocean 0, contains a well-fed
shark (A).  If that shark never gets to eat, it will survive three timesteps
without food, making it to Ocean 3; but it will be dead by Ocean 4.  Another
shark is born in Ocean 1, and is fed during the transition from Ocean 2 to
Ocean 3, but is never fed again.  It survives to Ocean 6, but it is dead by
Ocean 7.

Ocean 0    shark A (well-fed)          [empty cell]
timestep                               shark B born
Ocean 1    shark A                     shark B (well-fed)
timestep
Ocean 2    shark A                     shark B
timestep                               shark B eats a fish (born last timestep)
Ocean 3    shark A                     shark B (well-fed)
timestep   shark A dies
Ocean 4    [empty cell]                shark B
timestep
Ocean 5                                shark B
timestep
Ocean 6                                shark B
timestep                               shark B dies
Ocean 7                                [empty cell]

There are two Java classes to implementation and animate your ocean:

    Simulation.java
    SimText.java

The Simulation and SimText classes (which consist primarily of a simulation
driver called main) generate random input to initialize the ocean, and animate
the sequence of oceans returned by the timeStep method of Ocean class.  So
that they can initialize Ocean and monitor the fish and sharks in the
Ocean during the simulation.

In the animation produced by Simulation, sharks are red squares and fish are
green squares.  In the animation produced by SimText, sharks are 'S' characters
and fish are '~' characters.  Simulation is more fun to watch, but SimText may
be easier to use for debugging and remote access.

Part II:  Converting a Run-Length Encoding to an Ocean
======================================================

For a large ocean, an Ocean object can consume quite a bit of memory or disk
space.  For long-term storage, we can store an Ocean more efficiently if we
represent it as a "run-length encoding."  Imagine taking all the rows of cells
in the ocean, and connecting them into one long strip.  Think of the cells as
being numbered thusly:

                        -----------------------------
                        |   0  |   1  |   2  |   3  |
                        -----------------------------
                        |   4  |   5  |   6  |   7  |
                        -----------------------------
                        |   8  |   9  |  10  |  11  |
                        -----------------------------

Typically, many regions of this strip are "runs" of many empty cells in a row,
or many fish in a row, or many equally-hungry sharks in a row.  Run-length
encoding is a technique in which a sequence of identical consecutive cells are
represented as a single record or object.  For instance, the following strip of
fish (F), sharks fed two timesteps ago (S2), and empty cells (.):

            ------------------------------------------------------
            | F | F | F | S2 | S2 | S2 | S2 | S2 | . | . | . | . |
            ------------------------------------------------------
              0   1   2    3    4    5    6    7   8   9   10  11

could be represented with just three records, each representing one "run":

                              ------------------
                              | F3 | S2,5 | .4 |
                              ------------------

"F3" means that there are three consecutive fish, followed by "S2,5", meaning
five consecutive sharks fed two timesteps ago, and then ".4":  four empty
cells.  With this encoding, a huge ocean with just a few fish or sharks can be
stored in a tiny amount of memory.  (Note, however, that a shark that just ate
cannot be represented together with a shark that hasn't eaten in the last
timestep.  For a correct encoding, you must separate sharks based on their
hunger!)  If you are familiar with .GIF image files (often encountered on the
Web), you might be interested to know that they use run-length encoding to
reduce their sizes.

Part III:  Converting an Ocean to a Run-Length Encoding
=======================================================

A RunLengthEncoding constructor that takes an Ocean object as its
sole parameter and converts it into a run-length encoding of the Ocean.  