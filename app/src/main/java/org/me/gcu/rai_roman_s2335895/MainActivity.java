package org.me.gcu.rai_roman_s2335895;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

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

        SituationViewModel viewModel = new ViewModelProvider(this).get(SituationViewModel.class);
        viewModel.startAutoUpdate(new FeedLoader(getApplicationContext()));

        FragmentManager fm = getSupportFragmentManager();
        boolean landscape = findViewById(R.id.detail_container) != null;

        if (landscape) {
            // list on the left, details on the right, no back stack needed
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new SituationListFragment())
                    .replace(R.id.detail_container, new SituationDetailFragment())
                    .commit();
        } else {
            // portrait: remove any details pane left over from landscape
            Fragment leftover = fm.findFragmentById(R.id.detail_container);
            if (leftover != null) {
                fm.beginTransaction().remove(leftover).commit();
            }
            if (savedInstanceState == null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new SituationListFragment())
                        .commit();
            }
        }
    }
}