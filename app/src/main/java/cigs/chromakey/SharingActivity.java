package cigs.chromakey;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.print.PrintHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import cigs.chromakey.models.DIP;
import cigs.chromakey.models.FileUploader;


public class SharingActivity extends Activity
    implements View.OnClickListener {

    private static final String TAG = SharingActivity.class.getName();

    ImageView imgView;
    ImageButton btnEmail, btnPrint, btnUpload;
    PrintHelper printer;
    Bitmap fBitmap;
    Uri imageUri, imageViewUri;
    int background_id;


    private void processImage() {
        Log.i(TAG, "processing image");

        Bitmap tmp = Utils.decodeBitmapFromUri(this, imageUri);
        Bitmap bg  = BitmapFactory.decodeResource(getResources(), background_id);
        imgView    = (ImageView) findViewById(R.id.res_image);
        Bitmap fgt = null;

        // Se necesita q el bitmap sea mutable para poder cambiarle el fondo
        if (tmp != null){
            fgt = tmp.copy(Bitmap.Config.ARGB_8888, true);
        }

        Bitmap fg  = Bitmap.createScaledBitmap(fgt, bg.getWidth(), bg.getHeight(), false);

        DIP dip = new DIP(60, 62, Color.rgb(17, 168, 75));
        if (fg != null) {
            dip.chromaKey(fg, bg);
            fBitmap = fg;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_sharing);

        // Preparar printer
        printer = new PrintHelper(this);
        printer.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        imgView = (ImageView) findViewById(R.id.res_image);

        // Se reciben recursos de la actividad anterior
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        imageUri      = extras.getParcelable("res_image");
        imageViewUri  = extras.getParcelable("res_image_min");
        background_id = extras.getInt("bg_id");

        // Se visualiza la imagen pocesada
        Bitmap tmp = Utils.decodeBitmapFromUri(this, imageViewUri);
        imgView.setImageBitmap(tmp);//mBitmap

        // Asignar listeners a los botones del layout
        btnEmail = (ImageButton) findViewById(R.id.btn_email);
        btnEmail.setOnClickListener(this);

        btnPrint = (ImageButton) findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);

        btnUpload = (ImageButton) findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(this);
    }

    public void onClick (View v)
    {
        if (v.getId() == R.id.btn_print) {
            processImage();

            printer.printBitmap("HP stand muestra", fBitmap);
            return;
        }

        if (v.getId() == R.id.btn_email) {
            // Se pasa a mail activity
            Intent myIntent = new Intent(SharingActivity.this, MailActivity.class);
            myIntent.putExtra("res_image", imageViewUri);
            SharingActivity.this.startActivity(myIntent);
        }

        if (v.getId() == R.id.btn_upload) {
            SendHttpRequestTask t = new SendHttpRequestTask();

            String url = "http://192.168.1.45/gallery/gallery.php";
            String[] params = new String[]{url, "fileToUpload", "HP_photoboot.jpeg"};
            t.execute(params);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sharing, menu);

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

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String param1 = params[1];
            String param2 = params[2];
            String data = "No Response from the Server";

            Bitmap b = Utils.decodeBitmapFromUri( SharingActivity.this, imageViewUri );
            //        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+
            //                "/sdcard/Download/drawing.jpg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (b!=null)
                b.compress(Bitmap.CompressFormat.PNG, 0, baos);

            try {
                FileUploader client = new FileUploader(url);
                client.connectForMultipart();
                // client.addFormPart("name", "fileToUpload");
                client.addFilePart(param1, param2, baos.toByteArray());
                client.finishMultipart();
                data = client.getResponse();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            Log.i(TAG, "XXX"+data);
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            if (data.equals("0"))
                Toast.makeText(getApplicationContext(), "Foto subida exitosamente", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "No se pudo subir la foto", Toast.LENGTH_LONG).show();

            //Log.i(TAG, data);
        }

    }


}
