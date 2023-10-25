package org.moa.etlits.ui.fragments;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.moa.etlits.R;
import org.moa.etlits.databinding.FragmentSearchBinding;
import org.moa.etlits.ui.activities.EstablishmentSummaryActivity;
import org.moa.etlits.ui.activities.MainActivity;
import org.moa.etlits.ui.adapters.AnimalSearchAdapter;
import org.moa.etlits.ui.adapters.EstablishmentSearchAdapter;
import org.moa.etlits.ui.viewmodels.SearchViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SearchFragment extends Fragment {
    public static final String ESTABLISHMENT_VIEW = "ESTABLISHMENT_VIEW";
    public static final String ANIMAL_VIEW = "ANIMAL_VIEW";

    private EstablishmentSearchAdapter establishmentAdapter;

    private AnimalSearchAdapter animalAdapter;

    private SearchViewModel searchViewModel;

    private FragmentSearchBinding binding;

    public SearchFragment() {
    }

     public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        establishmentAdapter = new EstablishmentSearchAdapter((MainActivity) getActivity(), new ArrayList<>());
        animalAdapter = new AnimalSearchAdapter((MainActivity) getActivity(), new ArrayList<>());


        binding.acvEstablishmentSearch.setAdapter(establishmentAdapter);
        binding.acvEstablishmentSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem != null) {
                    String code = selectedItem.split("-")[0];
                    Intent intent = new Intent(getActivity(), EstablishmentSummaryActivity.class);
                    intent.putExtra("code", code.trim());
                    startActivity(intent);
                }
            }
        });

        binding.acvAnimalSearch.setAdapter(animalAdapter);

        searchViewModel = new SearchViewModel((Application) getActivity().getApplicationContext());
        searchViewModel.getEstablishments().observe(getActivity(), lst -> {
            establishmentAdapter.submitList(lst);
        });

        searchViewModel.getAnimals().observe(getActivity(), lst -> {
            animalAdapter.submitList(lst);
        });

        searchViewModel.getSearchView().observe(getActivity(), viewName -> {
            if (viewName.equals(ESTABLISHMENT_VIEW)) {
                updateTab(binding.tvEstablishmentSearch, true);
                updateTab(binding.tvAnimalSearch, false);

                binding.acvEstablishmentSearch.setVisibility(View.VISIBLE);
                binding.acvAnimalSearch.setVisibility(View.GONE);
            } else {
                updateTab(binding.tvEstablishmentSearch, false);
                updateTab(binding.tvAnimalSearch, true);
                binding.acvAnimalSearch.setVisibility(View.VISIBLE);
                binding.acvEstablishmentSearch.setVisibility(View.GONE);
            }
        });

        binding.tvEstablishmentSearch.setOnClickListener(v -> {
            searchViewModel.switchView(ESTABLISHMENT_VIEW);
        });
        binding.tvAnimalSearch.setOnClickListener(v -> {
            searchViewModel.switchView(ANIMAL_VIEW);
        });
    }

    private void updateTab(TextView tab, boolean isActive) {
        if (isActive) {
            tab.setBackground(getActivity().getDrawable(R.drawable.bg_search_active));
            tab.setTextColor(getActivity().getColor(R.color.colorPrimary));
        } else {
            tab.setBackground(getActivity().getDrawable(R.drawable.bg_search_inactive));
            tab.setTextColor(getActivity().getColor(R.color.white));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.acvEstablishmentSearch.setText("");
    }
}