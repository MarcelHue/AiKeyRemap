package io.github.marcelhue.aikeyremap;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.Toast;

/** Carries out a {@link KeyAction} on behalf of the foreground trigger activity. */
final class ActionRunner {
    private final Activity activity;

    ActionRunner(Activity activity) {
        this.activity = activity;
    }

    void run(KeyAction action, String appPackage) {
        switch (action) {
            case PLAY_PAUSE -> this.sendMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            case NEXT_TRACK -> this.sendMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT);
            case PREVIOUS_TRACK -> this.sendMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            case ASSISTANT -> this.showAssistant();
            case LAUNCH_APP -> this.launchApp(appPackage);
            case NONE -> {
            }
        }
    }

    // The system routes the key to the active media session, like a headset button. Real timestamps matter:
    // with downTime 0 the media key handler mistook separate presses for one multi-tap and dropped some.
    private void sendMediaKey(int keyCode) {
        AudioManager audioManager = this.activity.getSystemService(AudioManager.class);
        long now = SystemClock.uptimeMillis();
        audioManager.dispatchMediaKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0));
        audioManager.dispatchMediaKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0));
    }

    // Starting the assistant app's activity directly only opens its main screen (seen with Claude);
    // showAssist() opens a real assistant session, the same as the system gesture.
    private void showAssistant() {
        boolean isShown = this.activity.showAssist(null);
        if (!isShown) {
            this.showError(R.string.error_assistant);
        }
    }

    private void launchApp(String appPackage) {
        if (appPackage == null) {
            this.showError(R.string.error_no_app);
            return;
        }
        Intent launchIntent = this.activity.getPackageManager().getLaunchIntentForPackage(appPackage);
        if (launchIntent == null) {
            this.showError(R.string.error_app_missing);
            return;
        }
        this.activity.startActivity(launchIntent);
    }

    private void showError(int messageResId) {
        Toast.makeText(this.activity, messageResId, Toast.LENGTH_LONG).show();
    }
}
