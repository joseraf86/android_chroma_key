package cigs.chromakey;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.widget.Toast;

/**
 * Created by shadowfax on 6/10/15.
 */
public class Sharer {

    public static void shareText(String text,
                                    Activity activity) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        try {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(activity, "There is no social app client installed.", Toast.LENGTH_SHORT).show();
        }

    }

}
