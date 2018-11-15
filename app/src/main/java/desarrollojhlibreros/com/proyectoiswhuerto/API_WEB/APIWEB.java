package desarrollojhlibreros.com.proyectoiswhuerto.API_WEB;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIWEB {

    public static  Retrofit retrofit;
    public static  final String APIKEY="83f4b07a7e27629198760cf8b2c3e546";
    public static final String ULRBASE="api.openweathermap.org/data/2.5/";


    public static Retrofit getApiWebInstancia(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ULRBASE)
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit;
        }

        return retrofit;
    }

}
