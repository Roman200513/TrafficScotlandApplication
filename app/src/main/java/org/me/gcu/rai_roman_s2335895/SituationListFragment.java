package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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

        EditText searchBox = view.findViewById(R.id.searchBox);
        searchBox.setText(viewModel.getQuery());
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

        Spinner distanceSpinner = view.findViewById(R.id.distanceSpinner);
        String[] options = {"Any distance", "Within 5 km of GCU", "Within 10 km of GCU",
                "Within 25 km of GCU", "Within 50 km of GCU"};
        distanceSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, options));
        distanceSpinner.setSelection(viewModel.getDistanceIndex(), false);
        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                if (pos == viewModel.getDistanceIndex()) return;
                viewModel.setDistanceIndex(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        viewModel.getSituations().observe(getViewLifecycleOwner(), list -> {
            listView.setAdapter(new SituationAdapter(requireContext(), list,
                    viewModel.isDistanceActive()));
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