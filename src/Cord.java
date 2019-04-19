public class Cord
    {
        public int x, y, C = 0, S;
        public int metal[] = new int[3];
        public int ripTile[] = new int[3];
        public Cord(int x, int y, int m, int C)
        {
            this.x = x;
            this.y = y;
            if(m > 0) this.metal[m-1] = 1;
            this.C = C;
        }
    }