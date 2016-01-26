package com.example.aparna.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aparna on 1/23/16.
 */
public class CustomItemsAdapter extends ArrayAdapter<Item> {
    public CustomItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_item, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        // Populate the data into the template view using the data object
        tvId.setText(Long.toString(position + 1));
        tvText.setText(item.getText());
        // Return the completed view to render on screen
        return convertView;
    }

}
