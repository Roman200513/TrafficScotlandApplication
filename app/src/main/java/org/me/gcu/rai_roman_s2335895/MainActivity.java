package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView situationList;

    //DOWNLOADING FROM TRAFFIC SCOTLAND (used later, once live access works)
    final String clientId = "YOUR_ID_HERE";
    final String clientKey = "YOUR_KEY_HERE";
    final String unplannedEventsURL = "https://datex2.trafficscotland.org/rest/2.3/publications/UnplannedEvents/Content.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        situationList = findViewById(R.id.situationList);

        SituationViewModel viewModel = new ViewModelProvider(this).get(SituationViewModel.class);

        viewModel.getSituations().observe(this, list -> {
            ArrayList<String> rows = new ArrayList<>();
            for (Situation s : list) {
                rows.add(s.getType() + ": " + s.getDescription());
            }
            situationList.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, rows));
        });

        if (viewModel.getSituations().getValue().isEmpty()) {
            viewModel.loadFromXml(loadExample("roadworksContentExample.xml"));
        }
    }

    // Reads a file from the assets folder into a String
    private String loadExample(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e("Example", "Could not read file " + e);
        }
        return sb.toString();
    }
}