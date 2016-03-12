package uofm.software_engineering.group7.to_do_bot.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import uofm.software_engineering.group7.to_do_bot.R;

/**
 * Created by linxi_000 on 2016/3/12.
 */
public class SpinnerAdapter extends ArrayAdapter<Integer> {
    private Integer[] images;

    public SpinnerAdapter(Context context, Integer[] images) {
        super(context, android.R.layout.simple_spinner_item, images);
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(images[position]);
        return imageView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position);
    }

    private View getImageForPosition(int position) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(images[position]);

        return imageView;
    }
}
