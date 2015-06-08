package cigs.chromakey;

import android.graphics.Bitmap;

/**
 * Created by usuario on 5/06/15.
 */
public class DIP {

    /** a utility function to convert colors in RGB into YCbCr*/
    public static int rgb2y (int r, int g, int b)
    {
        int y;
        y = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
        return (y);
    }

    /** a utility function to convert colors in RGB into YCbCr*/
    public static int rgb2cb (int r, int g, int b)
    {
        int cb;
        cb = (int) Math.round(128 + -0.168736 * r - 0.331264 * g + 0.5 * b);
        return (cb);
    }

    /** a utility function to convert colors in RGB into YCbCr*/
    public static int rgb2cr (int r, int g, int b)
    {
        int cr;
        cr = (int) Math.round(128 + 0.5 * r - 0.418688 * g - 0.081312 * b);
        return (cr);
    }

    /*decides if a color is close to the specified hue*/
    public static double colorclose(int Cb_p,int Cr_p,int Cb_key,int Cr_key,int tola,int tolb)
    {
        double temp = Math.sqrt((Cb_key - Cb_p) * (Cb_key - Cb_p) + (Cr_key - Cr_p) * (Cr_key - Cr_p));
        if (temp < tola) {return (0.0);}
        if (temp < tolb) {return ((temp-tola)/(tolb-tola));}
        return (1.0);
    }

    /* loop through file and preform chromakey */
    public static void chromakey() {
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
