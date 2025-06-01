package Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.Creadores;
import com.example.mymusic.R;

import java.util.List;

import Global.Info;
import POJO.nombreTabla;

public class adaptadorTablaVista extends RecyclerView.Adapter<adaptadorTablaVista.MiniActivity> {
    public Context context;

    @NonNull
    @Override
    public adaptadorTablaVista.MiniActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tablas_vista,parent,false);
        return new MiniActivity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorTablaVista.MiniActivity holder, int position) {
        final int pos = position;
        holder.titulo.setText(Info.listaPlaylists.get(pos).getNombrePlaylist());
        holder.fecha.setText(Info.listaPlaylists.get(pos).getFechaCreacion());
        holder.seleccionarPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al seleccionar una tabla ir a su activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return Info.listaPlaylists.size();
    }
    public class MiniActivity extends RecyclerView.ViewHolder{
        TextView titulo, fecha;
        ImageView portada;
        LinearLayout seleccionarPlaylist;
        public MiniActivity(@NonNull View itemView){
            super(itemView);
            portada = itemView.findViewById(R.id.playlist_image);
            titulo = itemView.findViewById(R.id.titulo_play);
            fecha = itemView.findViewById(R.id.fecha);
            seleccionarPlaylist = itemView.findViewById(R.id.seleccionarPlaylist);
        }
    }
}
