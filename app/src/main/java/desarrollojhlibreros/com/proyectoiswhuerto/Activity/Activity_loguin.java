package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_loguin extends AppCompatActivity {

    private Button btnContinuar;
    private EditText user;
    private EditText telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);

        btnContinuar=(Button) findViewById(R.id.btnContinuar);
        user=(EditText) findViewById(R.id.txtUsers);
        telefono=(EditText) findViewById(R.id.txtTelefono);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),Activity_inicio.class));
            }
        });
    }
}
