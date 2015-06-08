package cigs.chromakey;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.FileNotFoundException;

/**
 * Created by usuario on 5/06/15.
 */
public class Utils {


    public static Parcelable getParcelableFromBundle( Bundle extras ) {

        Bundle bundle = extras;
        Uri uri;
        Bitmap mBitmap = null;

        if(extras != null) {

            return bundle.getParcelable("res_image");

        }

        return null;
    }
}
