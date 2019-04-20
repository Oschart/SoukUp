// Grid Coordinate Class
public class Cord
    {
        public int x, y, C = 0, S;
        // 3 Metal layers
        // 0 -> No Metal
        // 1 -> Metal
        // 2 -> Metal with a via
        public int metal[] = new int[3];     
        // Array used to rip a constructed route
        public int ripTile[] = new int[3];
        public Cord(int x, int y)
        {
            this.x = x;
            this.y = y;
            this.C = 0;
        }
    }