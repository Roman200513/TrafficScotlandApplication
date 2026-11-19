package org.me.gcu.rai_roman_s2335895;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SituationAdapter extends ArrayAdapter<Situation> {
    private static final long LONG_DAYS = 30;
    private static final long SHORT_DAYS = 365;

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
        ImageView icon = convertView.findViewById(R.id.itemIcon);
        TextView title = convertView.findViewById(R.id.itemTitle);
        TextView subtitle = convertView.findViewById(R.id.itemSubtitle);

        title.setText(s.getDescription());
        icon.setImageResource(iconFor(s.getType()));
        icon.setContentDescription(s.getType());

        long days = s.getDurationDays();
        if (days < 0) {
            subtitle.setText(s.getType());
            bar.setBackgroundColor(Color.GRAY);
        } else if (days > LONG_DAYS) {
            subtitle.setText("Long delay: " + days + " days");
            bar.setBackgroundColor(Color.parseColor("#C62828"));
        } else if (days > SHORT_DAYS) {
            subtitle.setText("Medium delay: " + days + " days");
            bar.setBackgroundColor(Color.parseColor("#F9A825"));
        } else {
            subtitle.setText("Short delay: " + days + " days");
            bar.setBackgroundColor(Color.parseColor("#2E7D32"));
        }
        return convertView;
    }

    private int iconFor(String type) {
        String t = type == null ? "" : type.toLowerCase();
        if (t.contains("maintenance") || t.contains("roadworks") || t.contains("construction")) {
            return R.drawable.ic_roadworks;
        }
        return R.drawable.ic_incident;
    }
}