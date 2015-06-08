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

    public DIP (Bitmap fg, Bitmap bg, int tola, int tolb, int chroma_key){
        this.fg = fg;
        this.bg = bg;
        this.tola = tola;
        this.tolb = tolb;
        this.chroma_key = chroma_key;
    }

    public int getPixel(int x, int y) {
        return fg.getPixel(x, y);
    }

    public void putPixel(int x, int y, int pixel){
        fg.setPixel(x, y, pixel);
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public int rgb2y (int r, int g, int b)
    {
        int y;
        y = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
        return y;
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public int rgb2cb (int r, int g, int b)
    {
        int cb;
        cb = (int) Math.round(128 + -0.168736 * r - 0.331264 * g + 0.5 * b);
        return cb;
    }

    /** A utility function to convert colors in RGB into YCbCr */
    public int rgb2cr (int r, int g, int b)
    {
        int cr;
        cr = (int) Math.round(128 + 0.5 * r - 0.418688 * g - 0.081312 * b);
        return cr;
    }

    /** Decides if a color is close to the specified hue */
    public double colorClose(int Cb_p,
                                    int Cr_p,
                                    int Cb_key,
                                    int Cr_key)
    {
        double temp = Math.sqrt((Cb_key - Cb_p) * (Cb_key - Cb_p) + (Cr_key - Cr_p) * (Cr_key - Cr_p));
        if (temp < tola) {return (0.0);}
        if (temp < tolb) {return ((temp-tola)/(tolb-tola));}
        return (1.0);
    }

    /* Chroma Key Algorithm */
    public void chromaKey() {

        // Get cb and cr of key color
        int cb_key = rgb2cb(Color.red(chroma_key),
                             Color.green(chroma_key),
                             Color.blue(chroma_key));
        Log.i("DIP: cbk: ", "("+")"+cb_key);
        int cr_key = rgb2cr(Color.red(chroma_key),
                Color.green(chroma_key),
                Color.blue(chroma_key));
        Log.i("DIP: crk: ", "("+")"+cr_key);
        double mask;
        int final_pixel;
        // for each i,j get Cb and Cr for pixel value
        for(int i=1; i< fg.getWidth(); i++) {
            for(int j=i; i< fg.getHeight(); i++) {
                // Get cb and cr of each pixel
                int cb_p = rgb2cb(Color.red(fg.getPixel(i,j)),
                                  Color.green(fg.getPixel(i,j)),
                                  Color.blue(fg.getPixel(i,j)));
                Log.i("DIP: cb (i,j): ", "("+i+","+j+")"+cb_p);

                int cr_p = rgb2cr(Color.red(fg.getPixel(i,j)),
                                  Color.green(fg.getPixel(i,j)),
                                  Color.blue(fg.getPixel(i,j)));
                Log.i("DIP: cr (i,j): ", "("+i+","+j+")"+cr_p);

                mask = colorClose(cb_p, cr_p, cb_key, cr_key);
                mask = 1.0 - mask;
                final_pixel = (int) Math.round( fg.getPixel(i,j) - mask*chroma_key + bg.getPixel(i,j)*mask );
                fg.setPixel(i, j, final_pixel);
                Log.i("DIP: px res (i,j): ", "("+i+","+j+")"+final_pixel);
            }
        }


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
