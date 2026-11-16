package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

        EditText searchBox = view.findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel.getSituations().observe(getViewLifecycleOwner(), list -> {
            listView.setAdapter(new SituationAdapter(requireContext(), list));
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