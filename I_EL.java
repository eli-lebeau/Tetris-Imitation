
/**
 * The I_EL class defines the "I-block" tetromino as a child class of the parent Tetromino_EL class
 * The constructor provides all necessary parameters for the Tetromino_EL parent class
 * These parameters provide the shape of this Tetromino
 * No methods are added or overidden from the parent class
 *
 * @Elebau
 * @Java 8
 * @5/3/2023
 */
public class I_EL extends Tetromino_EL
{
    public I_EL(){
        super(-3,43,-1,43,1,43,3,43,0,255,255,2);
    }
}
