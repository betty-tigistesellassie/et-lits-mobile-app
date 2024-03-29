package org.moa.etlits.ui.viewmodels;

import android.app.Application;

import org.moa.etlits.R;
import org.moa.etlits.data.models.CategoryValue;
import org.moa.etlits.data.repositories.CategoryValueRepository;
import org.moa.etlits.ui.validation.AnimalFormState;
import org.moa.etlits.utils.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AnimalEntryViewModel extends ViewModel {

    private MutableLiveData<AnimalFormState> animalFormState = new MutableLiveData<>();
    private CategoryValueRepository categoryValueRepository;
    private LiveData<List<CategoryValue>> breedList;
    private LiveData<List<CategoryValue>> sexList;

    private static Pattern ANIMAL_ID_REGEX_PATTERN = Pattern.compile("^ET \\d{10}$");

    AnimalEntryViewModel(Application application) {
        this.categoryValueRepository = new CategoryValueRepository(application);
        breedList = this.categoryValueRepository.loadByType(Constants.CATEGORY_KEY_BREEDS);
        sexList = this.categoryValueRepository.loadByType(Constants.CATEGORY_KEY_SEX);
    }

    public LiveData<List<CategoryValue>> getBreedList() {
        return breedList;
    }

    public LiveData<List<CategoryValue>> getSexList() {
        return sexList;
    }

    public LiveData<AnimalFormState> getAnimalFormState() {
        return animalFormState;
    }


    public void validateAnimalId(String animalId) {
        AnimalFormState newAnimalFormState = new AnimalFormState();
        if (isEmpty(animalId)) {
            newAnimalFormState.setAnimalIdError(R.string.animal_reg_field_required);
        } else {
            if (!isValidAnimalId(animalId)) {
                newAnimalFormState.setAnimalIdError(R.string.animal_reg_animal_id_invalid);
            }
        }
        animalFormState.setValue(newAnimalFormState);
    }

    public void validateAge(Integer age) {
        AnimalFormState newAnimalFormState = new AnimalFormState();
        if (isEmpty(age)){
            newAnimalFormState.setAgeError(R.string.animal_reg_field_required);
        } else {
            if (!isValidAge(age)) {
                newAnimalFormState.setAgeError(R.string.animal_reg_age_invalid);
            }
        }
        animalFormState.setValue(newAnimalFormState);
    }

    public void validateSex(String sex) {
        AnimalFormState newAnimalFormState = new AnimalFormState();
        if (isEmpty(sex)){
            newAnimalFormState.setSexError(R.string.animal_reg_field_required);
        }
        animalFormState.setValue(newAnimalFormState);
    }

    public void validateBreed(String breed) {
        AnimalFormState newAnimalFormState = new AnimalFormState();
        if (isEmpty(breed)){
            newAnimalFormState.setBreedError(R.string.animal_reg_field_required);
        }
        animalFormState.setValue(newAnimalFormState);
    }

    public void validateAllFields(String animalId, String sex, String breed,Integer age, boolean dead, String seller) {
        AnimalFormState newAnimalFormState = new AnimalFormState();
        if (isEmpty(animalId)) {
            newAnimalFormState.setAnimalIdError(R.string.animal_reg_field_required);
        } else {
            if (!isValidAnimalId(animalId)) {
                newAnimalFormState.setAnimalIdError(R.string.animal_reg_animal_id_invalid);
            }
        }

        if (isEmpty(sex)){
            newAnimalFormState.setSexError(R.string.animal_reg_field_required);
        }

        if (isEmpty(breed)){
            newAnimalFormState.setBreedError(R.string.animal_reg_field_required);
        }

        if (isEmpty(age)){
            newAnimalFormState.setAgeError(R.string.animal_reg_field_required);
        } else {
            if (!isValidAge(age)) {
                newAnimalFormState.setAgeError(R.string.animal_reg_age_invalid);
            }
        }

       animalFormState.setValue(newAnimalFormState);
    }

    private boolean isValidAnimalId(String animalId) {
        if (animalId == null) {
            return false;
        }

        Matcher matcher = ANIMAL_ID_REGEX_PATTERN.matcher(animalId);
        return matcher.matches();
    }

   private boolean isValidAge(Integer age) {
        if (age == null) {
            return false;
        }
        return age > 0 && age <= 300;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    private boolean isEmpty(Integer value) {
        return value == null || value <= 0;
    }

    public static class AnimalDataEntryViewModelFactory implements ViewModelProvider.Factory {
        private Application application;

        public AnimalDataEntryViewModelFactory(Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new AnimalEntryViewModel(application);
        }
    }
}