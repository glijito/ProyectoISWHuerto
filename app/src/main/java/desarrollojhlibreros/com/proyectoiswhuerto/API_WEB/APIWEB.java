package desarrollojhlibreros.com.proyectoiswhuerto.API_WEB;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIWEB {

    public static  Retrofit retrofit;
    public static  final String APIKEY="83f4b07a7e27629198760cf8b2c3e546";
    public static final String ULRBASE="api.openweathermap.org/data/2.5/";



    public static  Retrofit getApi(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
        httpclient.addInterceptor(logging);

        if(retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(ULRBASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpclient.build())
                    .build();
        }
        return retrofit;
    }

}
