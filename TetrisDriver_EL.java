import java.util.ArrayList;
/**
 * This is the driver class to play Tetris
 * It uses objects of the Tetromino_EL class and its child classes as well as the Cell_EL class
 * This class draws the board and interface for the Tetris game and accepts all commands from the user
 * The class calls methods from the Cell_EL class and the Tetromino_EL class in association with different commands given by the user and the needs of the game
 * The commands from the user come in the form of pressing different keys, including the arrow keys, the space bar, the c key, and the p key.
 * For specifics on how the class accomplishes the necessary components to facilitate a game of Tetris, see the documentation throughout the code
 * The class keeps the players score and runs until the player loses the game of Tetris
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public class TetrisDriver_EL
{
    public static void main (String[] args){
        StdDraw.setXscale(-20.0, +20.0);
        StdDraw.setYscale(0, +40.0);

        //creates a 2D array of type Cell_EL to represent the board
        //excess dimensions are given to allow for buffer room so that rotating the piece does not result in an out of bounds error
        int boardHeight = 50;
        int boardWidth = 28;
        Cell_EL[][] board = new Cell_EL[boardHeight][boardWidth];

        //the board contains ten units of padding above the board and nine units on either side
        int upperBuffer = 10;
        int sideBuffer = 18;

        //all elements in the array are initialized to having a false status, meaning that they are unfilled,
        //and with the position within the array for the x and y coordinates of the cell pending alteration in the Cell_EL class contructor
        for(int i = 1; i < boardHeight; i++){
            for (int j = 1; j< boardWidth; j++){
                board[i][j] = new Cell_EL(false, i, j);
            }
        }

        //ArrayList that contains the Tetrominos that are in play, meaning that they are either moving on screen or are in the queue
        ArrayList<Tetromino_EL> inPlay = new ArrayList<Tetromino_EL>();
        int inPlaySize = 4;

        //Arraylist containing the integers that correspond to the parameter nextPiece(int num) method needed to return the Tetromino types at the same index in the inPlay ArrayList
        ArrayList<Integer> queueNum = new ArrayList<Integer>();

        // there are seven possible pieces that could be put in play
        int numPieces = 7;
        
        // each tetromino cotains four blocks
        int numBlocks = 4;

        for(int i=0; i<inPlaySize; i++){
            //creates a random number to enter into the nextPiece(int num) method to return a random Tetromino to add to the inPlay ArrayList
            int num = (int)(Math.random()*numPieces);
            inPlay.add(nextPiece(num));
            //adds num to the queueNum ArrayList
            queueNum.add(num);
        }


        //holdNum contains the integer corresponding to the parameter nextPiece(int num) method needed to return the Tetromino type contained in the hold ArrayList
        int holdNum=-1;

        StdDraw.enableDoubleBuffering();
        //boolean that determines if the game is live
        boolean play = true;
        //pause between movement downward by the Tetromino
        int pause;
        int level;
        int linesCleared=0;
        int points = 0;
        

        while(play){
            //a new Tetromino is added to inPlay at the end of the loop

            //boolean value for determining if the Tetromino in use is still in play
            boolean live = true;
            //only one hold is permitted per new Tetromino, so the value is reset to true when a new Tetromino comes into play
            boolean held = true;
            //the boolean move determines if the game is paused or not, so it is reset to true when a new Tetromino comes into play
            boolean move = true;
            //boolean that determines if the Tetromino can move any further downward
            boolean downAble;
            //tracks how many lines have been cleared by placing the current Tetromino, reset to zero for each new Tetromino
            int thisClear=0;
            //pause is the amount of time before the Tetromino moves down again
            //it is updated based on how many lines the player has cleared
            //the game gets 5 milliseconds faster for every ten lines the player has cleared
            pause=200-5*(linesCleared/10);
            //the level the player has reached goes up by one for every ten lines the player has cleared
            level=1+linesCleared/10;

            //while loop that runs while the Tetromino in play has not been placed yet
            while(live){

                //redraws the board after every movement by the Tetromino
                StdDraw.clear(StdDraw.WHITE);
                drawBoundaries(points, linesCleared, level);

                //draws the first Tetromino in inPlay
                //This is the in play Tetromino
                inPlay.get(0).drawPiece();


                //draws the second, third, and fourth Tetrominos in inPlay in the queue position
                for(int i=1; i<inPlaySize; i++){
                    inPlay.get(i).queue(i);
                }

                //draws the contents of the board array
                for(int i = 1; i<boardHeight - upperBuffer; i+=2){
                    for (int j = sideBuffer/2; j< boardWidth + 1; j+=2){
                        board[i][j].draw();
                    }
                }

                //The rest of this while loop concerns the in play Tetromino
                
                
                //if the game is unpaused, the moveDown() method is called on the in play Tetromino
                //this moves the Tetromino down towards where it will go out of play
                if(move) inPlay.get(0).moveDown();

                //These methods allows the player to either hard drop the in Play Tetromino or to accelerate its movement downward
                //The downable boolean value represents whether the Tetromino can be moved downed any further
                downAble = true;

                //40 is the integer code for the down arrow
                if(downAble && StdDraw.isKeyPressed(40)){
                    //If the down arrow is pressed, the Tetromino will move down one row again without any additional pause
                    //so long as the following for loop does not detect any filled cells directly below the Tetromino
                    for(int i = 0; i<4; i++){
                        //checks cell below the Block_EL object
                        if(inPlay.get(0).getBlockY(i)<=2 || board[(int)inPlay.get(0).getBlockY(i)-2][(int)inPlay.get(0).getBlockX((i)) + sideBuffer].getStatus()==true){
                            downAble = false;    
                        }
                    }
                    //if all cells directly below are unfilled, the Tetromino is moved down
                    if(downAble){
                        inPlay.get(0).moveDown();
                    }
                }

                //This loop hard drops the Tetromino, meaning that the Tetromino is placed in its final position directly below where it is when the space bar is hit
                //32 is the integer key for the space bar
                while(downAble && StdDraw.isKeyPressed(32)){
                    //The body works the same as that of the if statement above, but is contained within a loop, meaning that the Tetromino will go as far down as it can
                    for(int i = 0; i<4; i++){
                        if(inPlay.get(0).getBlockY(i)<=2 || board[(int)inPlay.get(0).getBlockY(i)-2][(int)inPlay.get(0).getBlockX((i))+sideBuffer].getStatus()==true){
                            downAble = false;
                        }
                    }
                    if(downAble){
                        inPlay.get(0).moveDown();
                    }
                }

                //39 is the integer key code for the right arrow key
                //The body of this if statement moves the Tetromino to the right by calling the moveRight method so long as the right arrow key is pressed and doing so would not force the Tetromino beyond the confines of the board
                if(inPlay.get(0).getRightMost()<9 && StdDraw.isKeyPressed(39)){
                    inPlay.get(0).moveRight(board);
                }

                //37 is the integer key code for the left arrow key
                //The body of this if statement moves the Tetromino to the left by calling the moveLeft method so long as the left arrow key is pressed and doing so would not force the Tetromino beyond the confines of the board
                if(inPlay.get(0).getLeftMost()>-9 && StdDraw.isKeyPressed(37)){
                    inPlay.get(0).moveLeft(board);
                }

                //38 is the integer code for the up arrow key
                //calls the rotate method from the Tetromino class
                if(StdDraw.isKeyPressed(38)){
                    inPlay.get(0).rotate(board);
                }

                //hold, 67 is the integer code for the c key
                //the body runs so long as held is true, meaning that the Tetromino has not been rotated yet,
                //and the c key is pressed
                if(held && StdDraw.isKeyPressed(67)){
                    //the integer necessary to use a parameter to create a new instance of the Tetromino first in the inPlay array is stored in new hold
                    int newHold = queueNum.get(0); 
                    
                    //if the hold area is not empty, meaning something has been held already:
                    if(holdNum!=-1){
                        //At index one, an instance of the Tetromino already in the hold position is added to the inPlay ArrayList 
                        inPlay.add(1, nextPiece(holdNum));
                        //the integer used used as the parameter for the nextPiece method is added at index one to the queueNum ArrayList
                        queueNum.add(1, holdNum);
                    }
                    
                    //if no piece has been held yet:
                    else{
                        //Since there is nothing in the hold, a random new Tetromino must be added to the inPlay Arraylist so that there will still be three Tetrominos in the queue
                        int num = (int)(Math.random()*numPieces);
                        inPlay.add(nextPiece(num));
                        queueNum.add(num);
                    }
                    //The in play Tetromino is removed from inPlay and queueNum
                    //holdNum is given the value of the corresponding integer to the removed Tetromino that was added to hold
                    inPlay.remove(0);
                    queueNum.remove(0);
                    holdNum=newHold;
                    //held is set to false so that the newly in play Tetromino cannot be held and infinite holds cannot happen
                    held=false;
                }
                
                //if a Tetromino has been held, the hold method will be called on an instance of it and it will be drawn in the hold position
                if(holdNum>-1){
                    Tetromino_EL holdIt = nextPiece(holdNum);
                    holdIt.hold();
                }

                //Draws updated position of the moving piece
                inPlay.get(0).drawPiece();
                StdDraw.show();
                StdDraw.pause(pause);

                //checks to see if the piece has made contact with either the bottom of the board or any element of the board array whose boolean instance field is true, meaning that the cell is filled
                //if this is the case, live is set to false, and the 
                for(int i = 0; i<numBlocks; i++){
                    if(inPlay.get(0).getBlockY(i)<=2 || board[(int)inPlay.get(0).getBlockY(i)-2][(int)inPlay.get(0).getBlockX((i))+sideBuffer].getStatus()==true){
                      live=false;  
                    }
                }

                //Pauses or unpauses the game by changing the value of move
                //80 is the integer code for the p key
                if(StdDraw.isKeyPressed(80)){
                    move=!move;
                }
            }
            
            //updates the board array so that the cells where the Tetromino came to rest are updated to reflect that they are filled and "colors in" the cell with the color of the Tetromino
            for(int i = 0; i<numBlocks; i++){
                int x = (int)inPlay.get(0).getBlockY(i);
                int y = sideBuffer+(int)inPlay.get(0).getBlockX((i));
                board[x][y].setTrue(inPlay.get(0).getR(), inPlay.get(0).getG(), inPlay.get(0).getB());
            }
            
            //draws the the contents of the board array
            for(int i = 1; i<boardHeight - upperBuffer; i+=2){
                for (int j = sideBuffer/2; j<boardWidth + 1; j+=2){
                    board[i][j].draw();
                }
            }
            
            //if any any piece came to rest above the top of the board, meaning the player stacked the Tetrominos above the limit of the board, play is set to false so that the game ends
            for(int i = sideBuffer/2; i<boardWidth + 1; i+=2){
                if(board[boardHeight - upperBuffer - 1][i].getStatus()==true){
                    play=false;
                }
            }

            //checks if any whole lines have been filled and clears them
            for(int i = 1; i<boardHeight - upperBuffer + 2; i+=2){
                boolean clear = true;
                //if any cells are unfilled in the line (really a row) then clear is set to false and the line is not cleared
                for (int j = sideBuffer/2; j<boardWidth; j+=2){
                    if(board[i][j].getStatus()==false){
                        clear=false;
                    }
                }
                
                //if every cell is filled in the line, meaning clear was not set to false in the above for loop, then the line is cleared and the above lines are moved down to fill in the cleared line
                if(clear){
                    //clears the line
                    for (int j = sideBuffer/2; j<boardWidth + 1; j+=2){
                        board[i][j].setFalse();
                    }
                    //moves the above rows down
                    for (int j = i; j<boardHeight - upperBuffer + 2; j+=2){
                        for (int k = sideBuffer/2; k<boardWidth; k+=2){
                            board[j][k].setEqual(board[j+2][k]);
                            //clears the line once its information has been moved down
                            board[j+2][k].setFalse();
                        }                        
                    }
                    //total lines cleared is updated
                    linesCleared++;
                    //the number of lines cleared by placing this Tetromino is updated
                    thisClear++;
                    //the variable in the for loop is decreased by two because the above lines have all moved down, and so the line just tested must be tested again because it contains new information
                    i-=2;
                }
            }
            //depeding on how many lines were cleared with that Tetromino, different scores are added to the player's total
            //the more lines they clear with one Tetromino, the more points they get in a nonlinear fashion
            int scoreAdd=0;
            if(thisClear==1){
                scoreAdd=100;
            }
            if(thisClear==2){
                scoreAdd=300;
            }
            if(thisClear==3){
                scoreAdd=500;
            }
            if(thisClear==4){
                scoreAdd=800;
            }
            
            //the points to be added to the player's points is multiplied by the level the player has reached
            //the higher the level, the more points the player receives
            scoreAdd*=level;
            
            //adds the player's points from this placement to their score
            points+=scoreAdd;
            
            //removes the Tetromino that was placed from inPlay and its corresponding integer fromm queueNum
            inPlay.remove(0);
            queueNum.remove(0);
            
            //A new random Tetromino is added to inPlay at the end of the ArrayList so that it will appear last in the queue
            //Its corresponding integer is added to queue Num
            int num = (int)(Math.random()*numPieces);
            inPlay.add(nextPiece(num));
            queueNum.add(num);
            
            //the Tetromino formerly at index 1 and first in the queue will now be put in play
        }
        
        //end of game message shows once the while loop is exited
        StdDraw.text(-15,30, "Game Over");
        StdDraw.show();
    }

    //draws the boundary of the game
    private static void drawBoundaries(int points, int linesCleared, int level){
        StdDraw.setPenRadius(0.01);
        //draws edges of the board
        StdDraw.line(-10,0,-10,40);
        StdDraw.line(10,0,10,40);
        StdDraw.line(-10,0,10,0);
        StdDraw.setPenRadius(0.002);
        StdDraw.setPenColor(StdDraw.GRAY);

        //draws the grid within the board
        for(int i = -10; i<=10; i+=2){
            StdDraw.line(i,0,i,40);
        }
        for(int i = 0; i<40; i+=2){
            StdDraw.line(-10,i,10,i);
        }

        //Displays all lables and stats for the player in different areas of the interface
        StdDraw.text(15,38, "Queue:");
        StdDraw.text(-15,38, "Hold:");
        StdDraw.text(-15,26, "Points:");
        StdDraw.text(-15,24, ""+points);
        StdDraw.text(-15,20, "Lines Cleared:");
        StdDraw.text(-15,18, ""+linesCleared);
        StdDraw.text(-15,16, "Level: "+level);
    }

    //returns a new instance of a Tetromino_EL child class to add to the ArrayList
    //takes an integer parameter to detrmine which new Tetromino_EL child object is returned
    private static Tetromino_EL nextPiece(int t){
        if(t==0)return new I_EL();
        if(t==1)return new O_EL();
        if(t==2)return new T_EL();
        if(t==3)return new S_EL();
        if(t==4)return new Z_EL();
        if(t==5)return new J_EL();
        return new L_EL();
    }
}