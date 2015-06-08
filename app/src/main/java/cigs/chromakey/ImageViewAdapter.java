package cigs.chromakey;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by usuario on 8/06/15.
 */
public class ImageViewAdapter extends ArrayAdapter<Integer> {

    private final Activity context;
    private final Integer[] imageId;

    public ImageViewAdapter(Activity context, Integer[] imageId) {
        super(context, R.layout.list_single );
        this.context = context;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        //imageView.setImageResource(imageId[position]);
        imageView.setBackgroundColor(imageId[position]);
        return imageView;
    }

}
