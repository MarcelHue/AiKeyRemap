package io.github.marcelhue.aikeyremap;

import android.app.Activity;
import android.os.Bundle;

/**
 * Windowless entry point that Motorola's key service starts for each AI key press.
 * The slot comes as string extra, e.g. {@code intent:#Intent;component=...;S.slot=single;end}.
 */
public class TriggerActivity extends Activity {
    public static final String EXTRA_SLOT = "slot";

    // Runs in onCreate without ever showing a window: a translucent window flickered on every press and
    // never got focus while the screen was off or locked, so the action did not run there.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slot slot = Slot.fromId(this.getIntent().getStringExtra(EXTRA_SLOT));
        if (slot != null) {
            ActionStore store = new ActionStore(this);
            new ActionRunner(this).run(store.getAction(slot), store.getAppPackage(slot));
        }
        this.finish();
    }
}
