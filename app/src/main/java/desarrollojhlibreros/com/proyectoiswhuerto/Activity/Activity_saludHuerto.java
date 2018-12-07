package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;
import android.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import android.os.Handler;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_saludHuerto extends AppCompatActivity {

    private String nombre, direccionMAC;
    private BluetoothAdapter adaptadorBluetooth=BluetoothAdapter.getDefaultAdapter();//Obtiene el adaptador Bluetooth que tiene el sistema por defecto
    private Boolean comprobacionBluetooth=false;
    private BluetoothSocket btsocket=null;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThread mConnectedThread;
    Handler h;

    private final String TAG="Arduino";
    final int RECIEVE_MESSAGE = 1;        // Status  for Handler


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludhuerto);
        comprobarBluetooh();
    }

    private void comprobarBluetooh(){//Función que comprueba posibles errores con el bluetooth
        if(adaptadorBluetooth==null){//Este IF comprueba si el adaptador Bluetooth del teléfono funciona
            Toast mensaje=Toast.makeText(getApplicationContext(),"No podemos conectar con el Bluetooth de tu dispositivo D:", Toast.LENGTH_SHORT);
            mensaje.show();
        }
        else{//Si funciona comprueba que esté encendido
            if(adaptadorBluetooth.isEnabled()==false){//Si no está encendido manda un mensaje para que el usuario lo active
                Intent peticionActivarBT=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//Crea una notificación para que se active el bluetooth
                startActivityForResult(peticionActivarBT, 1);//Activa la notificación anteriormente creada y le da un requestCode de 1
            }
            else{
                verDispositivosVinculados();
            }
        }
    }

    private void verDispositivosVinculados(){
        Set<BluetoothDevice> dispositivosVinculados=adaptadorBluetooth.getBondedDevices();//Junta en una colección (Set) todos los dispositivos Bluetooth vinculados al dispositivo
        int contador=0;

        if(dispositivosVinculados.size()>0){
            String deviceHardwareAddress = null;
            
            for (BluetoothDevice dispositivo : dispositivosVinculados){//Este for each recorre toda la colección de dispositivos vinculados buscando el nombre de dispositivo que le especificamos
                String deviceName = dispositivo.getName();

                if(deviceName.equals("HC-05")){//Aquí especificamos el nombre del dispositivo bluetooth que necesitamos encontrar
                    deviceHardwareAddress = dispositivo.getAddress(); // Y si lo encontramos obtenemos su MAC address
                    break;
                }
                else{
                    contador++;//Cuenta los dispositivos que están vinculados al dispositivo
                }
            }

            if(contador==dispositivosVinculados.size()){
                Toast mensaje=Toast.makeText(getApplicationContext(),"Debes de vincular el dispositivo a tu celular antes de entrar a esta función!", Toast.LENGTH_LONG);
                mensaje.show();
                onBackPressed();
            }
            else{
                crearConexion(deviceHardwareAddress);
            }
        }
        else{
            Toast mensaje=Toast.makeText(getApplicationContext(),"Debes de vincular el dispositivo a tu celular antes de entrar a esta función!", Toast.LENGTH_LONG);
            mensaje.show();
            onBackPressed();
        }
    }

    private void crearConexion(String direccionMAC){
        BluetoothDevice arduino=adaptadorBluetooth.getRemoteDevice(direccionMAC);

        try{
            final Method m;
            m=arduino.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
            btsocket=(BluetoothSocket) m.invoke(arduino,MY_UUID);
        }catch (NoSuchMethodException e) {
            Toast mensaje=Toast.makeText(getApplicationContext(),"Error al crear el socket", Toast.LENGTH_LONG);
            mensaje.show();
            onBackPressed();
        } catch (IllegalAccessException e) {
            Toast mensaje=Toast.makeText(getApplicationContext(),"Error al crear el socket", Toast.LENGTH_LONG);
            mensaje.show();
            onBackPressed();
        } catch (InvocationTargetException e) {
            Toast mensaje=Toast.makeText(getApplicationContext(),"Error al crear el socket", Toast.LENGTH_LONG);
            mensaje.show();
            onBackPressed();
        }
        adaptadorBluetooth.cancelDiscovery();
        Log.d(TAG, "...Connecting...");
        try {
            btsocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btsocket.close();
            } catch (IOException e2) {
                errorExit(TAG, "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        Log.d("Arduino", "...Create Socket...");

        mConnectedThread = new ConnectedThread(btsocket);
        mConnectedThread.start();
    }
        /*TextView humedad=findViewById(R.id.humedad);

        humedad.setText("Huevos :v");*/

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {//Sobreescribimos el método onActivityResult para comprobar si se activó el Bluetooth en caso de que no lo esté ya

        switch(requestCode){
            case 1:
                if (resultCode == RESULT_CANCELED) {//RESULT_CANCELED nos dice que el bluetooth no se activó
                    onBackPressed();
                }
                else{
                    verDispositivosVinculados();
                }
                break;
            default:
                break;
        }

    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}