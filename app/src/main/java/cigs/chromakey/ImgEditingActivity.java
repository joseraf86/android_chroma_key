package cigs.chromakey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class ImgEditingActivity extends AppCompatActivity
            implements View.OnClickListener
{

    ImageView imgView, bgView;
    MenuItem btnSave;
    Uri imageUri = null; // captured photo
    Bitmap dipped_img;

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

        // Colocar foto tomada en pantalla
        try {
            imageUri = (Uri)extras.getParcelable("image");
            mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            imgView.setImageBitmap(mBitmap);
            //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
        }
        catch(FileNotFoundException e){}

        // activar oyentes para cada thumbnail
        bgView = (ImageView) findViewById(R.id.thumbnail_1);
        bgView.setOnClickListener(this);

        bgView = (ImageView) findViewById(R.id.thumbnail_2);
        bgView.setOnClickListener(this);

        bgView = (ImageView) findViewById(R.id.thumbnail_3);
        bgView.setOnClickListener(this);

        bgView = (ImageView) findViewById(R.id.thumbnail_4);
        bgView.setOnClickListener(this);

        bgView = (ImageView) findViewById(R.id.thumbnail_5);
        bgView.setOnClickListener(this);

/*
        int rojo = Color.red(-16668620); 0,168,54
        int verde = Color.green(-16668620);
        int azul = Color.blue(-16668620);

        Log.i("Edit::", ""+rojo+"> "+verde+"> "+azul);
*/

        //Log.i("Edit:: tolerancia :", ""+Color.rgb(0,255,0)+"> "+Color.rgb(0,204,0)+"> "+Color.rgb(0,153,0));
    }

    @Override
    public void onClick(View view) {

        // if actionmode is null "not started"
        if (mActionMode == null) {
            // Start the CAB
            mActionMode = this.startActionMode(new ActionBarCallBack()); //
            view.setSelected(true);
        }

        //if( view.getId() == findViewById(R.id.imageView).getId())

        if (view instanceof ImageView)
        {
            ImageView tmp = (ImageView) view;
            Bitmap bg_img, cp_bg_img = null;
            Bitmap fg_img, cp_fg_img = null;

            //Bitmap bgBitmap = convertToBitmap( tmp.getDrawable(), view.getWidth(), view.getHeight() );

            // obtener fondo seleccionado
            switch (view.getId()) {

                case R.id.thumbnail_1:
                    bg_img = BitmapFactory.decodeResource( getResources(), R.drawable.background_1);
                    break;
                case R.id.thumbnail_2:
                    bg_img = BitmapFactory.decodeResource( getResources(), R.drawable.background_2);
                    break;
                case R.id.thumbnail_3:
                    bg_img = BitmapFactory.decodeResource( getResources(), R.drawable.background_3);
                    break;
                default:
                    bg_img = BitmapFactory.decodeResource( getResources(), R.drawable.background_2);
                    break;
            }

            // se define foreground como la foto capturada
            try{
                fg_img = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                cp_fg_img = Bitmap.createScaledBitmap( fg_img, 400, 500, false );
                cp_bg_img = Bitmap.createScaledBitmap( bg_img, 400, 500, false );

                //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
            }
            catch(FileNotFoundException e){}



            DIP dip = new DIP(cp_fg_img, cp_bg_img, 60, 62, Color.rgb(17,168,75));

            if ( cp_fg_img.getWidth()  != cp_bg_img.getWidth() ||
                 cp_fg_img.getHeight() != cp_bg_img.getHeight() ) {
                Toast.makeText(getApplicationContext(), "la resolucion de la camara no coincide con el del fondo", Toast.LENGTH_SHORT);
                Log.w("Edit:Onclick::", " no coinciden dimnsiones de las imagenes");
                return;
            }

            dip.chromaKey();

            // mostrar resultado
            dipped_img = Bitmap.createScaledBitmap( cp_fg_img, view.getWidth(), view.getHeight(), false );
            imgView.setImageBitmap(dipped_img);



        }
        /* */
     }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
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

            Uri tmp = getImageUri(getApplicationContext(), dipped_img);

            Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
            myIntent.putExtra("res_image", tmp);
            ImgEditingActivity.this.startActivity(myIntent);

        }
    };
}
