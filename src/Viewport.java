public final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }

   public int getRow() {
      return row;
   }

   public int getCol() {
      return col;
   }

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

   /**
    * Set the viewport's row and column counts to the specified values.
    */
   public void shift(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   /**
    * Check if the viewport contains the specified Point p.
    */
   public boolean contains(Point p)
   {
      return p.getY() >= this.row && p.getY() < this.row + this.numRows &&
              p.getX() >= this.col && p.getX() < this.col + this.numCols;
   }


   public Point viewportToWorld(int col, int row)
   {
      return new Point(col + this.col, row + this.row);
   }

   public  Point worldToViewport(int col, int row)
   {
      return new Point(col - this.col, row - this.row);
   }


}
