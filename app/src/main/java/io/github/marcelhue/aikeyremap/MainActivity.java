package io.github.marcelhue.aikeyremap;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/** Settings screen: one row per slot with the action and, for "open app", the chosen app. */
public class MainActivity extends Activity {
    private ActionStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.store = new ActionStore(this);

        LinearLayout slotList = this.findViewById(R.id.slot_list);
        LayoutInflater inflater = this.getLayoutInflater();
        for (Slot slot : Slot.values()) {
            View row = inflater.inflate(R.layout.slot_row, slotList, false);
            this.bindRow(row, slot);
            slotList.addView(row);
        }
    }

    private void bindRow(View row, Slot slot) {
        TextView title = row.findViewById(R.id.slot_title);
        Spinner actionSpinner = row.findViewById(R.id.action_spinner);
        View appRow = row.findViewById(R.id.app_row);
        TextView appName = row.findViewById(R.id.app_name);
        Button chooseApp = row.findViewById(R.id.choose_app);

        title.setText(slot.getLabelResId());
        actionSpinner.setAdapter(this.createActionAdapter());
        actionSpinner.setSelection(this.store.getAction(slot).ordinal());
        this.showAppRow(appRow, appName, slot);

        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.store.setAction(slot, KeyAction.values()[position]);
                MainActivity.this.showAppRow(appRow, appName, slot);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // A spinner with fixed entries always has a selection.
            }
        });

        chooseApp.setOnClickListener(button -> new AppPicker(this).show(packageName -> {
            this.store.setAppPackage(slot, packageName);
            this.showAppRow(appRow, appName, slot);
        }));
    }

    private ArrayAdapter<String> createActionAdapter() {
        KeyAction[] actions = KeyAction.values();
        String[] labels = new String[actions.length];
        for (int index = 0; index < actions.length; index++) {
            labels[index] = this.getString(actions[index].getLabelResId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void showAppRow(View appRow, TextView appName, Slot slot) {
        boolean isLaunchApp = this.store.getAction(slot) == KeyAction.LAUNCH_APP;
        appRow.setVisibility(isLaunchApp ? View.VISIBLE : View.GONE);
        appName.setText(this.getAppLabel(this.store.getAppPackage(slot)));
    }

    private CharSequence getAppLabel(String packageName) {
        if (packageName == null) {
            return this.getString(R.string.no_app_chosen);
        }
        PackageManager packageManager = this.getPackageManager();
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(info);
        }
        catch (PackageManager.NameNotFoundException uninstalled) {
            return this.getString(R.string.error_app_missing);
        }
    }
}
