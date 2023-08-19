package hn.uth.examen201930010221.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName ="fitness_tabla")
public class Fitnes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Integer IdFitness;
    @NonNull
    @ColumnInfo(name = "lugar")
    private String lugar;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "coordinador")
    private String coordinador;

    @ColumnInfo(name = "ejercicio")
    private String ejercicio;
    @ColumnInfo(name = "acompaniante")
    private String acompaniante;

    @NonNull
    @ColumnInfo(name = "latitud")
    private String latitud;

    @ColumnInfo(name = "longitud")
    private String longitud;

    public Fitnes(@NonNull String lugar, String fecha, String coordinador, String ejercicio, String acompaniante, @NonNull String latitud, String longitud) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.coordinador = coordinador;
        this.ejercicio = ejercicio;
        this.acompaniante = acompaniante;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getIdFitness() {
        return IdFitness;
    }

    public void setIdFitness(Integer idFitness) {
        IdFitness = idFitness;
    }

    @NonNull
    public String getLugar() {
        return lugar;
    }

    public void setLugar(@NonNull String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(String coordinador) {
        this.coordinador = coordinador;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getAcompaniante() {
        return acompaniante;
    }

    public void setAcompaniante(String acompaniante) {
        this.acompaniante = acompaniante;
    }

    @NonNull
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(@NonNull String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
