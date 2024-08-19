
/**
 * The block ADT has parameters for the horizontonal and vertical positions of a 2x2 square
 * The class has methods to update the position two to left or right and up or down
 * There are also methods to set the position of the block using parameters
 * There are also methods to draw the block in its starting position, as well as the draw the block in modified positions for it to be in the queue or hold position in the Tetris game
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public class Block_EL
{
    //x is the horizontal position of the block
    private double x;
    //y is the vertical position of the block
    private double y;
    //radius is half the length of the block, which is 1
    private final double radius = 1;
    
    //Constructor, has parameters to assgin the position of the block
    public Block_EL(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    //updates the vertical position to be two higher
    public void moveUp(){
        y+=2;
    }
    
    //updates the vertical position to be two lower
    public void moveDown(){
        y-=2;
    }
    
    //updates the horizontal position to be two to the right
    public void moveRight(){
        x+=2;
    }
    
    //updates the horizontal position to be two to the left
    public void moveLeft(){
        x-=2;
    }
    
    //draws the block, takes parameters to set the color of the block
    public void drawBlock(int r, int g, int b){
        //draws the body of the block with the correct color
        StdDraw.setPenColor(r,g,b);
        StdDraw.filledSquare(x,y,radius);
        
        //draws the outline of the block
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.square(x,y,radius);
    }
    
    //returns the horizontal position of the block
    public double getX(){
        return x;
    }
    
    //return the vertical position of the block
    public double getY(){
        return y;
    }
    
    //sets the horizontal position of the block
    public void setX(double X){
        x=X;
    }
    
    //sets the vertical position of the block
    public void setY(double Y){
        y=Y;
    }
    
    //draws the block in the queue position, updates the position accordingly
    public void queue(int i, int r, int g, int b){
        //set color to proper color based on parameters
        StdDraw.setPenColor(r,g,b);
        
        //Draws block 15 to the right and 5+5*i down from its starting position
        double rx = this.x+15;
        double ry = this.y-5-(5*i);
        StdDraw.filledSquare(rx,ry,radius);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.square(rx,ry,radius);
    }
    
    //draws the block in the queue position, updates the position accordingly    
    public void hold(int r, int g, int b){
        //set color to proper color based on parameters
        StdDraw.setPenColor(r,g,b);
        
        //Draws block 15 to the left and 7 down from its starting position
        // 
        double rx = this.x-15;
        double ry = this.y-7;
        StdDraw.filledSquare(rx,ry,radius);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.square(rx,ry,radius);
    }
}
