package hn.uth.examen201930010221.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import hn.uth.examen201930010221.entity.Contacto;

@Dao
public interface ContactosDao {

    @Insert
    void insert(Contacto data);

    @Update
    void update(Contacto data);

    @Query("DELETE FROM contactos_table")
    void deleteAll();


    @Query("select * from contactos_table order by id")
    LiveData<List<Contacto>> getContactos();

    @Query("SELECT * FROM contactos_table WHERE fav > 0 ORDER BY fav DESC")
    LiveData<List<Contacto>> getContactosFrecuentes();

    @Query("SELECT * FROM contactos_table WHERE id = :id")
    LiveData<Contacto> obtenerContactoPorId(int id);
}
