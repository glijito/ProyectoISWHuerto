package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import desarrollojhlibreros.com.proyectoiswhuerto.Adaptadores.RecyclerViewChooserAdapter;
import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_choosePlant extends AppCompatActivity {

    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseplantas);

        ArrayList<Integer> images=new ArrayList<>();
        for (int i=0;i<10;i++)
            images.add(R.drawable.descuentos);

        initRecyclerViewPlant(R.id.recyclerPlant,images,R.layout.listchoose_plant);
        btnContinuar=(Button)findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),Activity_inicio.class));
            }
        });
    }


    private void initRecyclerViewPlant(int reciclerIdentificador, ArrayList<Integer> images, int cardViewID ){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = findViewById(reciclerIdentificador);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewChooserAdapter(images,this,cardViewID));
    }
}
