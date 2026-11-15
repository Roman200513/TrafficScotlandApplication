package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class SituationListFragment extends Fragment {

    public SituationListFragment() {
        super(R.layout.fragment_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.situationList);

        SituationViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(SituationViewModel.class);

        Button roadworksButton = view.findViewById(R.id.roadworksButton);
        Button incidentsButton = view.findViewById(R.id.incidentsButton);
        roadworksButton.setOnClickListener(v -> viewModel.showRoadworks());
        incidentsButton.setOnClickListener(v -> viewModel.showIncidents());

        viewModel.getSituations().observe(getViewLifecycleOwner(), list -> {
            ArrayList<String> rows = new ArrayList<>();
            for (Situation s : list) {
                rows.add(s.getType() + ": " + s.getDescription());
            }
            listView.setAdapter(new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_list_item_1, rows));
        });

        listView.setOnItemClickListener((parent, itemView, position, id) -> {
            ArrayList<Situation> current = viewModel.getSituations().getValue();
            viewModel.select(current.get(position));
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SituationDetailFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
