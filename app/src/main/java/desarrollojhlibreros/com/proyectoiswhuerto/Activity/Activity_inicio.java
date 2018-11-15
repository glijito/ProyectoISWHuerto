package desarrollojhlibreros.com.proyectoiswhuerto.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class Activity_inicio extends AppCompatActivity {

    private ImageButton btnSaludHuerto;
    private ImageButton btnClima;
    private ImageButton btnPlanta;
    private CarouselView imageSlider;
    private int carruselImages[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnClima=(ImageButton)findViewById(R.id.btnClima);
        btnPlanta=(ImageButton)findViewById(R.id.btnPlanta);
        btnSaludHuerto=(ImageButton)findViewById(R.id.btnSaludHuerto);
        imageSlider=(CarouselView)findViewById(R.id.imageSlider);

        setterCarrusel();

        btnClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),Activity_clima.class));
            }
        });

        btnSaludHuerto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),Activity_saludHuerto.class));
            }
        });
    }

    private void setterCarrusel(){
        carruselImages= new int[3];
        carruselImages[0]=R.drawable.dato1;
        carruselImages[1]=R.drawable.dato2;
        carruselImages[2]=R.drawable.dato3;
        imageSlider.setPageCount(carruselImages.length);
        imageSlider.setImageListener(imageListener);
    }

    ImageListener imageListener= new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(getApplication()).load(carruselImages[position]).into(imageView);
        }
    };
}
