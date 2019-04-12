
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Scanner;
import java.util.Stack;
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
        this.setLayout(new GridBagLayout());
        this.setTitle("SoukUp");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 500);
        this.setMinimumSize(new Dimension(450, 450));
        this.setLocation(500, 100);

        Chip = new Board(N, M);

        GridBagConstraints format = new GridBagConstraints();

        // Placing Components
        format.weightx = 0.5;
        format.fill = GridBagConstraints.HORIZONTAL;
        format.gridx = 0;
        format.gridy = 0;
        this.add(Chip, format);

        this.setVisible(true);

        RO = new Stack();
        RN = new Stack();
        NB = new Stack();

        Grid = new Cord[N][M];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                Grid[i][j] = new Cord(i, j, 0, 0);
            }
        }
        getInput();

        //SRC
        //while (src.x >= 0 && src.y >= 0 && src.m >= 0) {
        Step2();
        getInput();
        //}

    }

    public static void getInput() {
        Scanner in = new Scanner(System.in);
        System.out.println("Insert Source Cell x, y, and Metal Layer:");
        src = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 2);
        System.out.println("Insert Target Cell x, y, and Metal Layer:");
        targ = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 0);
        
        Chip.Grid[src.x][src.y].setMetal(src.m);
        Chip.Grid[targ.x][targ.y].setMetal(targ.m);
        src.S = 5;
        targ.S = 6;
        Grid[src.x][src.y] = src;
        Grid[targ.x][targ.y] = targ;
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
            //System.out.println("NOT EMPTY");
            curr = RO.pop();
            //System.out.println("Candidate = " + curr.x + ", " + curr.y);
            if (curr.x > 0) {
                NB.push(Grid[curr.x - 1][curr.y]);
            }
            if (curr.x < N - 1) {
                NB.push(Grid[curr.x + 1][curr.y]);
            }
            if (curr.y > 0) {
                NB.push(Grid[curr.x][curr.y - 1]);
            }
            if (curr.y < M - 1) {
                NB.push(Grid[curr.x][curr.y + 1]);
            }

            while (!NB.empty()) {
                nb = NB.pop();
                //System.out.println("Candidate = " + nb.x + ", " + nb.y + ", " + nb.S);

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
                if (nb.C == 2 || nb.m == 3) {
                    continue;
                } else if (nb.C <= 1 && (nb.x == targ.x || nb.y == targ.y)) {

                    RN.push(nb);
                    nb.C = 1;
                    if (nb.S <= 4) {
                        nb.S = traceB;
                    }
                    System.out.println("Candidate = " + nb.x + ", " + nb.y + ", " + nb.S);
                    //System.out.println("Candidate = " + nb.x + ", " + nb.y);
                    return Step5();
                } else if (nb.C == 0) {

                    nb.C = 1;
                    if (nb.S <= 4) {
                        nb.S = traceB;
                    }
                    System.out.println("Candidate0 = " + nb.x + ", " + nb.y + ", " + nb.S);
                    RN.push(nb);
                }

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
        System.out.println("Candidate6 = " + nb.x + ", " + nb.y + ", " + nb.S);
        RO.push(nb);
        return Step7();

    }

    public static Boolean Step7() {
        int dx = (nb.x > targ.x ? -1 : (nb.x < targ.x ? 1 : 0));
        int dy = (nb.y > targ.y ? -1 : (nb.y < targ.y ? 1 : 0));
        int metal = nb.m;
        //int pS = nb.S;
        nb = Grid[nb.x + dx][nb.y + dy];
        //nb.S = pS;
        System.out.println("Candidate7 = " + nb.x + ", " + nb.y + ", " + nb.S);
        // Hit an obstacle
        if (nb.C == 2 || nb.m == 3 || (nb.m == 2 && metal == 2)) {
            return Step3();
        }
        // Reached Target
        if (nb.S == 6) {
            return Step8((dx > 0 ? 1 : (dx < 0 ? 2 : (dy < 0 ? 3 : 4))));
        }

        // SSSP
        return Step6();

    }

    public static Boolean Step8(int traceB) {
        System.out.println("Connection Found!");
        nb.S = traceB;
        int dx = (nb.S == 1 ? -1 : (nb.S == 2 ? 1 : 0));
        int dy = (nb.S == 3 ? 1 : (nb.S == 4 ? -1 : 0));
        Cord par = nb, ch = Grid[nb.x + dx][nb.y + dy];
        while (ch.S != 5) {
            System.out.println("CandidateR = " + ch.x + ", " + ch.y + ", " + ch.S);
            dx = (ch.S == 1 ? -1 : (ch.S == 2 ? 1 : 0));
            dy = (ch.S == 3 ? 1 : (ch.S == 4 ? -1 : 0));

            Chip.Grid[ch.x][ch.y].setMetal(++ch.m);
            // Via Condition
            if (ch.m != par.m || ch.S != par.S) {
                ViaCount++;
                ch.via = true;
                Chip.Grid[ch.x][ch.y].setMetal(5);
            }
            CellCount++;
            par = ch;
            ch = Grid[ch.x + dx][ch.y + dy];
        }
        int Cost = 2 + CellCount + ViaCost * ViaCount;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                System.out.print((Grid[i][j].m));
            }
            System.out.println("");
        }
        System.out.println("Cost = " + Cost);
        return true;
    }

    public static void main(String[] args) {
        soukup router = new soukup();
    }
}
