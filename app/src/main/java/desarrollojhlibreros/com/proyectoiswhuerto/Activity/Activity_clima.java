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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.APIWEB;
import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.ApiWheaterService;
import desarrollojhlibreros.com.proyectoiswhuerto.Modelo.Clima;
import desarrollojhlibreros.com.proyectoiswhuerto.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_clima extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    
    private GoogleApiClient apiClient;
    private  int PETICION_PERMISO_LOCALIZACION=34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        Date date = new Date();
        String strDateFormat = "dd-MMM-aaaa";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

        TextView fechaActual =(TextView) findViewById(R.id.lblFechaActual);
        fechaActual.setText(objSDF.format(date));

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
        }

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Log.e("LATITUD", "onConnected: "+lastLocation.getLatitude());
        Log.e("LONGITUD", "onConnected: "+lastLocation.getLongitude());
        ApiWheaterService service = APIWEB.getApi().create(ApiWheaterService.class);

        Call<Clima> requestaClima=service.getClimaPosicionActual(lastLocation.getLatitude(),lastLocation.getLongitude(),APIWEB.APIKEY);
        requestaClima.enqueue(new Callback<Clima>() {
            @Override
            public void onResponse(Call<Clima> call, Response<Clima> response) {

                if(response.isSuccessful()){
                    Clima objetoClima=response.body();
                    setDatosClimaCardView(objetoClima);
                }else {
                    try {
                        Toast.makeText(getApplicationContext(),"problema en el dato obtenido:"+response.errorBody().string(),Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Clima> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Sin conexion a internet u otro medio ",Toast.LENGTH_LONG).show();
            }
        });
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

                //Permiso concedido
                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);
                Log.e("LATITUD", "onConnected: "+lastLocation.getLatitude());
                Log.e("LONGITUD", "onConnected: "+lastLocation.getLongitude());
            } else {
                Log.e("ERROR->", "Permiso denegado");
            }
        }
    }

    private void setDatosClimaCardView(Clima climaCardView){

        ImageView imageClima=(ImageView) findViewById(R.id.imageClima);
        TextView nubosidad=(TextView) findViewById(R.id.idNubes);
        TextView temMaxima=(TextView) findViewById(R.id.idTempMaxima);
        TextView temMinima=(TextView) findViewById(R.id.idTempMinima);

        Glide.with(this).load("http://openweathermap.org/img/w/"+climaCardView.getWh()[0].getIcon()+".png").into(imageClima);
        nubosidad.setText("Nubes:"+climaCardView.getCl().getAll()+" %");
        temMaxima.setText("temperatura maxima:"+climaCardView.getMa().getTemp_max());
        temMinima.setText("temperatura minima:"+climaCardView.getMa().getTemp_min());
    }
}
