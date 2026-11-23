package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;
import java.util.Map;

public class SituationDetailFragment extends Fragment {

    public SituationDetailFragment() {
        super(R.layout.fragment_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView road = view.findViewById(R.id.detailRoad);
        TextView description = view.findViewById(R.id.detailDescription);
        TextView type = view.findViewById(R.id.detailType);
        TextView start = view.findViewById(R.id.detailStart);
        TextView end = view.findViewById(R.id.detailEnd);
        TextView delay = view.findViewById(R.id.detailDelay);
        TextView direction = view.findViewById(R.id.detailDirection);
        TextView from = view.findViewById(R.id.detailFrom);
        TextView to = view.findViewById(R.id.detailTo);
        TextView extras = view.findViewById(R.id.detailExtras);

        View back = view.findViewById(R.id.detailBack);
        back.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        if (requireActivity().findViewById(R.id.detail_container) != null) {
            back.setVisibility(View.GONE);
        }

        SituationViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(SituationViewModel.class);

        viewModel.getSelected().observe(getViewLifecycleOwner(), s -> {
            if (s == null) {
                road.setText("Select an item to see its details");
                description.setText("");
                type.setText("");
                start.setText("");
                end.setText("");
                delay.setText("");
                direction.setText("");
                from.setText("");
                to.setText("");
                extras.setText("");
                return;
            }
            String shownRoad = s.getRoad() != null ? s.getRoad() : s.getExtras().get("Road number");
            road.setText(shownRoad == null ? "Road not given" : shownRoad);
            description.setText(s.getDescription());
            type.setText("Type: " + Situation.humanise(s.getType()));
            start.setText("Starts: " + Situation.prettyDate(s.getStartTime()));
            end.setText("Ends: " + Situation.prettyDate(s.getEndTime()));
            delay.setText("Delays: " + Situation.humanise(s.getDelayType()));
            direction.setText("Direction: " + Situation.humanise(s.getDirection()));
            from.setText("From: " + point(s.getFromLat(), s.getFromLon()));
            to.setText("To: " + point(s.getToLat(), s.getToLon()));

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> e : s.getExtras().entrySet()) {
                sb.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            }
            extras.setText(sb.toString().trim());
        });
    }

    private String point(double lat, double lon) {
        if (Double.isNaN(lat) || Double.isNaN(lon)) return "Not given";
        return String.format(Locale.UK, "%.5f, %.5f", lat, lon);
    }
}