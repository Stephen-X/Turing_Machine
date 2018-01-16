import java.util.HashMap;
import java.util.Map;

/**
 * State definition of the turing machine.
 *
 * @author Stephen Xie &lt;[redacted]@andrew.cmu.edu&gt;
 */
class State {

    private int id;  // the state's id
    private Map<Character, Transition> transitions;

    State(int id) {
        this.id = id;
        transitions = new HashMap<>();
    }

    /**
     * Add transition to this state.
     *
     * @param newTrans new transitions
     */
    void addTransition(Transition newTrans) {
        this.transitions.put(newTrans.getOldSymbol(), newTrans);
    }

    /**
     * Get a transition stored in this state.
     *
     * @param oldSymbol symbol that triggers the transition
     * @return the required transition, or null if not found
     */
    Transition getTransition(char oldSymbol) {
        return transitions.get(oldSymbol);
    }

    // --- getters -----------------------------

    public int getId() {
        return id;
    }
}
