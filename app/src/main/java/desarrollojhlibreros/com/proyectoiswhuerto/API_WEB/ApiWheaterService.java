package desarrollojhlibreros.com.proyectoiswhuerto.API_WEB;

import com.google.gson.JsonObject;

import desarrollojhlibreros.com.proyectoiswhuerto.Modelo.Clima;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiWheaterService {

    @GET("weather")
    Call<Clima> getClimaPosicionActual(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String key);




}
