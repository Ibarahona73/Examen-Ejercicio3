package hn.uth.examen201930010221.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import hn.uth.examen201930010221.entity.Fitnes;

public class FitnessRepository {

    private FitnessDao dao;
    private LiveData<List<Fitnes>> dataset;
    public FitnessRepository(Application app) {
        Database db = Database.getDatabase(app);
        this.dao = db.FitnessDao();
        this.dataset = dao.getFitnes();
    }

    public LiveData<List<Fitnes>> getDataset() {
        return dataset;
    }

    public void insert(Fitnes nuevo){
        //INSERTANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.insert(nuevo);
        });
    }

    public void update(Fitnes actualizar){
        //ACTUALIZANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.update(actualizar);
        });
    }

    public void delete(Fitnes borrar){
        //BORRANDO UN REGISTRO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.delete(borrar);
        });
    }

    public void deleteAll(){
        //BORRANDO TODOS LOS REGISTROS DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
