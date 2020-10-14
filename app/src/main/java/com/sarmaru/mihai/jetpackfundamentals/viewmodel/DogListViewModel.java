package com.sarmaru.mihai.jetpackfundamentals.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class DogListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogsList = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogsLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    public DogListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        // Mock data
        DogBreed dog1 = new DogBreed("1", "corgi", "15 years", "", "", "","");
        DogBreed dog2 = new DogBreed("2", "rottweiler", "10 years", "", "", "","");
        DogBreed dog3 = new DogBreed("3", "labrador", "18 years", "", "", "","");
        List<DogBreed> dogs = new ArrayList<DogBreed>();
        dogs.add(dog1);
        dogs.add(dog2);
        dogs.add(dog3);

        dogsList.setValue(dogs);
        dogsLoadError.setValue(false);
        loading.setValue(false);
    }
}
