package com.sarmaru.mihai.jetpackfundamentals.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.repository.DogApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DogListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogsList = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogsLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    private DogApiService dogApiService = new DogApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    public DogListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        fetchFromRemote();
    }

    private void fetchFromRemote() {
        loading.setValue(true);

        disposable.add(
                dogApiService.getDogsList().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<DogBreed> dogBreeds) {
                                dogsList.setValue(dogBreeds);
                                dogsLoadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                dogsLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
