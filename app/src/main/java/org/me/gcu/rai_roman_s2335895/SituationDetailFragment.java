package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

        SituationViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(SituationViewModel.class);

        viewModel.getSelected().observe(getViewLifecycleOwner(), s -> {
            if (s == null) return;
            road.setText(s.getRoad());
            description.setText(s.getDescription());
            type.setText("Type: " + s.getType());
            start.setText("Starts: " + s.getStartTime());
            end.setText("Ends: " + s.getEndTime());
            delay.setText("Delays: " + s.getDelayType());
        });
    }
}
