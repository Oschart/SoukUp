SoukUp Maze Routing using Java (and Swing for GUI)
By: Mohamed Abdel Hamed (Oschart)

**********************************
Assumptions:
**************
1- Adjacent metal tiles in the same direction are connect only if they belong the same route/net.
2- The separation/isolation of metal tiles is handled by the design rules.
3- Multiple nets can share the same source and/or target cell.
***********************************
Requirements:
***********************************
- User must have Java installed (preferably Java 8).
- User should also install Swing (for GUI).
***********************************
How to Use:
***********************************
1- In the beginning, the user must enter the valid dimensions of the grid, on which the routing is to be done.
2- The user then enters the via cost which will be used throughout the whole run.
3- The user then inserts the coordinates of the source and target cells (x (0-based), y (0-based), and metal layer(1, 2, or 3)).
4- Any invalid coordinates will mark program termination.
4- After the route is constructed graphically, the user is prompted to choose whether to display the grid in plain text format or not, which 
can be used for disambiguation of grid colors.
5- Note the transparency degree of colors give an indication to the overlapping of metal.
6- The user is then given the choice to rip the most recent route or not.
7- Repeat 3.
***********************************
Limitations
***********************************
1- The current implementation is not the most efficient or optimal way to apply SoukUp's algorithm, and it can be modified to have better performance and optimality.
2- This router is not concerned with design rules, and it assumes a simple cellular model of the chip.
