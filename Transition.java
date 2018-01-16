/**
 * Transition definition of the turing machine.
 *
 * @author Stephen Xie &lt;[redacted]@andrew.cmu.edu&gt;
 */
class Transition {

    public static final int RIGHT = 1;
    public static final int LEFT = -1;

    // definitions of this transition
    private char oldSymbol;
    private char newSymbol;
    private int movement;  // direction and distance the tape pointer in the machine should move
    private int nextState;

    Transition(char oldSymbol, char newSymbol, int movement, int nextState) {
        this.oldSymbol = oldSymbol;
        this.newSymbol = newSymbol;
        this.movement = movement;
        this.nextState = nextState;
    }

    // --- Getters ------------------------

    public char getOldSymbol() {
        return oldSymbol;
    }

    public char getNewSymbol() {
        return newSymbol;
    }

    public int getMovement() {
        return movement;
    }

    public int getNextState() {
        return nextState;
    }
}
