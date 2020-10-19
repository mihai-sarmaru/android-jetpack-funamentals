package com.sarmaru.mihai.jetpackfundamentals.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

public class DogDetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();

    public DogDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLiveData() {
        // Mock data
        DogBreed dog = new DogBreed("1", "corgi", "15 years", "", "companion", "calm", "");
        dogLiveData.setValue(dog);
    }
}
