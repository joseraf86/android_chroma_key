package cigs.chromakey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity {

    Uri imageUri = null;
    final static int CAPTURE_IMAGE_REQUEST_CODE = 1;

    private Handler mHandler;

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

        mHandler = new Handler();

        setContentView(R.layout.main);

        final Button boton = (Button) findViewById(R.id.btn_capture);
        boton.setOnClickListener(new View.OnClickListener() {
            // Tomar foto
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "prueba.jpg");
                values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);


                startActivityForResult( intent, CAPTURE_IMAGE_REQUEST_CODE);
            }
        });

        // Enviar correo al usuario
        String[] emails = {getUserEmail(getApplicationContext())};
        // Cambiar esta imagen a la foto imgUri
        Uri uri = Uri.parse("android.resource://" +
                             getPackageName() + "/" +
                             R.drawable.hp_banner);

        Mailer.composeEmail(emails, "HP Chroma photo stand", "HP Chroma photo stand", uri, this);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hp_banner);
       DIP dip = new DIP(bmp, bmp, 1,1,1);
        for (int i = 0; i < 255; i++) {
            int pixel = dip.getPixel(i, i);
            Log.i("MAinActivity:81", Color.red(pixel) + ", " + Color.green(pixel) + ", " + Color.blue(pixel));
        }

    }

    // Respuesta asincrona de startActivityForResult
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)  {
        if ( requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if ( resultCode == RESULT_OK) {
                mHandler.postDelayed(mLaunchLevel1Task,0);
            } else if ( resultCode == RESULT_CANCELED) {
                //Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private Runnable mLaunchLevel1Task = new Runnable() {
        public void run() {
            //Bundle bundle = new Bundle();
            //bundle.putParcelable("image", imageUri);
            Intent myIntent = new Intent(MainActivity.this, ImgEditingActivity.class);
            myIntent.putExtra("image", imageUri);
            MainActivity.this.startActivity(myIntent);

        }
    };
}
