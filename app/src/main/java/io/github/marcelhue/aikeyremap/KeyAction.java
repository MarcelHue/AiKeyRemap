package io.github.marcelhue.aikeyremap;

/** What the app does when a slot is triggered. The enum names are stored, so do not rename them. */
public enum KeyAction {
    NONE(R.string.action_none),
    PLAY_PAUSE(R.string.action_play_pause),
    NEXT_TRACK(R.string.action_next_track),
    PREVIOUS_TRACK(R.string.action_previous_track),
    ASSISTANT(R.string.action_assistant),
    LAUNCH_APP(R.string.action_launch_app);

    private final int labelResId;

    KeyAction(int labelResId) {
        this.labelResId = labelResId;
    }

    public int getLabelResId() {
        return this.labelResId;
    }
}
