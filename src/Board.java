
import javax.swing.*;
import java.awt.*;
import static java.lang.Math.min;

// Board class used to graphically display the grid with its metal
public class Board extends JPanel {

    public Cord Grid[][];
    private Color C0, C1, C2, C3, CVia;     // Metal colors
    private int dX, dY;     // Cell width and height
    private int N, M;       // Grid Dimensions

    //Initialize the grid of cells and specify the colors used for layers
    public Board(int N, int M, Cord Grid[][]) {

        this.dX = min((700 - 2 * M) / M, (600 - 2 * N) / N);
        this.dY = dX;   // in order to have square cells

        this.N = N;
        this.M = M;
        this.Grid = Grid;       // Board takes a reference to the original grid
        int alpha = 127;    // 50% transparency for Metal Layer overlapping
        C0 = new Color(160, 160, 160, alpha);
        C1 = new Color(0, 0, 204, alpha);
        C2 = new Color(204, 0, 0, alpha);
        C3 = new Color(0, 153, 0, alpha);
        CVia = new Color(0, 0, 0, alpha);

    }

    @Override
    public void paintComponent(Graphics g) {

        for (int x = 2, j = 0; j < M; x += 2 + dX, ++j) {
            for (int y = 2, i = 0; i < N; y += 2 + dY, ++i) {
                if (Grid[i][j].metal[0] + Grid[i][j].metal[1] + Grid[i][j].metal[2] == 0) {
                    g.setColor(C0);
                    g.fillRect(x, y, dX, dY);
                } else {
                    for (int k = 0; k <= 2; ++k) {
                        if (Grid[i][j].metal[k] > 0) {
                            g.setColor((k == 0 ? C1 : k == 1 ? C2 : C3));
                            g.fillRect(x, y, dX, dY);
                            if (Grid[i][j].metal[k] == 2) {  // Cell contains a via
                                g.setColor(CVia);
                                g.fillOval(x + dX / 4, y + dY / 4, dX / 2, dY / 2);
                            }
                        }
                    }
                }
            }
        }

        repaint();
    }

}
