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
    private int screen_w, screen_h;

    //private static final int PROGRESS = 0x1;
    //private ProgressBar mProgress;


    // Elementos para procesamiento

    Uri imageUri = null; // captured photo
    Bitmap bg_img, cp_bg_img = null;
    Bitmap fg_img, cp_fg_img = null;
    Bitmap mBitmap, dipped_img;

    DIP dip;
    Bitmap bgs[] = new Bitmap[10];
    Bitmap cache[] = new Bitmap[10];

    int last_bg_id;


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

        //Log.i(TAG, "imgView:"+imgView.getDrawingCache().getWidth());

        screen_w = 612;
        screen_h = 816;

        // Colocar foto tomada en pantalla
        imageUri = extras.getParcelable("image");

        mBitmap  = Utils.decodeSampledBitmapFromUri(this, imageUri, screen_w, screen_h);;




        imgView.setImageBitmap(mBitmap);

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

    private void changeToIndex(int index, int resourceId) {

        int i = index;
        last_bg_id = resourceId;

        if(bgs[i] == null){
            bgs[i] = BitmapFactory.decodeResource( getResources(),resourceId);
        }
        bg_img = bgs[i];
        if(cache[i] == null){
            cache[i] = processImage(fg_img,bg_img);
        }
        imgView.setImageBitmap(cache[i]);
        dipped_img = cache[i];
    }

    @Override
    public void onClick(View view) {

        // Activa menu contextual CAB para aceptar montaje
        if (mActionMode == null) {
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
                    changeToIndex(0, R.drawable.background_1);
                    break;
                case R.id.thumbnail_2:
                    changeToIndex(1, R.drawable.background_2);
                    break;
                case R.id.thumbnail_3:
                    changeToIndex(2, R.drawable.background_3);
                    break;
                default:
                    changeToIndex(0, R.drawable.background_1);
                    break;
            }
        }
        /* */
     }

    protected Bitmap processImage(Bitmap fg_img, Bitmap bg_img){
        //fg_img = Bitmap.createScaledBitmap( fg_img, bg_img.getWidth(), bg_img.getHeight(), false );
        cp_fg_img = Bitmap.createScaledBitmap( fg_img, screen_w, screen_h, false );
        cp_bg_img = Bitmap.createScaledBitmap( bg_img, screen_w, screen_h, false );

        // Comprobar dimensiones
        /*if ( cp_fg_img.getWidth()  != cp_bg_img.getWidth() ||
                cp_fg_img.getHeight() != cp_bg_img.getHeight() ) {
            Toast.makeText(getApplicationContext(), "la resolucion de la camara no coincide con el del fondo", Toast.LENGTH_SHORT).show();
            Log.w(TAG+":Onclick::", " no coinciden dimensiones de las imagenes");
        }*/

        // Procesar imagenes para vista previa
        dip.chromaKey(cp_fg_img, cp_bg_img);

        BitmapWorkerTask task = new BitmapWorkerTask(cp_fg_img, cp_bg_img);
        task.execute(0);

        // Mostrar resultado en vista previa
        dipped_img = cp_fg_img;

        //
        return dipped_img;

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

    /**
     * Menu Action Bar
     * @param menu
     * @return
     */
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

        //int id = item.getItemId();

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
                case R.id.next: // boton aceptar

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

        Uri tmp = Utils.getImageUri(getApplicationContext(), dipped_img);

        Intent myIntent = new Intent(ImgEditingActivity.this, SharingActivity.class);
        myIntent.putExtra("res_image_min", tmp);
        myIntent.putExtra("res_image", imageUri);
        myIntent.putExtra("bg_id", last_bg_id);
        ImgEditingActivity.this.startActivity(myIntent);

        }
    };
}
