package org.moa.etlits.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.moa.etlits.data.models.AnimalRegistration;
import org.moa.etlits.data.models.Establishment;
import org.moa.etlits.databinding.FragmentAnimalRegMoveEventsBinding;
import org.moa.etlits.ui.adapters.EstablishmentSearchAdapter;
import org.moa.etlits.ui.viewmodels.AnimalRegViewModel;
import org.moa.etlits.utils.Constants;
import org.moa.etlits.utils.ViewUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class AnimalRegMoveEventsFragment extends Fragment {

    private AnimalRegViewModel viewModel;
    private FragmentAnimalRegMoveEventsBinding binding;
    private EstablishmentSearchAdapter holdingGroundEstablishmentAdapter;

    private EstablishmentSearchAdapter moveOnEstablishmentAdapter;
    private SharedPreferences sharedPreferences;

    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

    public AnimalRegMoveEventsFragment() {

    }

    public static AnimalRegMoveEventsFragment newInstance() {
        AnimalRegMoveEventsFragment fragment = new AnimalRegMoveEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAnimalRegMoveEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        holdingGroundEstablishmentAdapter = new EstablishmentSearchAdapter(getActivity(), new ArrayList<>());
        moveOnEstablishmentAdapter = new EstablishmentSearchAdapter(getActivity(), new ArrayList<>());

        viewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity(), (ViewModelProvider.Factory) new AnimalRegViewModel.Factory(getActivity().getApplication(), 0)).get(AnimalRegViewModel.class);
        viewModel.getEstablishments().observe(getActivity(), lst -> {
            holdingGroundEstablishmentAdapter.submitList(filterEstablishmentsByCategory(lst, Constants.HOLDING_GROUND_ESTABLISHMENT_CATEGORIES));
            moveOnEstablishmentAdapter.submitList(filterEstablishmentsByCategory(lst, Constants.PRODUCTION_TYPE_ESTABLISHMENT_CATEGORIES));
        });
        viewModel.getAnimalRegistration().observe(getActivity(), animalRegistration -> {
            AnimalRegistration ar = animalRegistration;
            if (ar != null) {
                binding.actvMoveOffEid.setText(ar.getHoldingGroundEid());
            } else {
                String eid = sharedPreferences.getString(Constants.DEFAULT_ESTABLISHMENT, "");
                viewModel.initAnimalRegistration(eid);
                ar = viewModel.getAnimalRegistration().getValue();
            }
            binding.actvMoveOnEid.setText(ar.getEstablishmentEid());
            binding.btnDateMoveOff.setText(dateFormat.format(ar.getDateMoveOff().getTime()));
            binding.btnDateMoveOn.setText(dateFormat.format(ar.getDateMoveOn().getTime()));
            binding.btnDateIdentification.setText(dateFormat.format(ar.getDateIdentification().getTime()));
        });

        viewModel.getAnimalRegFormState().observe(getActivity(), formState -> {
            if (formState == null) {
                return;
            }

            ViewUtils.showError(getActivity(), formState.getHoldingGroundEidError(), binding.actvMoveOffEid, binding.tvMoveOffEidError);
            ViewUtils.showError(getActivity(), formState.getEstablishmentEidError(), binding.actvMoveOnEid, binding.tvMoveOnEidError);
            ViewUtils.showError(getActivity(), formState.getDateIdentificationError(), binding.btnDateIdentification, binding.tvDateIdentificationError);
            ViewUtils.showError(getActivity(), formState.getDateMoveOffError(), binding.btnDateMoveOff, binding.tvDateMoveOffError);
            ViewUtils.showError(getActivity(), formState.getDateMoveOnError(), binding.btnDateMoveOn, binding.tvDateMoveOnError);
        });

        binding.actvMoveOffEid.setAdapter(holdingGroundEstablishmentAdapter);
        binding.actvMoveOffEid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem != null) {
                    String code = selectedItem.split("-")[0];
                    viewModel.setMoveOffEstablishment(code.trim());
                    viewModel.validateMoveEvents();
                }
            }
        });

        binding.actvMoveOffEid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                binding.actvMoveOffEid.showDropDown();
            }
        });


        binding.actvMoveOnEid.setAdapter(moveOnEstablishmentAdapter);
        binding.actvMoveOnEid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem != null) {
                    String code = selectedItem.split("-")[0];
                    viewModel.setMoveOnEstablishment(code.trim());
                    viewModel.validateMoveEvents();
                }
            }
        });

        binding.actvMoveOnEid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                binding.actvMoveOnEid.showDropDown();
            }
        });

        binding.btnDateMoveOn.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(), (dView, year, month, dayOfMonth) -> {
                viewModel.setDateMoveOn(year, month, dayOfMonth);
                viewModel.validateMoveEvents();
                binding.btnDateMoveOn.setText(dateFormat.format(viewModel.getDateMoveOn().getTime()));
            }, viewModel.getDateMoveOn().get(Calendar.YEAR), viewModel.getDateMoveOn().get(Calendar.MONTH), viewModel.getDateMoveOn().get(Calendar.DAY_OF_MONTH)).show();
        });
        binding.btnDateMoveOff.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(), (dView, year, month, dayOfMonth) -> {
                viewModel.setDateMoveOff(year, month, dayOfMonth);
                viewModel.validateMoveEvents();
                binding.btnDateMoveOff.setText(dateFormat.format(viewModel.getDateMoveOff().getTime()));
            }, viewModel.getDateMoveOff().get(Calendar.YEAR), viewModel.getDateMoveOff().get(Calendar.MONTH), viewModel.getDateMoveOff().get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.btnDateIdentification.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(), (dView, year, month, dayOfMonth) -> {
                viewModel.setDateIdentification(year, month, dayOfMonth);
                viewModel.validateMoveEvents();
                binding.btnDateIdentification.setText(dateFormat.format(viewModel.getDateIdentification().getTime()));
            }, viewModel.getDateIdentification().get(Calendar.YEAR), viewModel.getDateIdentification().get(Calendar.MONTH), viewModel.getDateIdentification().get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private boolean isInCategory(List <String> categories, Set<String> productionTypes) {
        for (String productionType : productionTypes) {
            if (categories.contains(productionType)) {
                return true;
            }
        }
        return false;
    }

    private List<Establishment> filterEstablishmentsByCategory(List<Establishment> establishments, List<String> categories) {
        List<Establishment> filteredEstablishments = new ArrayList<>();
        for (Establishment establishment : establishments) {
            if (isInCategory(categories, establishment.getProductionTypes())) {
                filteredEstablishments.add(establishment);
            }
        }
        return filteredEstablishments;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}