package hn.uth.examen201930010221.ui.acompaniantes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hn.uth.examen201930010221.database.ContactosRepository;
import hn.uth.examen201930010221.entity.Contacto;

public class AcompaniantesViewModel extends AndroidViewModel {

    private ContactosRepository repository;
    private final LiveData<List<Contacto>> dataSet;

    private final LiveData<List<Contacto>> dataFrecuentes;

    public AcompaniantesViewModel(@NonNull Application app) {
        super(app);
        this.repository= new ContactosRepository(app);
        this.dataSet= repository.getDataset();
        this.dataFrecuentes = repository.getContactosFrecuentes();
    }

    private ContactosRepository getRepository(){return repository;}
    public LiveData<List<Contacto>> getDataSet(){return dataSet;}

    public LiveData<List<Contacto>> getDataFrecuentes(){return dataFrecuentes;}

    public LiveData<Contacto> getDataContactoId(int id){
        return repository.getContactoPorId(id);
    }
    public void insert(Contacto data) {
        repository.insert(data);
    }

    public void update(Contacto data) {
        repository.update(data);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}