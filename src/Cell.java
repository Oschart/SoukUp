

import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Cell extends JButton {
    
    private int Metal;
    private Icon Empty, M1, M2, M3, Via;  
    private int x, y;
    
    //constructs a cell with the board being its event listener 
    //Decides whether or not the cell contains a ship part
    public Cell (int Metal){
        this.Metal = Metal;
        this.loadImages();
        //the appearance of each cell
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setMargin(new Insets(0,0,0,0));
        this.setBorder(null);
        
        this.updateIcon();
    }
    
    //constructs a cell with the board being its event listener 
    //content indicates whether or not it has ship and the type of ship as well   
    // 5 -->  Aircraft carrier
    // 4 -->  Battleship
    // 3 -->  Destroyer
    // 2 -->  Submarine
    // 1 -->  Patrol boat
    // 0 -->  no Ship
    
    //sets a new type of ship for the cell
    public void setMetal (int Metal) {
        this.Metal = Metal;
        this.updateIcon();
    }
    
    //set the coordinates of the cell on the board
    public void setCoordinates(int x, int y) {
    	this.x = x; this.y = y;
    }
    
    //returns the x coordinate of the cell on the board
    public int getCoordinateX() {return this.x;}

    //returns the x coordinate of the cell on the board
    public int getCoordinateY() {return this.y;}
    
    //determines the displays the appropriate icon
    private void updateIcon() {
        this.setIcon(this.Metal == 0? Empty: this.Metal == 1? M1: this.Metal == 2? M2: this.Metal == 3? M3: Via);
    }
    
    //loads images from the directory to variables
    private void loadImages() {
        Empty = new ImageIcon(getClass().getResource("tile-00.png"));
        M1 = new ImageIcon(getClass().getResource("tile-01.png"));
        M2 = new ImageIcon(getClass().getResource("tile-02.png"));
        M3 = new ImageIcon(getClass().getResource("tile-03.png"));
        Via = new ImageIcon(getClass().getResource("tile-04.png"));
    }   
}