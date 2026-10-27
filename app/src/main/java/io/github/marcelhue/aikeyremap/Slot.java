package io.github.marcelhue.aikeyremap;

/** One way of pressing the AI key; Motorola's key service reports each one separately. */
public enum Slot {
    SINGLE("single", R.string.slot_single, KeyAction.PLAY_PAUSE),
    DOUBLE("double", R.string.slot_double, KeyAction.NONE),
    HOLD("hold", R.string.slot_hold, KeyAction.ASSISTANT);

    private final String id;
    private final int labelResId;
    private final KeyAction defaultAction;

    Slot(String id, int labelResId, KeyAction defaultAction) {
        this.id = id;
        this.labelResId = labelResId;
        this.defaultAction = defaultAction;
    }

    /** The value used in the trigger intent and as preference key. */
    public String getId() {
        return this.id;
    }

    public int getLabelResId() {
        return this.labelResId;
    }

    public KeyAction getDefaultAction() {
        return this.defaultAction;
    }

    /** Returns the slot with the given id, or null for an unknown id. */
    public static Slot fromId(String id) {
        for (Slot slot : values()) {
            if (slot.id.equals(id)) {
                return slot;
            }
        }
        return null;
    }
}
