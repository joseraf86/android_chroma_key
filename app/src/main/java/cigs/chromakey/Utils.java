package cigs.chromakey;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by usuario on 5/06/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /** */
    public static Bitmap decodeSampledBitmapFromUri(ContentResolver contentResolver, Uri uri,
                                                         int reqWidth, int reqHeight) {
        InputStream in = null;
        final BitmapFactory.Options options;

        // First decode with inJustDecodeBounds=true to check dimensions
        try {
            in = contentResolver.openInputStream(uri);
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(res, resId, options);
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        }
        catch(IOException e){
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeStream(in, null, options);
    }



}
