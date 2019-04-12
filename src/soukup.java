
import java.util.Scanner;
import java.util.Stack;

public class soukup {

    public static Cord src, targ;
    public static Stack<Cord> RO, RN;
    public static int N, M;
    public static Cord Grid[][];
    public static void main(String[] args) {
        //src = null, targ = null;
        RO = new Stack();
        RN = new Stack();
        Scanner input = new Scanner(System.in);
        System.out.println("Insert Grid Dimensions:");
        int N = input.nextInt(), M = input.nextInt();     // Board Dimensions

        getInput(src, targ);
        Grid = new Cord[N][M];
        //SRC
        while (src.x >= 0 && src.y >= 0 && src.m >= 0) {
            
            
            
            getInput(src, targ);
        }

    }

    public static void getInput(Cord src, Cord targ) {
        Scanner in = new Scanner(System.in);
        System.out.println("Insert Source Cell x, y, and Metal Layer:");
        src = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 2);
        System.out.println("Insert Target Cell x, y, and Metal Layer:");
        targ = new Cord(in.nextInt(), in.nextInt(), in.nextInt(), 2);
        
        RN.push(src);
    }
    
    public static void Step2()
    {
        for (int i = 0; i < RN.size(); ++i) {
                RO.push(RN.pop());
            }
    }
    
    public static void Step3()
    {
        while (true) {
                Cord curr = RO.pop();
                Stack<Cord> NB = new Stack();
                if (curr.x > 0) {
                    NB.push(Grid[curr.x - 1][curr.y]);
                }
                if (curr.x < N - 1) {
                    NB.push(Grid[curr.x + 1][curr.y]);
                }
                if (curr.y > 0) {
                    NB.push(Grid[curr.x][curr.y - 1]);
                }
                if (curr.x < M - 1) {
                    NB.push(Grid[curr.x][curr.y + 1]);
                }

                while (!NB.empty()) {
                    Cord nb = NB.pop();
                    if (nb.C == 2 || nb.S == 7) {
                        continue;
                    }
                    if (nb.S == 6);//BREAK;
                    else if(nb.C <= 1 && (nb.x == targ.x || nb.y == targ.y))
                    {
                        
                    }
                    else if (nb.C == 0) {
                        RN.push(nb);
                        nb.C = 1;
                        if(nb.S <= 4)
                        {
                            if(nb.x < curr.x) nb.S = 1; 
                            else if(nb.x > curr.x) nb.S = 2; 
                            else if(nb.y < curr.y) nb.S = 3; 
                            else nb.S = 4; 
                        }
                    }

                }

            }
    }
}
