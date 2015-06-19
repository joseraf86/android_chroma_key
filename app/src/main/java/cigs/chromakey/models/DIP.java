package cigs.chromakey.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


/**
 * DIP Digital Image Processing
 * Process a Bitmap (bpm) picture to subtract its background and
 * replace it with other picture
 */
public class DIP {
    private static final String TAG = DIP.class.getName();

    // Foreground fg and Background bg picture
    public Bitmap fg, bg;

    // Tolerance [tola; tolb]
    int tola, tolb;

    // Chroma Key ( Green or Blue color)
    int chroma_key;

    /***/
    public DIP (int tola, int tolb, int chroma_key){
        this.tola = tola;
        this.tolb = tolb;
        this.chroma_key = chroma_key;
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public static int rgb2cb (int r, int g, int b)
    {
        int cb;
        cb = (int) Math.round(128 + -0.168736 * r - 0.331264 * g + 0.5 * b);
        return cb;
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public static int rgb2cr (int r, int g, int b)
    {
        int cr;
        cr = (int) Math.round(128 + 0.5 * r - 0.418688 * g - 0.081312 * b);
        return cr;
    }

    /** Decides if a color is close to the specified hue */
    private double colorClose(  int cb_p,       int cr_p,
                                int cb_key,     int cr_key   )
    {
        double distance = Math.sqrt((cb_key - cb_p) * (cb_key - cb_p) + (cr_key - cr_p) * (cr_key - cr_p));

        if (distance < tola) {return (0.0);}
        if (distance < tolb) {return ((distance-tola)/(tolb-tola));}
        return (1.0);
    }

    /** Chroma Key Algorithm
     *  Los bitmaps deben ser mutables para poder realizar el procesado */
    public void chromaKey( Bitmap fg, Bitmap bg ) {
        double mask;
        int cb_key, cr_key;
        //int pxout_red, pxout_green, pxout_blue;

        this.fg = fg;
        this.bg = bg;

        // Get cb and cr of key color
        cb_key = rgb2cb( Color.red(chroma_key),
                         Color.green(chroma_key),
                         Color.blue(chroma_key));
        cr_key = rgb2cr(Color.red(chroma_key),
                        Color.green(chroma_key),
                        Color.blue(chroma_key));
        //Log.i("DIP:: cbk, crk :", ""+cb_key+", "+cr_key);
        //Log.i(TAG, "alturas "+ fg.getHeight()+ " "+ bg.getHeight());
        //Log.i(TAG, "anchos "+ fg.getWidth()+ " "+ bg.getWidth());

        // for each i,j get Cb and Cr for pixel value
        for(int i=0; i < fg.getWidth(); i++) {
            for(int j=0; j < fg.getHeight(); j++) {
                // Get cb and cr of each pixel
                int cb_p = rgb2cb(Color.red(fg.getPixel(i,j)),
                                  Color.green(fg.getPixel(i,j)),
                                  Color.blue(fg.getPixel(i,j)));

                int cr_p = rgb2cr(Color.red(fg.getPixel(i,j)),
                                  Color.green(fg.getPixel(i,j)),
                                  Color.blue(fg.getPixel(i,j)));

                mask = colorClose(cb_p, cr_p, cb_key, cr_key);
                mask = 1.0 - mask;

                if (mask == 1) {
                    try {
                        fg.setPixel(i, j, bg.getPixel(i, j));
                    }
                    catch(IllegalArgumentException e){
                        Log.i(TAG, "i "+i+ " j"+j);
                        return;
                    }
                }//else{
                    //if (mask == 0) {

                    //}else{
                        /*fg.setPixel(i, j, fg.getPixel(i,j) );
                        pxout_red    = (int) ( Color.red(fg.getPixel(i,j))
                                               - mask*Color.red(chroma_key)
                                               + mask*Color.red(bg.getPixel(i,j))
                        );
                        pxout_green  = (int) ( Color.green(fg.getPixel(i,j))
                                - mask*Color.green(chroma_key)
                                + mask*Color.green(bg.getPixel(i,j))
                        );
                        pxout_blue   = (int) ( Color.blue(fg.getPixel(i,j))
                                - mask*Color.blue(chroma_key)
                                + mask*Color.blue(bg.getPixel(i,j))
                        );
                        /*
                        pxout_red    = (int) ( Math.max( Color.red(fg.getPixel(i,j)) - mask*Color.red(chroma_key), 0)

                                + Color.red(bg.getPixel(i,j))*mask );
                        pxout_green  = (int) ( Math.max( Color.green(fg.getPixel(i,j)) - mask*Color.green(chroma_key), 0)
                                + Color.green(bg.getPixel(i,j))*mask );
                        pxout_blue   = (int) ( Math.max( Color.blue(fg.getPixel(i,j)) - mask*Color.blue(chroma_key), 0)
                                + Color.blue(bg.getPixel(i,j))*mask );
                        */
                        //fg.setPixel(i, j, Color.rgb(pxout_red, pxout_green, pxout_blue) ); // final_pixel
                    //}
                //}

                // final_pixel = (int) Math.round( fg.getPixel(i,j) - mask*chroma_key + bg.getPixel(i,j)*mask );

                //Log.i("DIP: px res (i,j): ", "("+i+","+j+""+final_pixel);
            }

            //Log.i("DIP:: distances :", i+""+colorcloses);
        }


    }

}
