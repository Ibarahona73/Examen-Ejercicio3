package hn.uth.examen201930010221.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hn.uth.examen201930010221.database.FitnessRepository;
import hn.uth.examen201930010221.entity.Fitnes;

public class EventoViewModel extends AndroidViewModel {

    private FitnessRepository repository;
    private final LiveData<List<Fitnes>> dataSet;

    public EventoViewModel(@NonNull Application app) {
        super(app);
        this.repository= new FitnessRepository(app);
        this.dataSet= repository.getDataset();
    }

    private FitnessRepository getRepository(){return repository;}
    public LiveData<List<Fitnes>> getDataSet(){return dataSet;}

    public void insert(Fitnes data) {
        repository.insert(data);
    }

    public void update(Fitnes data) {
        repository.update(data);
    }

    public void delete(Fitnes data) {
        repository.delete(data);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}