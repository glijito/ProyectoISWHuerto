package desarrollojhlibreros.com.proyectoiswhuerto.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import desarrollojhlibreros.com.proyectoiswhuerto.R;

public class RecyclerViewChooserAdapter extends RecyclerView.Adapter<RecyclerViewChooserAdapter.ViewHolder> {

    private ArrayList<Integer> images;
    private Context context;
    private int carviewerId;
    private Activity activityMainContainer;

    public RecyclerViewChooserAdapter(ArrayList<Integer> images, Activity context, int carviewerId) {
        this.images = images;
        this.context = context;
        this.carviewerId=carviewerId;
        this.activityMainContainer=context;
    }


    public RecyclerViewChooserAdapter(){
        this(null,null,0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(carviewerId,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            Glide.with(this.context).load(images.get(position)).into(holder.imagen);
            holder.nombrePlanta.setText("Planta: "+position);
        }catch(Throwable throwable){
            Toast.makeText(context,"Error en la carga del layout: LIST INICIO:  "+throwable.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        CheckBox choice;
        TextView nombrePlanta;

        public ViewHolder(View itemView){
            super(itemView);
            imagen=(ImageView) itemView.findViewById(R.id.imagePlant);
            choice=(CheckBox) itemView.findViewById(R.id.chkChoose);
            nombrePlanta=(TextView) itemView.findViewById(R.id.lblNamePlant);
        }
    }
}



