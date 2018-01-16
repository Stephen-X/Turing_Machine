/**
 * Main driver of the turing machine simulation for task 3:
 * Given &Sigma; = {0, 1}, design a Turing Machine that decides language
 * L = { 0^n * 1^n | n &isin; N }.
 * <p>
 * This Turing machine will read the input and decide the string.
 * It will leave the result (1 = 'yes' or 0 = 'no') on the output tape.
 *
 * @author Stephen Xie &lt;[redacted]@andrew.cmu.edu&gt;
 */
public class Main {
    /**
     * Basic idea: recursively eliminates a 0 and a 1 (replace them with Blank) from
     * both ends of the string respectively, until there's nothing left (accepting
     * state) or cannot continue (reject state).
     * <p>
     * <b>Transition Function Definition:</b> (let q4 be the accepting state, and q5 be
     * the rejecting state)
     * <ol>
     *     <li>&delta;(q0, 0) = (q1, B, R)</li>
     *     <li>&delta;(q0, 1) = (q5, B, R)</li>
     *     <li>&delta;(q0, B) = (q4, B, R)</li>
     *     <br>
     *     <li>&delta;(q1, 0) = (q1, 0, R)</li>
     *     <li>&delta;(q1, 1) = (q1, 1, R)</li>
     *     <li>&delta;(q1, B) = (q2, B, L)</li>
     *     <br>
     *     <li>&delta;(q2, 0) = (q5, 0, L)</li>
     *     <li>&delta;(q2, 1) = (q3, B, L)</li>
     *     <li>&delta;(q2, B) = (q5, B, L)</li>
     *     <br>
     *     <li>&delta;(q3, 0) = (q3, 0, L)</li>
     *     <li>&delta;(q3, 1) = (q3, 1, L)</li>
     *     <li>&delta;(q3, B) = (q0, B, R)</li>
     * </ol>
     * <p>
     * Note: as the project requires that we output the result as 0 / 1 at the front of an empty
     * tape, the actual implementation will not be kept exactly in sync with the mathematical
     * definition defined above.
     */
    public static void main(String[] args) {
        Turing machine = new Turing(9);

        // define states
        State s0 = new State(0);  // this state is created to skip the `E` header at start
        State s1 = new State(1);
        State s2 = new State(2);
        State s3 = new State(3);
        State s4 = new State(4);
        State s5 = new State(5);  // this is the accepting state
        State s6 = new State(6);  // this is the rejecting state
        State s7 = new State(7);  // this is the "wiping" state that helps output formatting

        // --- and the transitions --------------------------------------------------------------

        // we will always encounter the starting symbol `E` first at the beginning;
        // simply move pass it
        s0.addTransition(new Transition('E', 'E', Transition.RIGHT, 1));

        // string starts with 0; wipe it then start to move to the string end;
        s1.addTransition(new Transition('0', 'B', Transition.RIGHT, 2));
        // string starts with 1; reject
        s1.addTransition(new Transition('1', 'B', Transition.LEFT, 6));
        // no symbol left; accept
        s1.addTransition(new Transition('B', 'B', Transition.LEFT, 5));
        // Handles a special accept case: original string is empty. We should reject this since n
        // should be >= 1 from the language definition
        s1.addTransition(new Transition('E', 'E', Transition.LEFT, 6));

        // until we pass the end of the string, keep moving right; after we reached the first blank
        // or the ending indicator, move back 1 position to reach the ending symbol
        s2.addTransition(new Transition('0', '0', Transition.RIGHT, 2));
        s2.addTransition(new Transition('1', '1', Transition.RIGHT, 2));
        s2.addTransition(new Transition('B', 'B', Transition.LEFT, 3));
        s2.addTransition(new Transition('E', 'E', Transition.LEFT, 3));

        // string ends with 0; reject
        s3.addTransition(new Transition('0', '0', Transition.LEFT, 6));
        // string ends with 1; wipe it then start to move to the string front
        s3.addTransition(new Transition('1', 'B', Transition.LEFT, 4));
        // no symbol left: this is not symmetric; reject
        s3.addTransition(new Transition('B', 'B', Transition.LEFT, 6));
        s3.addTransition(new Transition('E', 'E', Transition.LEFT, 6));

        // until we pass the start of the string, keep moving left; after we reached the first blank
        // or the starting indicator, move back 1 position to reach the starting symbol
        s4.addTransition(new Transition('0', '0', Transition.LEFT, 4));
        s4.addTransition(new Transition('1', '1', Transition.LEFT, 4));
        // now we're at string start again; turn back to state 1
        s4.addTransition(new Transition('B', 'B', Transition.RIGHT, 1));
        s4.addTransition(new Transition('E', 'E', Transition.RIGHT, 1));

        // in accepting state, we go all the way back to the start of the string marked 'E', and change
        // it to '1'; then we wipe everything to 'B' until we move pass the other `E` in state 7
        s5.addTransition(new Transition('0', '0', Transition.LEFT, 5));
        s5.addTransition(new Transition('1', '1', Transition.LEFT, 5));
        s5.addTransition(new Transition('B', 'B', Transition.LEFT, 5));
        // reached the start; marked with `1`, then move on to state 7
        s5.addTransition(new Transition('E', '1', Transition.RIGHT, 7));

        // in rejecting state, we go all the way back to the start of the string marked 'E', and change
        // it to '0'; then we wipe everything to 'B' until we move pass the other `E` in state 7
        s6.addTransition(new Transition('0', '0', Transition.LEFT, 6));
        s6.addTransition(new Transition('1', '1', Transition.LEFT, 6));
        s6.addTransition(new Transition('B', 'B', Transition.LEFT, 6));
        // reached the start; marked with `0`, then move on to state 7
        s6.addTransition(new Transition('E', '0', Transition.RIGHT, 7));

        // now that the accepting / rejecting state is marked, wipe everything until we move pass
        // the `E` that marks the end of the original string
        s7.addTransition(new Transition('0', 'B', Transition.RIGHT, 7));
        s7.addTransition(new Transition('1', 'B', Transition.RIGHT, 7));
        s7.addTransition(new Transition('B', 'B', Transition.RIGHT, 7));
        // found that `E`; change it and move on to halting state
        s7.addTransition(new Transition('E', 'B', Transition.RIGHT, 8));

        // ------------------------------------------------------------

        // add the states to the machine
        machine.addState(s0);
        machine.addState(s1);
        machine.addState(s2);
        machine.addState(s3);
        machine.addState(s4);
        machine.addState(s5);
        machine.addState(s6);
        machine.addState(s7);

        // feed data to the machine and execute it
        String inTape = "0000011";
//        String inTape = "01";
//        String inTape = "10";
//        String inTape = "00000000001111111111";
//        String inTape = "";  // this should be rejected according to language definition
//        String inTape = "0";
//        String inTape = "1";
//        String inTape = "0002011111";  // exception
//        String inTape = "000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111";
//        String inTape = "0000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111";
//        String inTape = "0000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111";
        // the following should throw an exception as it exceeds memory restriction;
        // note that the actual string is wrapped by "E%sEB"
//        String inTape = "00000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111111";
        // -- mixing BLANKs in the string may trigger undefined issues; this is not handled ----
//        String inTape = "000B011111";
//        String inTape = "000001B1111";
        // -------------------------------------------------------------------------------------
        System.out.println(inTape);
        // For simplicity, here I preprocessed the input tape by marking its two ends with a special symbol 'E';
        // it will be useful for printing the final result as per requirement.
        inTape = String.format("E%sE", inTape);
        String outTape = machine.execute(inTape);
        System.out.println(outTape);

    }
}
