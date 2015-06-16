package cigs.chromakey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class MailActivity extends AppCompatActivity {

    private static final String TAG = MailActivity.class.getName();
    private EditText etEmailAddrss;

    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail);

        // se reciben recursos de la actividad anterior
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        imageUri = extras.getParcelable("res_image");

        etEmailAddrss    = (EditText) findViewById(R.id.edittext);
        etEmailAddrss.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(etEmailAddrss, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

    }

    public void onClick (View v)
    {
        if (v.getId() == R.id.btn_email) {
            // Enviar comEditrreo al usuario
            //getUserEmail(getApplicationContext())
            String str = etEmailAddrss.getText().toString();

            if ( !str.isEmpty() ) {
                String[] emails = {etEmailAddrss.getText().toString()};
                Eula.showEula(this);

                // Cambiar esta imagen a la foto imgUri
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hp_banner);
                // Uri tmp = Utils.getImageUri(getApplicationContext(), fBitmap);
                Mailer.composeEmail(emails, "HP Chroma photo stand", "HP Chroma photo stand", imageUri, this);
            }


        }

        //if (v.getId() == R.id.btn_upload) {
        //Uploader.uploadChroma(imageViewUri);
        //}
    }

    private String getUserEmail(Context context) {

        Log.i(TAG, "getting user emails");
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts   = AccountManager.get(context).getAccounts();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mail, menu);
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


}
