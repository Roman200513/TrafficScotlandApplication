package org.me.gcu.rai_roman_s2335895;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SituationViewModel extends ViewModel {
    private static final long UPDATE_MINUTES = 60;

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

    public LiveData<ArrayList<Situation>> getSituations() {
        return situations;
    }

    public LiveData<Situation> getSelected() {
        return selected;
    }

    public void select(Situation s) {
        selected.setValue(s);
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
            if (query.isEmpty() || road.contains(query) || desc.contains(query)) {
                result.add(s);
            }
        }
        situations.setValue(result);
    }

    @Override
    protected void onCleared() {
        if (scheduler != null) scheduler.shutdownNow();
    }
}