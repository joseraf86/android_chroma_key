package cigs.chromakey;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


/**
 * Created by jrgs on 5/06/15.
 * DIP Digital Image Processing
 * Process a Bitmap (bpm) picture to subtract its background and
 * replace it with other picture
 */
public class DIP {
    // Foreground fg and Background bg picture
    Bitmap fg, bg;

    // Tolerance [tola; tolb]
    int tola;
    int tolb;

    // Chroma Key ( Green or Blue color)
    int chroma_key;

    /***/
    public DIP (int tola, int tolb, int chroma_key){
        this.tola = tola;
        this.tolb = tolb;
        this.chroma_key = chroma_key;
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public static int rgb2y (int r, int g, int b)
    {
        int y;
        y = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
        return y;
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

    /** Chroma Key Algorithm */
    public void chromaKey( Bitmap fg, Bitmap bg ) {
        double mask;
        int final_pixel;
        int cb_key, cr_key;

        this.fg = fg;
        this.bg = bg;

        // Get cb and cr of key color
        cb_key = rgb2cb( Color.red(chroma_key),
                         Color.green(chroma_key),
                         Color.blue(chroma_key));
        cr_key = rgb2cr(Color.red(chroma_key),
                        Color.green(chroma_key),
                        Color.blue(chroma_key));
        Log.i("DIP:: cbk, crk :", ""+cb_key+", "+cr_key);
        //Log.i("DIP:: fg.getWidth()", ""+fg.getWidth());
//
        String colorcloses = "";
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
                //Log.i("DIP:: c/ i,j cb, cr: ", "("+i+","+j+")"+cb_p+", "+cr_p);

                mask = colorClose(cb_p, cr_p, cb_key, cr_key);
                //if ( i==20 && j == 400) Log.i("DIP:: color :", ""+fg.getPixel(i,j));
                //colorcloses += mask + ", ";
                mask = 1.0 - mask;

                int pxout_red, pxout_green, pxout_blue;

                if (mask == 1) {
                    fg.setPixel(i, j, bg.getPixel(i,j) );
                }else{
                    if (mask == 0) {

                    }else{
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
                    }
                }

                // final_pixel = (int) Math.round( fg.getPixel(i,j) - mask*chroma_key + bg.getPixel(i,j)*mask );

                //Log.i("DIP: px res (i,j): ", "("+i+","+j+""+final_pixel);
            }

            //Log.i("DIP:: distances :", i+""+colorcloses);
        }

        Log.i("DIP: chromakey ", "DONE!!!!!!");
        /*
        int pos, mx;
        int r,g,b, r_bg, g_bg, b_bg;
        int cb, cr, cb_key, cr_key, tola, tolb, r_key, g_key, b_key;
        double mask;

        Bitmap bg, fg;

        pos = fg.datapos;
        mx = 3 * fg.getWidth();
        switch (mx%4)
        {
            case 1:
                mx = mx+3;
                break;
            case 2:
                mx = mx+2;
                break;
            case 3:
                mx = mx+1;
                break;
        }
        int i, j;
        for (i=0; i  ;)  {
            //fseek(fg.file, pos, SEEK_SET);
            //fseek(out.file, pos, SEEK_SET);
            //fseek(bg.file, pos, SEEK_SET);
            for (j=pos; j ; )     {
                //b = getc(fg.file);
                //g = getc(fg.file);
                //r = getc(fg.file);
                //b_bg = getc(bg.file);
                //g_bg = getc(bg.file);
                //r_bg = getc(bg.file);
                cb = rgb2cb(r,g,b);
                cr = rgb2cr(r,g,b);
                mask = colorclose(cb, cr, cb_key, cr_key, tola, tolb);
                mask = 1 - mask;
                r = Math.max(r - mask*r_key, 0) + mask*r_bg;
                g = Math.max(g - mask*g_key, 0) + mask*g_bg;
                b = Math.max(b - mask*b_key, 0) + mask*b_bg;
                //fputc(b, out.file);
                //fputc(g, out.file);
                //fputc(r, out.file);
            }
            pos = mx+pos;
        }
        */
    }

}
