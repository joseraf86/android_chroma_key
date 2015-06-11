package cigs.chromakey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;


public class SharingActivity extends AppCompatActivity
    implements View.OnClickListener {

    ImageView imgView;
    Button btnEmail, btnPrint, btnShare;
    PrintHelper printer;
    Bitmap mBitmap;
    Uri imageUri;

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
        imageUri = extras.getParcelable("image");

        try{
            mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            imgView.setImageBitmap(mBitmap);
            //mBitmap = Bitmap.createScaledBitmap(mBitmap, 500, 750, false);
        }
        catch(FileNotFoundException e){}

        // Set photo
        imgView = (ImageView) findViewById(R.id.res_image);
        imgView.setImageBitmap(mBitmap);

        // Asignar listeners a los botones del layout
        btnEmail = (Button) findViewById(R.id.btn_email);
        btnEmail.setOnClickListener(this);

        btnPrint = (Button) findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(this);
        
        btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);

    }

    public void onClick (View v)
    {
        if (v.getId() == R.id.btn_print) {
            printer.printBitmap("HP stand muestra", mBitmap);
            return;
        }

        if (v.getId() == R.id.btn_email) {
            // Enviar correo al usuario
            String[] emails = {getUserEmail(getApplicationContext())};

            // Cambiar esta imagen a la foto imgUri
            // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hp_banner);
            Mailer.composeEmail(emails, "HP Chroma photo stand", "HP Chroma photo stand", imageUri, this);
            return;
        }

        if (v.getId() == R.id.btn_share) {
            Sharer.shareText( "Hello World from my custom app", this);
            return;
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
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


    }


}
