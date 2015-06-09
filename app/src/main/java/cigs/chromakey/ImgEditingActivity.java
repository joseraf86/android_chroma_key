package cigs.chromakey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;


public class ImgEditingActivity extends AppCompatActivity
            implements View.OnClickListener
{

    ImageView imgView, bgView;
    MenuItem btnSave;
    Uri imageUri = null;

    private ActionMode mActionMode;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_editing);

        mHandler = new Handler();

        // set photo
        imgView = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        Bitmap mBitmap;

        imageUri = (Uri)extras.getParcelable("image");
        try{
            mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            imgView.setImageBitmap(mBitmap);
            //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
        }
        catch(FileNotFoundException e){}

        // add save button
        bgView = (ImageView) findViewById(R.id.imageView);
        bgView.setOnClickListener(this);

       // btnSave = (MenuItem) findViewById(R.id.save);
       // btnSave.setOnMenuItemClickListener(this);

        Bitmap fg_img = BitmapFactory.decodeResource( getResources(), R.drawable.foreground_3 );
        Bitmap bg_img = BitmapFactory.decodeResource( getResources(), R.drawable.background_1 );
        Bitmap cp_fg_img = Bitmap.createScaledBitmap( fg_img, 800, 1200, false );
        Bitmap cp_bg_img = Bitmap.createScaledBitmap( bg_img, 800, 1200, false );

        DIP dip = new DIP(cp_fg_img, cp_bg_img, 20, 50, -16668620);

        //Log.i("Edit:: tolerancia :", ""+Color.rgb(0,255,0)+"> "+Color.rgb(0,204,0)+"> "+Color.rgb(0,153,0));

        dip.chromaKey();

        imgView.setImageBitmap(cp_fg_img);

    }

    @Override
    public void onClick(View view) {

        // if actionmode is null "not started"
        if (mActionMode != null) {
            return;
        }

        // Start the CAB
        mActionMode = this.startActionMode(new ActionBarCallBack()); //
        view.setSelected(true);
/*
        if(view.getId()==findViewById(R.id.imageView).getId())
        {

        }
        */
     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_img_editing, menu);
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

    // menu contextual al seleccionar un fondo
    class ActionBarCallBack implements ActionMode.Callback {

        // 4. Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_img_editing, menu);
            return true;
        }

        // 5. Called when the user click share item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Log.i(ImgEditingActivity.class.getName(), "XXXXXXXXXXXXXXXXXXXXXXXXXX");

            switch (item.getItemId()) {
                case R.id.save:
                    //
                    mHandler.postDelayed(mLaunchLevel2Task, 0);
                    mode.finish(); // Action picked, so close the CAB
                    //Toast.makeText(ImgEditingActivity.this, "Shared!", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    Toast.makeText(ImgEditingActivity.this, "no", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }

        // 6. Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // 7. Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }

    }

    private Runnable mLaunchLevel2Task = new Runnable() {
        public void run() {

            Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
            myIntent.putExtra("res_image", imageUri);
            ImgEditingActivity.this.startActivity(myIntent);

        }
    };
}
