package cigs.chromakey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cigs.chromakey.models.Mail;



public class MailActivity extends AppCompatActivity {

    private static final String TAG = MailActivity.class.getName();

    private EditText etEmailAddrss;
    private Mail mail;
    private Uri imageUri;

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

        mail = new Mail();
        mail.setSubject("HP Chroma photo stand");
        mail.setBody("HP Chroma photo stand");
        mail.attach(imageUri);

    }

    // compose
    public void accept() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL,   mail.getTo());
        intent.putExtra(Intent.EXTRA_SUBJECT, mail.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mail.getBody() + "<br/>Gracias por preferirnos"));
        // Colocamos el adjunto en el stream
        intent.putExtra(Intent.EXTRA_STREAM, mail.getAttachment());

        // Indicamos el MIME type
        // intent.setType("image/jpeg");

        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick (View v)
    {
        Log.i(TAG, "evento click");
        if (v.getId() == R.id.btn) {
            String[] emails = {etEmailAddrss.getText().toString()};
            mail.setTo(emails);

            Eula.showEula(this, emails, imageUri);

            // Enviar comEditrreo al usuario
            //getUserEmail(getApplicationContext())

            //if ( !str.isEmpty() ) {

                // Cambiar esta imagen a la foto imgUri
                // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hp_banner);
                // Uri tmp = Utils.getImageUri(getApplicationContext(), fBitmap);

           // }
        }

        //if (v.getId() == R.id.btn_upload) {
        //Uploader.uploadChroma(imageViewUri);
        //}
    }
/*
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
*/
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
