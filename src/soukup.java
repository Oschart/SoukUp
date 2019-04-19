
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import static java.lang.Math.abs;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class soukup extends JFrame {

    // Source, Target, Current Cell, and Neighbor cell
    public static Cord src, targ, curr, nb;
    // Source and Target metal types
    public static int srcMetal, targMetal;
    // R old, R new, and Neighbor stacks
    public static Stack<Cord> RO, RN, NB;
    // Grid Dimensions
    public static int N, M;
    public static int ViaCount = 0, ViaCost, CellCount = 0;
    // Start and end timestamps of execution for one route (used to get elapsed time)
    public static long startTime, endTime;
    public static Cord Grid[][];

    // For Grid Graphical Display
    public static Board Chip;

    public soukup() {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert Grid Dimensions:");
        // Board Dimensions
        N = input.nextInt();
        M = input.nextInt();  
        System.out.println("Insert Via Cost:");
        ViaCost = input.nextInt();
        
        this.setTitle("SoukUp");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 700));
        this.setLocation(500, 100);

        // Initialize the grid with empty cells (no metal)
        Grid = new Cord[N][M];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Grid[i][j] = new Cord(j, i, 0, 0);
            }
        }
        Chip = new Board(N, M, Grid);
        this.add(Chip);
        this.setVisible(true);

        // Initialize the stacks used in the algorithm
        RO = new Stack();
        RN = new Stack();
        NB = new Stack();

        getInput();
        while (src.x >= 0 && src.y >= 0) {  // run while x-y coordinates are not negative
            startTime = System.nanoTime();
            Step2();
            getInput();
        }

    }

    // Method that takes source and target cells information
    public static void getInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("Insert Source Cell x, y, and Metal Layer:");
        src = new Cord(in.nextInt(), in.nextInt(), srcMetal = in.nextInt(), 2);
        System.out.println("Insert Target Cell x, y, and Metal Layer:");
        targ = new Cord(in.nextInt(), in.nextInt(), targMetal = in.nextInt(), 0);
        
        srcMetal--;
        targMetal--;    // To use them as 0-based index
        src.S = 5;
        targ.S = 6;
        Grid[src.y][src.x] = src;
        Grid[targ.y][targ.x] = targ;
        RN.push(src);
    }

    // Move RN into RO
    public static Boolean Step2() {
        while (!RN.empty()) {
            RO.push(RN.pop());
        }
        return Step3();
    }

    // Method responsible for bubbling and initiating the route
    public static Boolean Step3() {
        while (!RO.empty()) {
            curr = RO.pop();
            // Add the viable neighbors of the current cell to the NB (neighbor) stack
            if (curr.x > 0) {
                NB.push(Grid[curr.y][curr.x - 1]);
            }
            if (curr.x < M - 1) {
                NB.push(Grid[curr.y][curr.x + 1]);
            }
            if (curr.y > 0) {
                NB.push(Grid[curr.y - 1][curr.x]);
            }
            if (curr.y < N - 1) {
                NB.push(Grid[curr.y + 1][curr.x]);
            }
            
            // Iterate over each neighbor
            while (!NB.empty()) {
                nb = NB.pop();
                // Traceback code that specifies direction
                int traceB;
                if (nb.x < curr.x) {
                    traceB = 2;
                } else if (nb.x > curr.x) {
                    traceB = 1;
                } else if (nb.y < curr.y) {
                    traceB = 3;
                } else {
                    traceB = 4;
                }
                // Discard the cell if it hits an obstacle
                if (nb.C == 2 || isObstacle(traceB)) {
                    continue;
                }
                // Go to traceback step (step8) if target is reached
                if (nb.S == 6) {
                    return Step8(traceB);
                }
                // If neighbor is in the same direction as target
                if (nb.C <= 1 && sameDirection(traceB)) {

                    RN.push(nb);
                    nb.C = 1;
                    if (nb.S <= 4) {
                        nb.S = traceB;
                    }
                    return Step5();
                } else if (nb.C == 0) {

                    nb.C = 1;
                    if (nb.S <= 4) {
                        nb.S = traceB;
                    }
                    RN.push(nb);
                }

            }
            // Empty the neighbor stack before picking a new cell
            while (!NB.empty()) {
                NB.pop();
            }
        }
        return Step4();
    }

    public static Boolean Step4() {
        if (RN.empty()) {
            System.out.println("No Connection");
            return false;
        }
        return Step2();

    }

    public static Boolean Step5() {

        while (!RN.empty()) {
            RO.push(RN.pop());
        }
        return Step6();
    }

    public static Boolean Step6() {

        nb.C = 2;
        // Set the traceback code for neighbor
        if (nb.S <= 4) {
            if (nb.x < curr.x) {
                nb.S = 2;
            } else if (nb.x > curr.x) {
                nb.S = 1;
            } else if (nb.y < curr.y) {
                nb.S = 3;
            } else {
                nb.S = 4;
            }
        }
        RO.push(nb);
        return Step7();

    }

    public static Boolean Step7() {
        int dx = (nb.S == 1 ? 1 : (nb.S == 2 ? -1 : 0));
        int dy = (nb.S == 3 ? -1 : (nb.S == 4 ? 1 : 0));
        int dis1 = abs(nb.x - targ.x) + abs(nb.y - targ.y);
        // If neighbor goes out of bounds
        if (nb.y + dy < 0 || nb.y + dy > N - 1 || nb.x + dx < 0 || nb.x + dx > M - 1) {
            return Step3();
        }
        nb = Grid[nb.y + dy][nb.x + dx];
        int dis2 = abs(nb.x - targ.x) + abs(nb.y - targ.y);
        // Hit an obstacle
        if (nb.C == 2 || isObstacle((dx != 0 ? 1 : 3))) {
            return Step3();
        }
        // Reached Target
        if (nb.S == 6) {
            return Step8((dx > 0 ? 1 : (dx < 0 ? 2 : (dy < 0 ? 3 : 4))));
        }

        // Check if it's closer to target
        if (dis2 >= dis1) {
            return Step3();
        }
        return Step6();

    }

    // Traceback Step that specifies metal types and vias
    public static Boolean Step8(int traceB) {

        System.out.println("Connection Found!");
        CellCount = ViaCount = 0;
        nb.S = traceB;
        int dx = (nb.S == 1 ? -1 : (nb.S == 2 ? 1 : 0));
        int dy = (nb.S == 3 ? 1 : (nb.S == 4 ? -1 : 0));
        Cord par = nb, ch = Grid[nb.y + dy][nb.x + dx];     // Parent cell
        int prevMetal = targMetal;
        int ripCntr = 0;
        par.ripTile[targMetal] = 1;
        while (ch.S != 5) {
            ++ripCntr;
            if (dy != 0) {  // If direction is vertical
                ch.metal[1] = 1;
                ch.ripTile[1] = 1;
                if (prevMetal != 1) {
                    par.metal[1] = par.metal[prevMetal] = 2;   // Place a via
                    par.ripTile[prevMetal] = 1;
                    par.ripTile[1] = 1;
                    prevMetal = 1;
                    ViaCount++;
                }
            } else if (dx != 0) {   // If direction is horizontal
                if (prevMetal == 0 && ch.metal[0] == 0) {
                    ch.metal[0] = 1;
                    ch.ripTile[0] = 1;
                } else if (prevMetal == 2 && ch.metal[2] == 0) {
                    ch.metal[2] = 1;
                    ch.ripTile[2] = 1;
                } else {    // A Via is necessary
                    if (ch.metal[0] == 0) {     // Metal 1
                        if (ch.metal[prevMetal] == 0 && prevMetal != 1) {
                            ch.metal[0] = ch.metal[prevMetal] = 2;

                            ch.ripTile[prevMetal] = 2;
                        } else {
                            par.metal[0] = par.metal[prevMetal] = 2;
                            par.ripTile[prevMetal] = 1;
                            par.ripTile[0] = 1;
                        }
                        ch.ripTile[0] = 1;
                        prevMetal = 0;
                        ch.metal[0] = 1;
                    } else {        // Metal 3
                        if (ch.metal[prevMetal] == 0 && prevMetal != 1) {
                            ch.metal[2] = ch.metal[prevMetal] = 2;
                            ch.ripTile[prevMetal] = 1;
                        } else {
                            par.metal[2] = par.metal[prevMetal] = 2;
                            par.ripTile[prevMetal] = 2;
                            par.ripTile[2] = 1;
                        }
                        prevMetal = 2;
                        ch.ripTile[2] = 1;
                        ch.metal[2] = 1;
                    }
                    ViaCount++;
                }
            }
            CellCount++;
            par = ch;
            dx = (ch.S == 1 ? -1 : (ch.S == 2 ? 1 : 0));
            dy = (ch.S == 3 ? 1 : (ch.S == 4 ? -1 : 0));
            ch = Grid[ch.y + dy][ch.x + dx];

            // Remove the constructed route if it exceeds a certain length
            if (ripCntr > 300) {
                return ripRoute();
            }
        }
        // Check for needed vias for the source cell
        if(dx != 0 && prevMetal == 1) 
        {
            if (par.metal[0] == 0) {
                par.metal[srcMetal] = par.metal[0] = 2;
                par.ripTile[0] = 1;
                prevMetal = 0;
            } else {
                par.metal[srcMetal] = par.metal[2] = 2;
                par.ripTile[2] = 1;
                prevMetal = 2;
            }
            ++ViaCount;
        }
        if (prevMetal != srcMetal) {
            ch.metal[srcMetal] = ch.metal[prevMetal] = 2;
            ch.ripTile[prevMetal] = 1;
            ViaCount++;
        }
        ch.ripTile[srcMetal] = 1;
        endTime = System.nanoTime();
        int Cost = 2 + CellCount + ViaCost * ViaCount;
        System.out.println("Cost = " + Cost);
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed Time = " + elapsedTime);
        JOptionPane.showMessageDialog(null, "    Connection Found!\n Cost = " + Cost + "\n Elapsed Time = " + elapsedTime/1000.0 + " ms", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Reset the codes of the grid
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Grid[i][j].S = 0;
                Grid[i][j].C = 0;
            }
        }
        // Empty the stacks
        while (!NB.empty()) {
            NB.pop();
        }
        while (!RN.empty()) {
            RN.pop();
        }
        while (!RO.empty()) {
            RO.pop();
        }
        System.out.println("To Rip The Previous Route, insert 1, and 0 otherwise:");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        if(x == 1) ripRoute();
        return true;
    }

    // Check if specified direction approaches the target
    public static Boolean sameDirection(int traceB) {

        switch (traceB) {
            case 1:
                return nb.x < targ.x;
            case 2:
                return nb.x > targ.x;
            case 3:
                return nb.y > targ.y;
            default:
                return nb.y < targ.y;
        }
    }

    // Check if neighbor is an obstacle
    public static Boolean isObstacle(int traceB) {
        if (nb.metal[0] != 0 && nb.metal[2] != 0 && nb.metal[0] != 0) {
            return true;
        }
        if (traceB <= 2) {  // Horizontal
            return (nb.metal[0] != 0 && nb.metal[2] != 0);
        } else {        // Vertical
            // Either M2 is already occupied, or there's a via between M1 and M3
            return (nb.metal[1] != 0 || nb.metal[0] + nb.metal[2] == 4);
        }
    }

    // Print the grid layer by layer in plain text format
    // 0 -> no metal
    // 1 -> metal
    // 2 -> metal with a via
    public static void printGrid() {
        for (int k = 0; k < 3; ++k) {
            System.out.println("Layer " + (k + 1) + ": ");
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < M; ++j) {
                    System.out.print((Grid[i][j].metal[k]));
                }
                System.out.println("");
            }
        }
    }

    // Rips the last constructed route/net
    // Warning: it might affect previous nets if they have common cells
    public static Boolean ripRoute() {
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                for (int k = 0; k < 3; ++k) {
                    if (Grid[i][j].ripTile[k] == 1) {
                        Grid[i][j].metal[k] = 0;      // Remove added metal 
                    } 
                    Grid[i][j].ripTile[k] = 0;
                }
            }
        }
        return Step3();
    }

    public static void main(String[] args) {
        soukup router = new soukup();
    }
}
