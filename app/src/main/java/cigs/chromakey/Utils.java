package cigs.chromakey;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by usuario on 5/06/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();

    public static int calculateInSampleSize( BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width  = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth  = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                //Log.i(TAG, "height: "+halfHeight / inSampleSize);
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /** */
    public static Bitmap decodeSampledBitmapFromUri(Activity act, Uri uri,
                                                    int reqWidth, int reqHeight) {
        Bitmap tmp = null;
        InputStream in = null;
        final BitmapFactory.Options options;


        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            in = act.getContentResolver().openInputStream(uri);
            options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            in = act.getContentResolver().openInputStream(uri);
            tmp = BitmapFactory.decodeStream(in, null, options);

            in.close();

        }
        catch(IOException e){
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        return tmp;
    }



    public static Bitmap decodeBitmapFromUri(Activity act, Uri uri) {
        Bitmap tmp = null;
        InputStream in = null;
        final BitmapFactory.Options options;

        try {
            in = act.getContentResolver().openInputStream(uri);
            tmp = BitmapFactory.decodeStream(in);

            in.close();

        }
        catch(IOException e){
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        return tmp;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
