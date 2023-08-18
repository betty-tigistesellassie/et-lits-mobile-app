package org.moa.etlits.data.dao;


import org.moa.etlits.data.models.Animal;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AnimalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Animal animal);

    @Update
    void update(Animal animal);

    @Query("SELECT * FROM animals")
    LiveData<List<Animal>> getAllAnimals();

    @Query("SELECT * FROM animals where id=:animalId")
    LiveData<Animal> loadById(long animalId);
}