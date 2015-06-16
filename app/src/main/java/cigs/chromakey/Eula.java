package cigs.chromakey;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Displays an EULA ("End User License Agreement") that the user has to accept.
 */
class Eula {
    private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";
    private static final String PREFERENCES_EULA = "eula";

    /**
     * Displays the EULA if necessary. This method should be called from the onCreate()
     * method of your main Activity.
     *
     * @param activity The Activity to finish if the user rejects the EULA.
     */
    static void showEula(final Activity activity, final String[] emails, final Uri imgUri) {

        //final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_EULA,
        //        Activity.MODE_PRIVATE);
        //if (!preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.eula_title);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.txt_accept, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
            //        accept(preferences);
                    ((MailActivity)activity).accept();
                }
            });
            builder.setNegativeButton(R.string.txt_refuse, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    refuse(activity);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    refuse(activity);
                }
            });
            // UNCOMMENT TO ENABLE EULA
            //builder.setMessage(readFile(activity, R.raw.eula));
            builder.create().show();
        //}//
    }

    private static void accept(SharedPreferences preferences) {
        preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
    }

    private static void refuse(Activity activity) {
        activity.finish();
    }

    static void showDisclaimer(Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setMessage(readFile(activity, R.raw.disclaimer));
        builder.setCancelable(true);
        builder.setTitle(R.string.disclaimer_title);
        builder.setPositiveButton(R.string.txt_accept, null);
        builder.create().show();
    }

    private static CharSequence readFile(Activity activity, int id) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(activity.getResources().openRawResource(id)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) buffer.append(line).append('\n');
            return buffer;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
