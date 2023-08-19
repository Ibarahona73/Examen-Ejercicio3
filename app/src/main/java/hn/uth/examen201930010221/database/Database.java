package hn.uth.examen201930010221.database;

import android.content.Context;

import androidx.annotation.NonNull;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import hn.uth.examen201930010221.entity.Contacto;
import hn.uth.examen201930010221.entity.Fitnes;

@androidx.room.Database(version = 1, exportSchema = false, entities = {Contacto.class,  Fitnes.class})
public abstract class Database extends RoomDatabase {

    public abstract FitnessDao FitnessDao();

    public abstract ContactosDao ContactosDao();

    private static volatile Database INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {

                    Callback miCallback = new Callback() {

                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            databaseWriteExecutor.execute(() -> {
                                FitnessDao dao = INSTANCE.FitnessDao();
                                dao.deleteAll();

                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "ejercicio3").addCallback(miCallback).build();
                }
            }
        }
        return INSTANCE;
    }
}


