package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.APIWEB;
import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.ApiWheaterService;
import desarrollojhlibreros.com.proyectoiswhuerto.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Activity_clima extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private GoogleApiClient apiClient;
    private int PETICION_PERMISO_LOCALIZACION=34;
    private ApiWheaterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        TextView fechaActual=(TextView) findViewById(R.id.lblFechaActual);
        service = APIWEB.getApi().create(ApiWheaterService.class);

        Date objDate = new Date();
        String strDateFormat = "dd-MMM-aaaa";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        fechaActual.setText(objSDF.format(objDate));

        getPositionNow();
    }

    private void getPositionNow() {
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERRO--->", "No se pudo obtener la geolocalizacion");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        }else {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            getClimaActual(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("COnexion:", "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);
                getClimaActual(lastLocation);
            } else {
                Log.e("ERROR->", "Permiso denegado");
            }
        }
    }


    private void getClimaActual(Location location){
       Call<JsonObject> objectWheater= service.getClimaPosicionActual(location.getLatitude(),location.getLongitude(),APIWEB.APIKEY);
       objectWheater.enqueue(new Callback<JsonObject>() {
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject objectBody = response.body();
                    JsonArray arrayObject= objectBody.getAsJsonArray("weather");
                    JsonElement climaHoy=arrayObject.get(0);
                }
           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"No se pudo conectar al servidor, intentelo mas tarde o verfique su conexion",Toast.LENGTH_LONG).show();
           }

       });
    }

}
