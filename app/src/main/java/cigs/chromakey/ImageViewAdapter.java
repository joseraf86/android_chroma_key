package cigs.chromakey;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cigs.chromakey.models.ImageLoader;

/**
 * Created by usuario on 8/06/15.
 */
public class ImageViewAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater=null;
        public ImageLoader imageLoader;

        public ImageViewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader=new ImageLoader(activity.getApplicationContext());
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.list_single, null);

            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);

            // Setting all values in listview
            //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
            return vi;
        }
    }
