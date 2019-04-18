
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
import javax.swing.JPanel;

public class soukup extends JFrame {

    public static Cord src, targ, curr, nb;
    public static Stack<Cord> RO, RN, NB;
    public static int N = 10, M = 10;
    public static int ViaCount = 0, ViaCost = 3, CellCount = 0;
    public static Cord Grid[][];

    // For Grid Graphical Display
    public static Board Chip;

    public soukup() {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert Grid Dimensions:");
        N = input.nextInt();
        M = input.nextInt();     // Board Dimensions
        //this.setLayout(new GridBagLayout());
        this.setTitle("SoukUp");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 500);
        this.setMinimumSize(new Dimension(800, 700));
        this.setLocation(500, 100);

        Grid = new Cord[N][M];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Grid[i][j] = new Cord(j, i, 0, 0);
            }
        }
        
        Chip = new Board(N, M, Grid);

        GridBagConstraints format = new GridBagConstraints();

        // Placing Components
        format.weightx = 0.5;
        format.fill = GridBagConstraints.HORIZONTAL;
        format.gridx = 0;
        format.gridy = 0;
        //this.add(Chip, format);
        this.add(Chip);

        this.setVisible(true);

        RO = new Stack();
        RN = new Stack();
        NB = new Stack();

        
        getInput();

        //SRC
        while (src.x >= 0 && src.y >= 0) {
            Step2();
            getInput();
        }

    }

    public static void getInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("Insert Source Cell x, y, and Metal Layer:");
        src = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 2);
        System.out.println("Insert Target Cell x, y, and Metal Layer:");
        targ = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 0);
        
        src.S = 5;
        targ.S = 6;
        Grid[src.y][src.x] = src;
        Grid[targ.y][targ.x] = targ;
        RN.push(src);
    }

    public static Boolean Step2() {
        while (!RN.empty()) {
            RO.push(RN.pop());
        }
        return Step3();
    }

    public static Boolean Step3() {
        while (!RO.empty()) {
            curr = RO.pop();
            //System.out.println("Candidate = " + curr.x + ", " + curr.y);

            if (curr.x > 0) {
                NB.push(Grid[curr.y][curr.x - 1]);
            }
            if (curr.x < N - 1) {
                NB.push(Grid[curr.y][curr.x + 1]);
            }
            if (curr.y > 0) {
                NB.push(Grid[curr.y - 1][curr.x]);
            }
            if (curr.y < M - 1) {
                NB.push(Grid[curr.y + 1][curr.x]);
            }

            while (!NB.empty()) {
                nb = NB.pop();
                
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
                if (nb.S == 6) {
                    return Step8(traceB);
                }
                if (nb.C == 2 || isObstacle(traceB)) {
                    continue;
                } else if (nb.C <= 1 && sameDirection(traceB)) {

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
                    System.out.println("Candidate2 = " + nb.x + ", " + nb.y + ", " + nb.S);
                    RN.push(nb);
                }

            }
            while(!NB.empty()) NB.pop();
            //Chip.Grid[curr.y][curr.x].setMetal(0);
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
            //System.out.println("Step5");
        }
        return Step6();
    }

    public static Boolean Step6() {

        nb.C = 2;
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
        if (nb.y + dy < 0 || nb.y + dy > N - 1 || nb.x + dx < 0 || nb.x + dx > M - 1) {
            return Step3();
        }
        nb = Grid[nb.y + dy][nb.x + dx];
        int dis2 = abs(nb.x - targ.x) + abs(nb.y - targ.y);
        // Hit an obstacle
        if (nb.C == 2 || isObstacle((dx != 0? 1: 3))) {
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

    public static Boolean Step8(int traceB) {

        System.out.println("Connection Found!");
        nb.S = traceB;
        int dx = (nb.S == 1 ? -1 : (nb.S == 2 ? 1 : 0));
        int dy = (nb.S == 3 ? 1 : (nb.S == 4 ? -1 : 0));
        Cord par = nb, ch = Grid[nb.y + dy][nb.x + dx];
        while (ch.S != 5) {
            if (dy != 0) {  // If direction is vertical
                ch.metal[1] = 1;
                if (par.metal[1] == 0) {
                    par.metal[1] = 2;   // Place a via
                    ch.metal[1] = 2;
                    ViaCount++;
                    //Chip.Grid[par.y][par.x].setMetal(5);
                }
            } else if (dx != 0) {   // If direction is horizontal
                if (par.metal[0] > 0 && ch.metal[0] == 0)
                {
                    System.out.println("JOJOJO");
                    ch.metal[0] = 1;
                }
                else if(par.metal[2] > 0 && ch.metal[2] == 0)
                {
                    ch.metal[2] = 1;
                }
                else 
                {
                    if(ch.metal[0] == 0) ch.metal[0] = par.metal[0] = 2;
                    else ch.metal[2] = par.metal[2] = 2;
                    ViaCount++;
                    //Chip.Grid[par.y][par.x].setMetal(5);
                }
            }
            //System.out.println("CandidateR = " + ch.x + ", " + ch.y + ", " + ch.S + " " + ch.metal[0] + " " + ch.metal[1] + " " + ch.metal[2]);
            //Chip.Grid[ch.y][ch.x].setMetal(topLayer(ch));

            CellCount++;
            par = ch;
            dx = (ch.S == 1 ? -1 : (ch.S == 2 ? 1 : 0));
            dy = (ch.S == 3 ? 1 : (ch.S == 4 ? -1 : 0));
            ch = Grid[ch.y + dy][ch.x + dx];
        }
        int Cost = 2 + CellCount + ViaCost * ViaCount;

//        for (int i = 0; i < N; ++i) {
//            for (int j = 0; j < M; ++j) {
//                System.out.print((Grid[i][j].m));
//            }
//            System.out.println("");
//        }
        System.out.println("Cost = " + Cost);
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Grid[i][j].S = 0;
                Grid[i][j].C = 0;
            }
        }
        while (!NB.empty()) {
            NB.pop();
        }
        while (!RN.empty()) {
            RN.pop();
        }
        while (!RO.empty()) {
            RO.pop();
        }
        return true;
    }

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

    public static Boolean isObstacle(int traceB) {
        if(nb.metal[0] > 0 && nb.metal[2] > 0 && nb.metal[0] > 0) return true;
        if (traceB <= 2) {
            return (nb.metal[0] > 0 && nb.metal[2] > 0);
        } else {
            return (nb.metal[1] == 1);
        }
    }

    public static int topLayer(Cord tile) {
        int i = 3;
        while (tile.metal[i - 1] != 1 && i >= 0) {
            --i;
        }
        return i;
    }

    public static void main(String[] args) {
        soukup router = new soukup();
    }
}
