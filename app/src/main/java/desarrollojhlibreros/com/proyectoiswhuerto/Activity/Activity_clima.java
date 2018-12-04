package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.Manifest;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.APIWEB;
import desarrollojhlibreros.com.proyectoiswhuerto.API_WEB.ApiWheaterService;
import desarrollojhlibreros.com.proyectoiswhuerto.Modelo.Clima;
import desarrollojhlibreros.com.proyectoiswhuerto.Modelo.Pronostico;
import desarrollojhlibreros.com.proyectoiswhuerto.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_clima extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient apiClient;
    private int PETICION_PERMISO_LOCALIZACION = 34;
    private GraphView grafico;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clima);

        grafico = (GraphView) findViewById(R.id.bar_chart);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Date date = new Date();
        String strDateFormat = "dd-MMM-aaaaa";
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
        TextView fechaActual = (TextView) findViewById(R.id.lblFechaActual);
        fechaActual.setText(objSDF.format(date));
    }

    LocationCallback locationCallback=new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(locationResult==null){
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if(location!=null)
                    obtenerLocalizacion(location);
                else
                    Log.e("NULL","2.-SIN INSTANCIA");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        this.mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private  void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        obtenerLocalizacion(location);
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERRO--->", "No se pudo obtener la geolocalizacion");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void obtenerLocalizacion(Location location) {
        ApiWheaterService service = APIWEB.getApi().create(ApiWheaterService.class);
        Call<Clima> requestaClima = service.getClimaPosicionActual(location.getLatitude(), location.getLongitude(), APIWEB.APIKEY);
        requestaClima.enqueue(new Callback<Clima>() {
            @Override
            public void onResponse(Call<Clima> call, Response<Clima> response) {
                if (response.isSuccessful()) {
                    Clima objetoClima = response.body();
                    setDatosClimaCardView(objetoClima);
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), "problema en el dato obtenido:" + response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Clima> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sin conexion a internet u otro medio ", Toast.LENGTH_LONG).show();
            }
        });

        Call<Pronostico> requestPronostico = service.getClimaHistorical(location.getLatitude(), location.getLatitude(), APIWEB.APIKEY);
        requestPronostico.enqueue(new Callback<Pronostico>() {
            @Override
            public void onResponse(Call<Pronostico> call, Response<Pronostico> response) {
                if (response.isSuccessful()) {

                    Pronostico pronostico = response.body();

                    TextView localizacion = (TextView) findViewById(R.id.lugarClima);
                    localizacion.setText("Pronostico clima: " + pronostico.getCiudad().getName());

                    DataPoint[] entries = new DataPoint[pronostico.getClimas().length];

                    for (int i = 0; i < pronostico.getClimas().length; i++) {
                        Clima clima = pronostico.getClimas()[i];
                        String hora = clima.getDt_txt().substring(clima.getDt_txt().indexOf(" ") + 1, clima.getDt_txt().indexOf(" ") + 2);
                        DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                        try {
                            Date date = (Date) formatter.parse(hora);
                            entries[i] = new DataPoint((int) clima.getMa().getTemp_max(), Integer.parseInt(hora));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(entries);
                    grafico.addSeries(series);

                } else {
                    try {
                        Toast.makeText(getApplicationContext(), "problema en el dato obtenido:" + response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Pronostico> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sin conexion a internet/sin otro medio disponible", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("COnexion:", "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
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
