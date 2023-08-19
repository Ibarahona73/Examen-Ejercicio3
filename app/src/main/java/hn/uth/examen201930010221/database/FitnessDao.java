package hn.uth.examen201930010221.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hn.uth.examen201930010221.entity.Fitnes;

@Dao
public interface FitnessDao {

    @Insert
    void insert(Fitnes data);

    @Update
    void update(Fitnes data);

    @Query("DELETE FROM fitness_tabla")
    void deleteAll();

    @Delete
    void delete(Fitnes data);

    @Query("select * from fitness_tabla order by id")
    LiveData<List<Fitnes>> getFitnes();
}
