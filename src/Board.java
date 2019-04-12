
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.AncestorListener;

import java.util.Random;
import java.awt.*;

public class Board extends JPanel{

    public Cell Grid[][];
    private JOptionPane pane = new JOptionPane();

    //Initialize the grid of cells then places the ships of the grid
    public Board(int N, int M) {

        this.setLayout(new GridLayout(N, M));	
        Grid = new Cell[N][M];	
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                this.Grid[i][j] = new Cell(0);
                this.Grid[i][j].setCoordinates(i, j);
                this.add(this.Grid[i][j]);
            }
        }
    }

}

    