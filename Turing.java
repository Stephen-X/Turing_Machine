/**
 * A simple turing machine simulation.
 *
 * @author Stephen Xie &lt;[redacted]@andrew.cmu.edu&gt;
 */
class Turing {

    private static final int TAPE_LEN = 100;
    private static final char BLANK = 'B';

    private char[] tape;  // output tape
    private int  tapePointer;  // pointer for output tape
    private State[] states;  // a list of states ordered by id (found via array index)
    private int stateCount;  // total number of states in this machine

    Turing(int state) {
        if (state < 2) {
            throw new IllegalArgumentException("Must have at least 2 states.");
        }
        // initialize the machine's states
        // note that the last state will always be the halting state as per definition
        this.states = new State[state - 1];
        this.stateCount = 1;
    }


    /**
     * Read in a tape and execute the machine according to the defined states.
     *
     * @param inTape String representing the input tape
     * @return String representing the output tape
     */
    String execute(String inTape) {
        // initialize in-memory tape and current state
        initTape(inTape);
        int currState = 0;  // id of current state this machine is in

        Transition transition;
        // while we haven't reached the halting state
        while (currState < stateCount - 1) {
            transition = states[currState].getTransition(tape[tapePointer]);
            if (transition != null) {
                int newPointerPos = tapePointer + transition.getMovement();
                if (newPointerPos < 0 || newPointerPos > tape.length) {
                    throw new ArrayIndexOutOfBoundsException("Cannot apply transition: tape pointer moves out of range" +
                            "after this transition.");
                }

                tape[tapePointer] = transition.getNewSymbol();
                // TODO: for debugging
//                System.out.println("State " + currState + ", TapePointer " + tapePointer + ": " + String.valueOf(tape));
                tapePointer = newPointerPos;
                currState = transition.getNextState();
            } else {
                throw new IllegalArgumentException("Transition not found for this input: " + tape[tapePointer] + ".");
            }
        }

        return String.valueOf(tape);
    }

    /**
     * Add a new state to the machine.
     *
     * @param newState new state to be added
     */
    void addState(State newState) {
        if (stateCount <= states.length) {
            states[stateCount++ - 1] = newState;
        } else {
            throw new ArrayIndexOutOfBoundsException("Exceeded maximum amount of states allowed; " +
                    "note that the last state of the machine is always reserved for the halting state.");
        }
    }

    /**
     * Initialize the tape in this machine with the input tape's content.
     *
     * @param inTape input tape
     */
    private void initTape(String inTape) {
        if (inTape.length() >= TAPE_LEN) {
            // the last symbol must be a BLANK, otherwise the machine doesn't know where to stop reading
            throw new IllegalArgumentException("Input tape length exceeded maximum machine memory; " +
                    "note that the last symbol of the tape is always reserved for the BLANK symbol.");
        }
        if (tape == null) tape = new char[TAPE_LEN];
        tapePointer = 0;
        for (char c : inTape.toCharArray()) {
            tape[tapePointer++] = c;
        }
        // fill the remaining cells with blank symbols
        for (; tapePointer < TAPE_LEN; tapePointer++) {
            tape[tapePointer] = BLANK;
        }
        tapePointer = 0;
    }

}
