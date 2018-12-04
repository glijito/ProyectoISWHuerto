package desarrollojhlibreros.com.proyectoiswhuerto.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Pronostico {
    @Expose
    @SerializedName("cnt")
    private int cnt;
    @Expose
    @SerializedName("list")
    private Clima climas[];
    @Expose
    @SerializedName("city")
    private Ciudad ciudad;

    public Pronostico(int cnt, Clima[] climas, Ciudad ciudad) {
        this.cnt = cnt;
        this.climas = climas;
        this.ciudad = ciudad;
    }

    public Pronostico() {
        this(0,null,null);
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public Clima[] getClimas() {
        return climas;
    }

    public void setClimas(Clima[] climas) {
        this.climas = climas;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Pronostico{" +
                "cnt=" + cnt +
                ", climas=" + Arrays.toString(climas) +
                ", ciudad=" + ciudad +
                '}';
    }
}
