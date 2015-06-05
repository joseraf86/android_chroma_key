package cigs.a015_captureandprint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;

import java.io.FileNotFoundException;


public class ImgEditingActivity extends ActionBarActivity {

    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_editing);


        imgView = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras !=null) {

            Parcelable rtrv_value = extras.getParcelable("image");

            try{
                Bitmap mBitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream( (Uri)rtrv_value ));
                //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
                imgView.setImageBitmap(mBitmap);
            }
            catch(FileNotFoundException e){}



        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_img_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Runnable mLaunchLevel2Task = new Runnable() {
        public void run() {
            Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
            ImgEditingActivity.this.startActivity(myIntent);
        }
    };
}
