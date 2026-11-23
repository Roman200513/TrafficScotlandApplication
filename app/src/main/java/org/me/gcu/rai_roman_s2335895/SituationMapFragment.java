package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SituationMapFragment extends Fragment {

    private GoogleMap map;

    public SituationMapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.mapBack).setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        SituationViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(SituationViewModel.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                map = googleMap;
                map.getUiSettings().setZoomControlsEnabled(true);
                // Same list the list screen shows, so search and distance filters apply here too
                viewModel.getSituations().observe(getViewLifecycleOwner(), this::showPins);
            });
        }
    }

    private void showPins(List<Situation> list) {
        if (map == null || list == null) return;
        map.clear();

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        int count = 0;
        LatLng last = null;

        for (Situation s : list) {
            if (s.getPoints().isEmpty()) continue;
            GeoPoint p = s.getPoints().get(0);
            LatLng pos = new LatLng(p.getLat(), p.getLon());
            map.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(s.getRoad() == null ? "Road not given" : s.getRoad())
                    .snippet(s.getDescription()));
            bounds.include(pos);
            last = pos;
            count++;
        }

        if (count == 1) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(last, 14));
        } else if (count > 1) {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    bounds.build(),
                    getResources().getDisplayMetrics().widthPixels,
                    getResources().getDisplayMetrics().heightPixels,
                    150));
        }
    }
}