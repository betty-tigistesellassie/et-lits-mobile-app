package org.moa.etlits.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.moa.etlits.R;
import org.moa.etlits.ui.activities.AnimalRegActivity;
import org.moa.etlits.ui.activities.AnimalRegListActivity;
import org.moa.etlits.ui.activities.SyncActivity;
import org.moa.etlits.utils.Constants;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class AnimalsFragment extends Fragment {
    private CardView registerAnimal;
    private CardView viewRegistrations;
    private Fragment searchFragment;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public AnimalsFragment() {
    }

  public static AnimalsFragment newInstance() {
        AnimalsFragment fragment = new AnimalsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_animals, container, false);
        if (savedInstanceState == null) {
            searchFragment = new SearchFragment();
            getChildFragmentManager().beginTransaction().add(R.id.animals_search_fragment, searchFragment, "search_animals").commit();
        }

        registerForResults(v);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        viewRegistrations = v.findViewById(R.id.card_view_registrations);
        registerAnimal = v.findViewById(R.id.card_register);
        registerAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnimalRegActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        viewRegistrations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnimalRegListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerForResults(View v) {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        int registeredAnimals = data.getIntExtra(AnimalRegActivity.REGISTERED_ANIMALS, 0);
                        if (registeredAnimals > 0) {
                            Snackbar.make(v, getString(R.string.animal_reg_close_snackbar_msg, String.valueOf(registeredAnimals)), Snackbar.LENGTH_LONG)
                                    .setAction(R.string.animal_reg_close_snackbar_action, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getActivity(), SyncActivity.class);
                                            intent.putExtra("syncType", Constants.SyncType.ALL_DATA.toString());
                                            startActivity(intent);
                                        }
                                    }).show();
                        }

                    }
                });

    }

}