package org.me.gcu.rai_roman_s2335895;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SituationAdapter extends ArrayAdapter<Situation> {
    private static final long LONG_DAYS = 30;

    public SituationAdapter(Context context, ArrayList<Situation> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_situation, parent, false);
        }
        Situation s = getItem(position);

        View bar = convertView.findViewById(R.id.colourBar);
        TextView title = convertView.findViewById(R.id.itemTitle);
        TextView subtitle = convertView.findViewById(R.id.itemSubtitle);

        title.setText(s.getDescription());

        long days = s.getDurationDays();
        if (days < 0) {
            subtitle.setText(s.getType());
            bar.setBackgroundColor(Color.GRAY);
        } else if (days > LONG_DAYS) {
            subtitle.setText("Long: " + days + " days");
            bar.setBackgroundColor(Color.parseColor("#C62828"));
        } else {
            subtitle.setText("Short: " + days + " days");
            bar.setBackgroundColor(Color.parseColor("#2E7D32"));
        }
        return convertView;
    }
}