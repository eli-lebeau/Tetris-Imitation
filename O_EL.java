
/**
 * The O_EL class defines the "O-block" tetromino as a child class of the parent Tetromino_EL class
 * The constructor provides all necessary parameters for the Tetromino_EL parent class
 * These parameters provide the shape of this Tetromino
 * 
 * The rotate(Cell_EL[][] cells) method is overriden in this class over the rotate(Cell_EL[][] cells) method in the parent class
 * It is overriden and left without a body to make sure that the O-block does not rotate since it does not rotate in Tetris
 * All other methods from the parent class are not overidden and no other methods are added
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public class O_EL extends Tetromino_EL
{
    public O_EL(){
        super(-1,43,-1,41,1,41,1,43,240,240,0,0);
    }

    // Method overrides the rotate method in the parent class so that the O block does not rotate
    public void rotate(Cell_EL[][] cells){
        
    }
}
