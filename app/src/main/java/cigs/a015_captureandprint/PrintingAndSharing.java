package cigs.a015_captureandprint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.print.PrintHelper;

/**
 * Created by usuario on 4/06/15.
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

        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.chin);

        printer.printBitmap("droids.jpg - test print", bitmap);
    }


}
