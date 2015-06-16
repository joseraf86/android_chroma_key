package cigs.chromakey;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.print.PrintHelper;

/**
 *
 */
public class PrintingAndSharing {

    Activity activity;
    PrintHelper printer;

    public PrintingAndSharing(Activity activity)  {

        printer = new PrintHelper(activity);

        this.activity = activity;
        printer.setScaleMode(PrintHelper.SCALE_MODE_FIT);

    }
    private void doPhotoPrint() {

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.hp_btn_next);

        printer.printBitmap("droids.jpg - test print", bitmap);
    }


}
