
/**
 * The Cell_EL class creates an ADT that represents each cell on the Tetris board
 * These cells have instance variables to determine the posisition of the cell, 
 * the status of the cell as being filled with a placed Tetromino,
 * and the color the cell should be colored depending on which if any Tetromino has been place in it
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public class Cell_EL
{
    //boolean for if the cell is filled by a placed piece or not
    private boolean status;


    //half of the length of a side of the cell's square
    private int halfLength;
    
    //rgb values to determine color
    private int red;
    private int green;
    private int blue;
    
    //horizontal and vertical position of the cell
    private int x;
    private int y;
    
    //contructor takes parameters for the stateus and position of the cell
    public Cell_EL(boolean s, int y, int x){
        status=s;

        halfLength = 1;

        //color is initialized to white
        red=255;
        green=255;
        blue=255;
        
        //horizontal position is adjusted due to the excess length of the array representing the board
        //the x value matches where the cell is drawn, not its position in the array
        //there are 18 total units of buffer space on the sides of the array, so 18 must be subtracted
        //from the x value
        this.x=x-18;
        this.y=y;
    }

    //method for a cell to become filled
    //status is set to true to reflect that the cell is filled
    //the colors of the Tetromino that is filling the cell are given as the parameters
    public void setTrue(int r, int g, int b){
        status=true;
        //the color of the cell is updated to be the color of the Tetromino
        red=r;
        green=g;
        blue=b;
    }

    //draws the cell using the information stored in the instance variables
    public void draw(){
        StdDraw.setPenColor(red,green,blue);
        StdDraw.filledSquare(x,y,halfLength);
        // draws outline of cell
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.square(x,y,halfLength);
    }

    //sets the cell as unfilled
    //used when a line is cleared
    //status is set to false and the color of the cell is reset to white
    public void setFalse(){
        status=false;
        red=255;
        green=255;
        blue=255;
    }
    
    //returns the status of the cell, meaning if it is filled or unfilled
    public boolean getStatus(){
        return status;
    }
    
    //return rgb values
    public int getR(){
        return red;
    }
    public int getG(){
        return green;
    }
    public int getB(){
        return blue;
    }
    
    //if the cell in the parameter is filled, then the cell for which this method is called upon
    //has its status set to true and it is given the color of the cell above it
    //otherwise the status of the cell that this is called upon is set to false
    //used when a line is cleared and there are filled cells above it
    public void setEqual(Cell_EL above){
        if(above.getStatus())this.setTrue(above.getR(), above.getG(), above.getB());
        else this.setFalse();
    }
}
