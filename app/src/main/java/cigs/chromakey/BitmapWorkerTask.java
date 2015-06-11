package cigs.chromakey;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * Created by shadowfax on 6/11/15.
 */
class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    //private final WeakReference<ImageView> imageViewReference;
    private int data = 0;
    private DIP dip;
    private Bitmap fg_img;
    private Bitmap bg_img;

    public BitmapWorkerTask(Bitmap fg_img, Bitmap bg_img) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        //imageViewReference = new WeakReference<ImageView>(imageView);
        dip = new DIP(60, 62, Color.rgb(17, 168, 75));
        this.fg_img = fg_img;
        this.bg_img = bg_img;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        // data = params[0];
        // return decodeSampledBitmapFromResource(getResources(), data, 100, 100));
        dip.chromaKey(fg_img, bg_img);
        return dip.fg;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    // @Override
    /*
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }*/
}
