
/**
 * Parent class for all Tetrominos (Tetris pieces)
 * The class has instance fields for an array of four Block_EL objects that make up each Tetromino
 * Thre are also instance fields for the rgb values to determine the color of the Tetromino,
 * for which Block_EL object the other Block_EL objects in the array should be rotated about,
 * and for tracking how many times the Tetromino has been rotated
 * The class also has all necessary methods for moving the Tetromnino left or right,
 * rotating the Tetromino, drawing the Tetromino moving downward and in the queue and hold positions,
 * and some other methods that support these functions.
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public abstract class Tetromino_EL
{

    //rgb values to determine the color for each Tetromino
    private int red;
    private int green;
    private int blue;

    //block within the Tetromino that the piece should be rotated about
    private int r;

    //tracks how many times the Tetromino has been rotated
    private int turns;

    //array of the four Block_EL objects that make up the Tetromino
    private Block_EL[] blocks = new Block_EL[4];

    //Constructor for the Tetromino_EL class
    //Takes parameters for the horizontal and vertical positions of all four Block_EL objects
    //takes parameters to be assigned to the instance vairables for the rgb values
    //takes a parameter to determine which number block the Tetromino will rotate about
    public Tetromino_EL(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, int red, int green, int blue, int r){
        blocks[0] = new Block_EL(x1, y1);
        blocks[1] = new Block_EL(x2, y2);
        blocks[2] = new Block_EL(x3, y3);
        blocks[3] = new Block_EL(x4, y4);
        this.red=red;
        this.green=green;
        this.blue=blue;
        this.r=r;
        //initalizes the counter that tracks the number of rotations done to zero
        turns = 0;
    }

    //draws the Tetromino by calling the drawBlock method on each Block_EL object in the blocks array
    public void drawPiece(){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].drawBlock(red, green, blue);
        }
    }

    //moves the Tetromino down by calling the moveDown method on each Block_EL object in the blocks array
    public void moveDown(){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].moveDown();
        }
    }

    //moves the Tetromino up by calling the moveUp method on each Block_EL object in the blocks array    
    public void moveUp(){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].moveUp();
        }
    }

    //calls the moveRight method provided that doing so would not move the Tetromino beyond the borders of the board
    //and it would not move the Tetromino into an already placed Tetromino
    //to test if moving the Tetromino would move it into an already placed one, the method take a parameter of a 2D array
    //that tracks which cells on the board are already filled, and have a status of true, and if moving the Tetromino would move it into a filled cell, the Tetromino is not moved
    public void moveRight(Cell_EL[][] cells){
        boolean move = true;
        for(int i = 0; i<blocks.length; i++){
            //outer if statement ensure that the value is within the bounds of the cells array
            if(blocks[i].getX()+20<44){
                if(cells[(int)blocks[i].getY()][(int)blocks[i].getX()+20].getStatus()){
                    move=false;
                }       
            }
        }
        if(move){
            for(int i = 0; i<blocks.length; i++){
                blocks[i].moveRight();
            }
        }
    }

    //calls the moveLeft method provided that doing so would not move the Tetromino beyond the borders of the board
    //and it would not move the Tetromino into an already placed Tetromino
    //to test if moving the Tetromino would move it into an already placed one, the method take a parameter of a 2D array
    //that tracks which cells on the board are already filled, and have a status of true, and if moving the Tetromino would move it into a filled cell, the Tetromino is not moved
    public void moveLeft(Cell_EL[][] cells){
        boolean move = true;
        for(int i = 0; i<blocks.length; i++){
            //outer if statement ensure that the value is within the bounds of the cells array
            if(blocks[i].getX()+18<28 && blocks[i].getY()>0){
                if(cells[(int)blocks[i].getY()][(int)blocks[i].getX()+16].getStatus()){
                    move=false;
                }
            }
        }
        if(move){
            for(int i = 0; i<blocks.length; i++){
                blocks[i].moveLeft();
            }
        }
    }

    //rotates the Tetromino about the Block_EL stored at index r (an integer instance field) in the array blocks
    //the intricacies in this method arise from the need to make sure that the Tetromino does not
    //rotate beyond the bounds of the board or into an already placed block
    //Additionally, many steps must be taken to make sure that an out of bounds error does not occur for the cells 2D array parameter 
    public void rotate(Cell_EL[][] cells){
        //it is assumed that the Tetromino can be rotated without issue
        boolean rotatable = true;

        //creates the provisional new coordinates for each Block_EL in blocks by using the 2x2 rotation matrix and applying it to the set of coordinates
        //associated with the position of the Block_EL object at index i
        for(int i = 0; i<blocks.length; i++){
            if(i!=r){
                //If we were rotating about the origin, we would find that for our rotated values, x=-y and y=x
                //But since we are rotating about r, we find that this.x=r.y+(this.x-r.x) this.x=this.y+(this.x-r.x)
                //so as to simulate r.x and r.y being the origin of this rotation
                double provX = blocks[i].getX();
                double provY = blocks[i].getY();
                int x = (int)(blocks[r].getY()+provX-blocks[r].getX());
                //the 18 accounts for the additional horizontal dimension of the cells 2D array beyond the horizontal dimension of the board
                int y = 18+(int)(blocks[r].getX()+(provY-blocks[r].getY())*-1);

                //the outer if statement tests whether or not the x and y values are within the dimensions of the cells array
                //if they are, then the inner if statement test if the element at those indices has a status of true, meaning that the cell on the board is filled and the Tetromino cannot be rotated
                if(x>-1 && x<46 && y<29){
                    if(cells[x][y].getStatus()){
                        rotatable=false;
                    }                    
                }
            }
            //if for any Block_EL object in blocks rotatable is set to false, then the block will not be rotated
        }

        //if rotatable remains true after the above for loop, then the Tetromino is rotated
        if(rotatable){

            //Using the rotation matrix as described above, the horizontal and vertical positions of each Block_EL object to reflect the rotation
            for(int i = 0; i<blocks.length; i++){
                if(i!=r){
                    double provX = blocks[i].getX();
                    double provY = blocks[i].getY();
                    blocks[i].setX(blocks[r].getX()+(provY-blocks[r].getY())*-1);
                    blocks[i].setY(blocks[r].getY()+provX-blocks[r].getX());
                }
            }

            //the counter for how many rotations have been made is updated
            turns++;

            //The extreme values of the Tetromino are found
            double rightMost = this.getRightMost();
            double leftMost = this.getLeftMost();
            double lowest = this.getLowest();

            //This ensure that no piece rotate below the board, and so it is moved up enough such that all parts of the Tetromino are in the board
            if(lowest<1){
                for (int i = 0; i<(rightMost-1)/2; i++){
                    this.moveUp();
                }    
            }

            //When rotating Tetrominos, they tend to shift to the right or left
            //this next section attempts to correct this shift when possible, meaning that correction would not force the Tetromino off the board or into another already placed piece

            //It is assumed that the Tetromino can be corrected until shown otherwise
            boolean fixableRight = true;
            boolean fixableLeft = true;

            //This for loop checks to see if for any Block_EL object in blocks, the rightward correction would cause the Tetromino to run into an already placed piece
            //If it would, then fixableRight is set to false and the rightward correction will not be applied
            for(int i = 0; i<blocks.length; i++){
                //updated positions after correction
                double x = 18+blocks[i].getX()+2;
                double y = blocks[i].getY();
                //the outer if statement tests whether or not the x and y values are within the dimensions of the cells array
                //if they are, then the inner if statement test if the element at those indices has a status of true, meaning that the cell on the board is filled
                if(y<46 && x<27){
                    if(cells[(int)y][(int)x].getStatus()){
                        fixableRight=false;
                    }                    
                }
            }

            //This for loop checks to see if for any Block_EL object in blocks, the leftward correction would cause the Tetromino to run into an already placed piece
            //If it would, then fixableLeft is set to false and the leftward correction will not be applied            
            for(int i = 0; i<blocks.length; i++){
                //updated positions after correction
                double x = 18+blocks[i].getX()-2;
                double y = blocks[i].getY();
                //the outer if statement tests whether or not the x and y values are within the dimensions of the cells array
                //if they are, then the inner if statement test if the element at those indices has a status of true, meaning that the cell on the board is filled
                if(y>0 && x>18 && x<29){
                    if(cells[(int)y][(int)x].getStatus()){
                        fixableLeft=false;
                    }                    
                }
            }
            
            //depending on the number of rotations done, the Tetromino must be corrected either left or right
            //The following if statements determine which way the Tetromino must be corrected 
            
            
            //for a rotations when the remainders of the turns counter is 1 or 2, the Tetromino is corrected by moving it to the right
            //The correction is only applied if the Tetromino is at its farthest edge one cell away from the edge of the board
            if(fixableRight){
                if((turns%4==1 || turns%4==2) && rightMost<9) this.moveRight(cells);
                //this corrects the initial correction if it forces the Tetromino of the board
                if(rightMost>9){
                    for (int i = 0; i<(rightMost-9)/2; i++){
                        this.moveLeft(cells);
                    }
                }                
            }

            //for a rotations when the remainders of the turns counter is 0 or 3, the Tetromino is corrected by moving it to the left
            //The correction is only applied if the Tetromino is at its farthest edge one cell away from the edge of the board
            if(fixableLeft){
                if((turns%4==0 || turns%4==3) && leftMost>-9) this.moveLeft(cells);
                //this corrects the initial correction if it forces the Tetromino of the board
                if(leftMost<-9){
                    for (int i = 0; i<(-9-leftMost)/2; i++){
                        this.moveRight(cells);
                    }
                }  
            }
        }
    }

    //draws the Tetromino in the queue position by calling the queue method on each Block_EL object in the blocks array
    public void queue(int q){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].queue(q,red,green,blue);
        }
    }

    //draws the Tetromino in the hold position by calling the hold method on each Block_EL object in the blocks array
    public void hold(){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].hold(red,green,blue);
        }
    }

    //returns the vertical position of a Block_EL within blocks
    public double getBlockY(int i){
        return blocks[i].getY();
    }

    //returns the horizontal position of a Block_EL within blocks
    public double getBlockX(int i){
        return blocks[i].getX();
    }

    //returns the r value for the rgb integers values that determine the Tetromino's color
    public int getR(){
        return red;
    }

    //returns the g value for the rgb integers values that determine the Tetromino's color
    public int getG(){
        return green;
    }

    //returns the b value for the rgb integers values that determine the Tetromino's color
    public int getB(){
        return blue;
    }

    //finds the rightmost position of a Block_EL within the blocks array, and thus the rightmost point of the Tetromino
    //this is accomplished by finding and returning the greatest horizontal value among those belonging to the elements of the blocks array
    public double getRightMost(){
        double right = -10;
        for(int i = 0; i<blocks.length; i++){
            if(blocks[i].getX()>right) right = blocks[i].getX();
        }
        return right;
    }

    //finds the leftmost position of a Block_EL within the blocks array, and thus the leftmost point of the Tetromino
    //this is accomplished by finding and returning the lowest horizontal value among those belonging to the elements of the blocks array
    public double getLeftMost(){
        double left = 0;
        for(int i = 0; i<blocks.length; i++){
            if(blocks[i].getX()<left) left = blocks[i].getX();
        }
        return left;
    }

    //finds the lowest position of a Block_EL within the blocks array, and thus the lowest point of the Tetromino
    //this is accomplished by finding and returning the smallest horizontal value among those belonging to the elements of the blocks array
    public double getLowest(){
        double bottom = blocks[0].getY();
        for(int i = 1; i<blocks.length; i++){
            if(blocks[i].getY()<bottom) bottom = blocks[i].getY();
        }
        return bottom;
    }

}
