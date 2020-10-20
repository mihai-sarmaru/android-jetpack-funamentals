package com.sarmaru.mihai.jetpackfundamentals.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sarmaru.mihai.jetpackfundamentals.db.DogBreedDao;
import com.sarmaru.mihai.jetpackfundamentals.db.DogDatabase;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.repository.DogApiService;
import com.sarmaru.mihai.jetpackfundamentals.util.SharedPreferencesHelper;

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

    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;
    private AsyncTask<Void, Void, List<DogBreed>> retrieveDogsTask;

    private SharedPreferencesHelper preferencesHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L; // 5 minutes

    public DogListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        long updateTime = preferencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        if (updateTime != 0 && currentTime - updateTime < refreshTime) {
            fetchFromDatabase();
        } else {
            fetchFromRemote();
        }
    }

    public void refreshBypassCache() {
        fetchFromRemote();
    }

    public void fetchFromDatabase() {
        loading.setValue(true);
        retrieveDogsTask = new RetrieveDogsTask();
        retrieveDogsTask.execute();
    }

    private void fetchFromRemote() {
        loading.setValue(true);

        disposable.add(
                dogApiService.getDogsList().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(@io.reactivex.annotations.NonNull List<DogBreed> dogBreeds) {
                                // Store info in DB
                                insertTask = new InsertDogsTask();
                                insertTask.execute(dogBreeds);
                                Toast.makeText(getApplication(), "Retrieved from API", Toast.LENGTH_SHORT).show();
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

    private void dogsRetrieved(List<DogBreed> dogList) {
        dogsList.setValue(dogList);
        dogsLoadError.setValue(false);
        loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        // Cancel AsyncTask to prevent memory leak
        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }

        if (retrieveDogsTask != null) {
            retrieveDogsTask.cancel(true);
            retrieveDogsTask = null;
        }

    }

    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>> {

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> dogBreedList = lists[0];

            // Insert into DB received list
            DogBreedDao dogBreedDao = DogDatabase.getInstance(getApplication()).dogBreedDao();
            dogBreedDao.deleteAllDogs();
            List<DogBreed> newDogList = new ArrayList<>(dogBreedList);
            List<Long> result = dogBreedDao.insertAll(newDogList.toArray(new DogBreed[0]));

            // Attach UID to the object
            for (int i = 0; i < dogBreedList.size(); i++) {
                dogBreedList.get(i).uuid = result.get(i).intValue();
            }

            return dogBreedList;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            preferencesHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private class RetrieveDogsTask extends AsyncTask<Void, Void, List<DogBreed>> {

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogBreedDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "Retrieved from DB", Toast.LENGTH_SHORT).show();
        }
    }
}
