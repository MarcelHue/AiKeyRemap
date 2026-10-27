package io.github.marcelhue.aikeyremap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/** Dialog that lists all apps with a launcher entry, sorted by name. */
final class AppPicker {
    private final Context context;

    AppPicker(Context context) {
        this.context = context;
    }

    /** Shows the list and passes the package name of the picked app to {@code onPicked}. */
    void show(Consumer<String> onPicked) {
        PackageManager packageManager = this.context.getPackageManager();
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(launcherIntent, 0);
        apps.sort(Comparator.comparing(app -> app.loadLabel(packageManager).toString(),
                String.CASE_INSENSITIVE_ORDER));

        CharSequence[] labels = new CharSequence[apps.size()];
        for (int index = 0; index < apps.size(); index++) {
            labels[index] = apps.get(index).loadLabel(packageManager);
        }

        new AlertDialog.Builder(this.context)
                .setTitle(R.string.choose_app)
                .setItems(labels, (dialog, which) -> onPicked.accept(apps.get(which).activityInfo.packageName))
                .show();
    }
}
