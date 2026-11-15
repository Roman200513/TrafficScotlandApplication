package org.me.gcu.rai_roman_s2335895;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SituationViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Situation>> situations =
            new MutableLiveData<>(new ArrayList<>());
    private final SituationParser parser = new SituationParser();

    public LiveData<ArrayList<Situation>> getSituations() {
        return situations;
    }

    public void loadFromXml(String xml) {
        situations.setValue(parser.parse(xml));
    }

    private final MutableLiveData<Situation> selected = new MutableLiveData<>();

    public LiveData<Situation> getSelected() {
        return selected;
    }

    public void select(Situation s) {
        selected.setValue(s);
    }

}