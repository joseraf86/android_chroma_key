package cigs.chromakey;



import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Uri imageUri = null;
    final static int CAPTURE_IMAGE_REQUEST_CODE = 1;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        setContentView(R.layout.main);

        final ImageButton boton = (ImageButton) findViewById(R.id.btn_capture);
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
    }

    // Respuesta asincrona de startActivityForResult
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)  {
        if ( requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if ( resultCode == RESULT_OK) {
                mHandler.postDelayed(mLaunchLevel1Task,0);
            }
            //else if ( resultCode == RESULT_CANCELED) {
                //Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            //}
            else {

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
        //int id = item.getItemId();


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
