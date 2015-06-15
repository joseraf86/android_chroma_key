package cigs.chromakey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.print.PrintHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;


public class SharingActivity extends AppCompatActivity
    implements View.OnClickListener {

    private static final String TAG = SharingActivity.class.getName();

    ImageView imgView;
    ImageButton btnEmail, btnPrint, btnUpload;
    Button btnShare;
    PrintHelper printer;
    Bitmap mBitmap, fBitmap;
    Uri imageUri, imageViewUri;
    int background_id;

    private String getUserEmail(Context context) {

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        String email = "";
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
                break;
            }
        }
        return email;
    }

    private void processImage(){
        Bitmap tmp = Utils.decodeBitmapFromUri(this, imageUri);
        Bitmap bg = BitmapFactory.decodeResource(getResources(), background_id);

        imgView = (ImageView) findViewById(R.id.res_image);

        // se necesita q el bitmap sea mutable para poder cambiarle el fondo
        Bitmap fgt = tmp.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap fg  = Bitmap.createScaledBitmap(fgt, bg.getWidth(), bg.getHeight(), false);

        DIP dip = new DIP(60, 62, Color.rgb(17, 168, 75));
        if (bg!=null && fg !=null) {
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

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        imageUri = extras.getParcelable("res_image");
        imageViewUri = extras.getParcelable("res_image_min");
        background_id = extras.getInt("bg_id");

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
            // Enviar correo al usuario
            String[] emails = {getUserEmail(getApplicationContext())};

            // Cambiar esta imagen a la foto imgUri
            // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hp_banner);
            // Uri tmp = Utils.getImageUri(getApplicationContext(), fBitmap);
            Mailer.composeEmail(emails, "HP Chroma photo stand", "HP Chroma photo stand", imageViewUri, this);
            return;
        }

        if (v.getId() == R.id.btn_upload) {
            Uploader.uploadChroma(imageViewUri);
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
        //int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


}
