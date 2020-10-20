package com.sarmaru.mihai.jetpackfundamentals.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sarmaru.mihai.jetpackfundamentals.db.DogDatabase;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

public class DogDetailViewModel extends AndroidViewModel {

    private RetrieveDogTask task;

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();

    public DogDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid) {
        task = new RetrieveDogTask();
        task.execute(uuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        // Clear task to prevent memory leak
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    private class RetrieveDogTask extends AsyncTask<Integer, Void, DogBreed> {

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogBreedDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);
        }
    }
}
