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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_clima extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private GoogleApiClient apiClient;
    private  int PETICION_PERMISO_LOCALIZACION=34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);
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
            Log.e("LATITUD", "onConnected: "+lastLocation.getLatitude());
            Log.e("LONGITUD", "onConnected: "+lastLocation.getLongitude());
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
}
