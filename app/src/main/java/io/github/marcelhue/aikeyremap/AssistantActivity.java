package io.github.marcelhue.aikeyremap;

import android.app.Activity;
import android.widget.Toast;

/**
 * Transparent activity that opens the default assistant. Starting the assistant app's activity directly
 * only opens its main screen (seen with Claude); showAssist() opens a real assistant session instead.
 */
public class AssistantActivity extends Activity {
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
        boolean isShown = this.showAssist(null);
        if (!isShown) {
            Toast.makeText(this, R.string.error_assistant, Toast.LENGTH_LONG).show();
        }
        this.finish();
    }
}
