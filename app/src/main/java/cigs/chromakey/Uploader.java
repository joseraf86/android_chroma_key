package cigs.chromakey;

import android.net.Uri;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class Uploader {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void uploadChroma(Uri uri, String url) {
        // String url = "http://yourserver";
        File file = new File(uri.getPath());
        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"yourfile");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            //Do something with response...

        } catch (Exception e) {
            // show error
        }
        return;
    }

}
