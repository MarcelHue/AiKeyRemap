package io.github.marcelhue.aikeyremap;

import android.content.Context;
import android.content.SharedPreferences;

/** Saves the chosen action, and the app for "open app", per slot. */
public final class ActionStore {
    private static final String PREFERENCES_NAME = "slot_actions";
    private static final String ACTION_SUFFIX = ".action";
    private static final String APP_SUFFIX = ".app";

    private final SharedPreferences preferences;

    public ActionStore(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public KeyAction getAction(Slot slot) {
        String stored = this.preferences.getString(slot.getId() + ACTION_SUFFIX, null);
        if (stored == null) {
            return slot.getDefaultAction();
        }
        try {
            return KeyAction.valueOf(stored);
        }
        catch (IllegalArgumentException unknownAction) {
            // Stored by a newer or older version that had a different action list.
            return slot.getDefaultAction();
        }
    }

    public void setAction(Slot slot, KeyAction action) {
        this.preferences.edit().putString(slot.getId() + ACTION_SUFFIX, action.name()).apply();
    }

    /** Package name of the app to open, or null if none was chosen. */
    public String getAppPackage(Slot slot) {
        return this.preferences.getString(slot.getId() + APP_SUFFIX, null);
    }

    public void setAppPackage(Slot slot, String packageName) {
        this.preferences.edit().putString(slot.getId() + APP_SUFFIX, packageName).apply();
    }
}
