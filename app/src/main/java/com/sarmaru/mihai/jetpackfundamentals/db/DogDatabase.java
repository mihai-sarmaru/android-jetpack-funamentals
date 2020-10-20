package com.sarmaru.mihai.jetpackfundamentals.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;

@Database(entities = {DogBreed.class}, version = 1)
public abstract class DogDatabase extends RoomDatabase {

    private static DogDatabase instance;

    public static DogDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DogDatabase.class,
                    "dogdatabase")
                    .build();
        }
        return instance;
    }

    public abstract DogBreedDao dogBreedDao();

}
