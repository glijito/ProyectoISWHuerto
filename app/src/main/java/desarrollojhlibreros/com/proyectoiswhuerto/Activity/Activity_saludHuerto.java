package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Set;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_saludHuerto extends AppCompatActivity {

    private String nombre, direccionMAC;
    private BluetoothAdapter adaptadorBluetooth=BluetoothAdapter.getDefaultAdapter();//Obtiene el adaptador Bluetooth que tiene el sistema por defecto
    private Boolean comprobacionBluetooth=false;

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

                if(deviceName.equals("LG")){//Aquí especificamos el nombre del dispositivo bluetooth que necesitamos encontrar
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
                Toast mensaje=Toast.makeText(getApplicationContext(),"La MAC del dispositivo es: "+deviceHardwareAddress, Toast.LENGTH_SHORT);
                mensaje.show();
            }
        }
        else{
            Toast mensaje=Toast.makeText(getApplicationContext(),"Debes de vincular el dispositivo a tu celular antes de entrar a esta función!", Toast.LENGTH_LONG);
            mensaje.show();
            onBackPressed();
        }
    }

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

}
