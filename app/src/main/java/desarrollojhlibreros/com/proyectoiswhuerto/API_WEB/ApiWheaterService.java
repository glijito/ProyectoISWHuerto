package desarrollojhlibreros.com.proyectoiswhuerto.API_WEB;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiWheaterService {

    @GET("weather")
    Call<JsonObject> getClimaPosicionActual(@Query("lat") double lat, @Query("lon") double lon,@Query("appid") String key);




}
