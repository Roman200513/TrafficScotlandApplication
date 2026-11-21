package org.me.gcu.rai_roman_s2335895;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SituationViewModel extends ViewModel {
    private static final long UPDATE_MINUTES = 60;

    // Fixed reference point (roughly Glasgow Caledonian University)
    public static final double CENTRE_LAT = 55.8667;
    public static final double CENTRE_LON = -4.2500;
    public static final double[] DISTANCES_KM = {0, 5, 10, 25, 50};

    private final MutableLiveData<ArrayList<Situation>> situations =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Situation> selected = new MutableLiveData<>();
    private final SituationParser parser = new SituationParser();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private ScheduledExecutorService scheduler;

    private ArrayList<Situation> roadworks = new ArrayList<>();
    private ArrayList<Situation> incidents = new ArrayList<>();
    private boolean showingRoadworks = true;
    private String query = "";
    private int distanceIndex = 0;

    public LiveData<ArrayList<Situation>> getSituations() {
        return situations;
    }

    public LiveData<Situation> getSelected() {
        return selected;
    }

    public void select(Situation s) {
        selected.setValue(s);
    }

    public String getQuery() {
        return query;
    }

    public int getDistanceIndex() {
        return distanceIndex;
    }

    public boolean isDistanceActive() {
        return distanceIndex > 0;
    }

    public void setDistanceIndex(int index) {
        distanceIndex = index;
        refresh();
    }

    public void startAutoUpdate(FeedLoader loader) {
        if (scheduler != null) return;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                ArrayList<Situation> r = parser.parse(loader.roadworks());
                ArrayList<Situation> i = parser.parse(loader.incidents());
                if (r.isEmpty() && i.isEmpty()) return;
                mainHandler.post(() -> {
                    roadworks = r;
                    incidents = i;
                    refresh();
                });
                Log.d("AutoUpdate", "Refreshed: " + r.size() + " roadworks, " + i.size() + " incidents");
            } catch (Exception e) {
                Log.e("AutoUpdate", "Update failed", e);
            }
        }, 0, UPDATE_MINUTES, TimeUnit.MINUTES);
    }

    public void showRoadworks() {
        showingRoadworks = true;
        refresh();
    }

    public void showIncidents() {
        showingRoadworks = false;
        refresh();
    }

    public void search(String text) {
        query = text == null ? "" : text.trim().toLowerCase();
        refresh();
    }

    private void refresh() {
        ArrayList<Situation> source = showingRoadworks ? roadworks : incidents;
        ArrayList<Situation> result = new ArrayList<>();
        for (Situation s : source) {
            String road = s.getRoad() == null ? "" : s.getRoad().toLowerCase();
            String desc = s.getDescription() == null ? "" : s.getDescription().toLowerCase();
            boolean textOk = query.isEmpty() || road.contains(query) || desc.contains(query);

            boolean distanceOk = true;
            if (distanceIndex > 0) {
                double km = s.distanceKmFrom(CENTRE_LAT, CENTRE_LON);
                distanceOk = !Double.isNaN(km) && km <= DISTANCES_KM[distanceIndex];
            }
            if (textOk && distanceOk) result.add(s);
        }
        if (distanceIndex > 0) {
            Collections.sort(result, (a, b) -> Double.compare(
                    a.distanceKmFrom(CENTRE_LAT, CENTRE_LON),
                    b.distanceKmFrom(CENTRE_LAT, CENTRE_LON)));
        }
        situations.setValue(result);
    }

    @Override
    protected void onCleared() {
        if (scheduler != null) scheduler.shutdownNow();
    }
}