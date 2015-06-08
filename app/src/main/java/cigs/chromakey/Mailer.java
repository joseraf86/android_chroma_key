package cigs.chromakey;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.widget.Toast;

/**
 * Created by shadowfax on 6/8/15.
 */
public class Mailer {
    public static void composeEmail(String[] addresses,
                                    String subject,
                                    String text,
                                    Uri uri,
                                    Activity activity) {

        String body = "<h1><b>"+text+"</b></h1>";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body+"gracias por preferirnos"));
        // Colocamos el adjunto en el stream
        intent.putExtra(Intent.EXTRA_STREAM,
                        uri);

        // Indicamos el MIME type
        // intent.setType("image/jpeg");

        try {
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(activity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

}
