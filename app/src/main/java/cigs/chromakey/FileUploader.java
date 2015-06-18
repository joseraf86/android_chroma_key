package cigs.chromakey;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUploader {
    public final static String TAG = FileUploader.class.getName();
    private String url;
    private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
    private HttpURLConnection connection;
    private OutputStream outputStream;

    public FileUploader(String url) {
        this.url = url;
    }

    public byte[] downloadImage(String imgName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.out.println("URL ["+url+"] - Name ["+imgName+"]");

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write( ("name=" + imgName).getBytes());

            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];

            while ( is.read(b) != -1)
                baos.write(b);

            con.disconnect();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return baos.toByteArray();
    }

    public void connectForMultipart() throws Exception {
        connection = (HttpURLConnection) ( new URL(url)).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.connect();
        outputStream = connection.getOutputStream();
    }

    public void addFormPart(String paramName, String value) throws Exception {
        writeParamData(paramName, value);
    }

    public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
        outputStream.write((delimiter + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        outputStream.write(("Content-Type: application/octet-stream\r\n").getBytes());
        outputStream.write(("Content-Transfer-Encoding: binary\r\n").getBytes());

        outputStream.write("\r\n".getBytes());
        outputStream.write(data);
        outputStream.write("\r\n".getBytes());
    }

    public void finishMultipart() throws Exception {
        outputStream.write((delimiter + boundary + delimiter + "\r\n").getBytes());
    }

    public String getResponse() throws Exception {
        InputStream is = connection.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuffer buffer = new StringBuffer();

        while ( is.read(b1) != -1)
            buffer.append(new String(b1));

        connection.disconnect();

        return buffer.toString();
    }

    private void writeParamData(String paramName, String value) throws Exception {
        outputStream.write((delimiter + boundary + "\r\n").getBytes());
        outputStream.write("Content-Type: text/plain\r\n".getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
        outputStream.write(("\r\n" + value + "\r\n").getBytes());
    }
}
