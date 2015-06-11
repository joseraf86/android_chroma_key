package cigs.chromakey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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


public class ImgEditingActivity extends AppCompatActivity
            implements View.OnClickListener
{
    private static final String TAG = ImgEditingActivity.class.getName();

    // Interfaz
    ImageView imgView, bgView;
    private ActionMode mActionMode;
    private Handler mHandler;

    //private static final int PROGRESS = 0x1;
    //private ProgressBar mProgress;


    // Elementos para procesamiento
    DIP dip;
    Uri imageUri = null; // captured photo
    Bitmap bg_img, cp_bg_img = null;
    Bitmap fg_img, cp_fg_img = null;
    Bitmap mBitmap, dipped_img;
    Bitmap bgs[] = new Bitmap[10];
    //Bitmap cache[] = new Bitmap[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_editing);

        mHandler = new Handler();

        // Set dip
        dip = new DIP(60, 62, Color.rgb(17,168,75));

        // Set photo
        imgView = (ImageView) findViewById(R.id.image);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        Log.i(TAG, "imgView:"+imgView.getWidth());

        // Colocar foto tomada en pantalla
        imageUri = extras.getParcelable("image");
        mBitmap = Utils.decodeSampledBitmapFromUri(this, imageUri, 550, 600);
        imgView.setImageBitmap(mBitmap);
            //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);


        // Cargar fondos
        bgs[0] = BitmapFactory.decodeResource( getResources(), R.drawable.background_1);
        bgs[1] = BitmapFactory.decodeResource( getResources(), R.drawable.background_2);
        bgs[2] = BitmapFactory.decodeResource( getResources(), R.drawable.background_3);

        //mImageView.setImageBitmap(
        //        Utils.decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));

        // Activar oyentes para cada thumbnail
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

    }

    @Override
    public void onClick(View view) {

        // Activa menu contextual para aceptar montaje
        if (mActionMode == null) {
            // Start the CAB
            mActionMode = this.startActionMode(new ActionBarCallBack()); //
            view.setSelected(true);
        }

        // Evento sobre imagen del slider
        if (view instanceof ImageView)
        {
            // Se define foreground como la foto capturada
            fg_img = mBitmap;

            // Obtener fondo seleccionado
            switch (view.getId()) {

                case R.id.thumbnail_1:
                    if(bgs[0] == null){
                        bgs[0] = BitmapFactory.decodeResource( getResources(), R.drawable.background_1);
                    }
                    bg_img = bgs[0];
                    break;
                case R.id.thumbnail_2:
                    if(bgs[1] == null){
                        bgs[1] = BitmapFactory.decodeResource( getResources(), R.drawable.background_2);
                    }
                    bg_img = bgs[1];
                    break;
                case R.id.thumbnail_3:
                    if(bgs[2] == null){
                        bgs[2] = BitmapFactory.decodeResource( getResources(), R.drawable.background_3);
                    }
                    bg_img = bgs[2];
                    break;
                default:
                    if(bgs[0] == null){
                        bgs[0] = BitmapFactory.decodeResource( getResources(), R.drawable.background_1);
                    }
                    bg_img = bgs[0];
                    break;
            }

            //fg_img = Bitmap.createScaledBitmap( fg_img, bg_img.getWidth(), bg_img.getHeight(), false );
            cp_fg_img = Bitmap.createScaledBitmap( fg_img, 550, 600, false );
            cp_bg_img = Bitmap.createScaledBitmap( bg_img, 550, 600, false );

            // Comprobar dimensiones
            if ( cp_fg_img.getWidth()  != cp_bg_img.getWidth() ||
                 cp_fg_img.getHeight() != cp_bg_img.getHeight() ) {
                Toast.makeText(getApplicationContext(), "la resolucion de la camara no coincide con el del fondo", Toast.LENGTH_SHORT).show();
                Log.w(TAG+":Onclick::", " no coinciden dimensiones de las imagenes");
                return;
            }

            // Procesar imagenes para vista previa
            dip.chromaKey(cp_fg_img, cp_bg_img);
            // Mostrar resultado en vista previa
            dipped_img = cp_fg_img;
                    //Bitmap.createScaledBitmap(cp_fg_img, fg_img.getWidth(), fg_img.getHeight(), false);
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
/*
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
*/
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
       // int id = item.getItemId();

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
            Log.i(TAG, "action mode item clicked");

            switch (item.getItemId()) {
                case R.id.save: // boton aceptar
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

        Log.i(TAG, "Pasando uri por intent...");

        // Procesar imagenes con resolucion real
        //dip.chromaKey(fg_img, bg_img);
        //dipped_img = fg_img;


        Uri tmp = getImageUri(getApplicationContext(), dipped_img);

        Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
        myIntent.putExtra("res_image", tmp);
        ImgEditingActivity.this.startActivity(myIntent);

        }
    };
}
