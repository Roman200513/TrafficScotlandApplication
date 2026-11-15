package org.me.gcu.rai_roman_s2335895;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SituationViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Situation>> situations =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Situation> selected = new MutableLiveData<>();
    private final SituationParser parser = new SituationParser();

    private ArrayList<Situation> roadworks = new ArrayList<>();
    private ArrayList<Situation> incidents = new ArrayList<>();
    private boolean loaded = false;

    public LiveData<ArrayList<Situation>> getSituations() {
        return situations;
    }

    public LiveData<Situation> getSelected() {
        return selected;
    }

    public void select(Situation s) {
        selected.setValue(s);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void loadFromXml(String roadworksXml, String incidentsXml) {
        roadworks = parser.parse(roadworksXml);
        incidents = parser.parse(incidentsXml);
        loaded = true;
        situations.setValue(roadworks);
    }

    public void showRoadworks() {
        situations.setValue(roadworks);
    }

    public void showIncidents() {
        situations.setValue(incidents);
    }
}