package cigs.chromakey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;


public class ImgEditingActivity extends ActionBarActivity
            implements View.OnClickListener
{

    ImageView imgView, bgView;
    MenuItem btnSave;

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

        if(extras !=null) {

            Parcelable rtrv_value = extras.getParcelable("image");

            try{
                Bitmap mBitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream( (Uri)rtrv_value ));
                //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
                imgView.setImageBitmap(mBitmap);
            }
            catch(FileNotFoundException e){}

// add save button

            bgView = (ImageView) findViewById(R.id.imageView);
            bgView.setOnClickListener(this);

           // btnSave = (MenuItem) findViewById(R.id.save);
           // btnSave.setOnMenuItemClickListener(this);

        }

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
            //Toast.makeText(ImgEditingActivity.this, "joder!", Toast.LENGTH_SHORT).show();
            switch (item.getItemId()) {
                case R.id.save:
                    Toast.makeText(ImgEditingActivity.this, "Shared!", Toast.LENGTH_SHORT).show();
                    // mHandler.postDelayed(mLaunchLevel1Task,0);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
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

    private Runnable mLaunchLevel1Task = new Runnable() {
        public void run() {

            //Bundle bundle = new Bundle();
            //bundle.putParcelable("image", imageUri);

            Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
            ImgEditingActivity.this.startActivity(myIntent);

        }
    };
}
