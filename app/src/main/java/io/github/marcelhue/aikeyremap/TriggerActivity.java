package io.github.marcelhue.aikeyremap;

import android.app.Activity;

/**
 * Invisible entry point that Motorola's key service starts for each AI key press.
 * The slot comes as string extra, e.g. {@code intent:#Intent;component=...;S.slot=single;end}.
 */
public class TriggerActivity extends Activity {
    public static final String EXTRA_SLOT = "slot";

    private boolean hasRun;

    // Waits for window focus: showAssist() fails while the caller is not visible yet, which is still the
    // case in onResume ("showAssistFromActivity failed: caller ... is not visible").
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus || this.hasRun) {
            return;
        }
        this.hasRun = true;
        this.runSlotAction();
        this.finish();
    }

    private void runSlotAction() {
        Slot slot = Slot.fromId(this.getIntent().getStringExtra(EXTRA_SLOT));
        if (slot == null) {
            return;
        }
        ActionStore store = new ActionStore(this);
        new ActionRunner(this).run(store.getAction(slot), store.getAppPackage(slot));
    }
}
