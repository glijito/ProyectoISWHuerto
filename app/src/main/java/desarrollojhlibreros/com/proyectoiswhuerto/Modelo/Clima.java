package desarrollojhlibreros.com.proyectoiswhuerto.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Clima {

    @Expose
    @SerializedName("coord") private coord co;
    @Expose
    @SerializedName("weather") private weather wh[];
    @Expose
    @SerializedName("main") private main ma;
    @Expose
    @SerializedName("wind") private wind wd;
    @Expose
    @SerializedName("clouds") private clouds cl;

    public class coord{

        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        @Override
        public String toString() {
            return "coord{" +
                    "lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }
    }

    public class weather{

        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "weather{" +
                    "id=" + id +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }


    public class main{

        private double temp;
        private int humidity;
        private int pressure;
        private double temp_min;
        private double temp_max;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        @Override
        public String toString() {
            return "main{" +
                    "temp=" + temp +
                    ", humidity=" + humidity +
                    ", pressure=" + pressure +
                    ", temp_min=" + temp_min +
                    ", temp_max=" + temp_max +
                    '}';
        }
    }

    public class clouds{
        private int all;

        public int getAll() {
            return all;
        }

        @Override
        public String toString() {
            return "clouds{" +
                    "all=" + all +
                    '}';
        }
    }

    public class wind{

        private double speed;

        public double getSpeed() {
            return speed;
        }

        @Override
        public String toString() {
            return "wind{" +
                    "speed=" + speed +
                    '}';
        }
    }

    public coord getCo() {
        return co;
    }

    public weather[] getWh() {
        return wh;
    }

    public main getMa() {
        return ma;
    }

    public wind getWd() {
        return wd;
    }

    public clouds getCl() {
        return cl;
    }

    @Override
    public String toString() {
        return "Clima{" +
                "co=" + co +
                ", wh=" + Arrays.toString(wh) +
                ", ma=" + ma +
                ", wd=" + wd +
                ", cl=" + cl +
                '}';
    }
}
