package Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.VerInfoCancion;

import Global.Info;

public class adaptadorVerCancion extends RecyclerView.Adapter<adaptadorVerCancion.MiniActivity> {
    public Context context;
    
    @NonNull
    @Override
    public adaptadorVerCancion.MiniActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myVer = View.inflate(context, R.layout.view_holder_vercancion, null);
        adaptadorVerCancion.MiniActivity miniMini = new adaptadorVerCancion.MiniActivity(myVer);
        return miniMini;
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorVerCancion.MiniActivity holder, int position) {

        final int pos = position;
        holder.txtvSongTitle.setText(Info.listaCanciones.get(position).getTitulo());
        holder.txtvSongArtist.setText(Info.listaCanciones.get(position).getArtista());
        holder.txtvDuration.setText(Info.listaCanciones.get(position).getDuracion());
        holder.clicCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verCancion = new Intent(context, VerInfoCancion.class);
                verCancion.putExtra("titulo", Info.listaCanciones.get(pos).getTitulo());
                verCancion.putExtra("artista", Info.listaCanciones.get(pos).getArtista());
                verCancion.putExtra("tiempo", Info.listaCanciones.get(pos).getDuracion());
                verCancion.putExtra("cancion", Info.listaCanciones.get(pos).getMusica());
                context.startActivity(verCancion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Info.listaCanciones.size();
    }

    public class MiniActivity extends RecyclerView.ViewHolder {
        TextView txtvSongTitle, txtvSongArtist, txtvDuration;
        LinearLayout clicCancion;
        //ImageView album;

        public MiniActivity(@NonNull View itemView) {
            super(itemView);
            txtvSongTitle = (TextView) itemView.findViewById(R.id.txtv_tituloCancion);
            txtvSongArtist = (TextView) itemView.findViewById(R.id.txtv_artistaCancion);
            txtvDuration = (TextView) itemView.findViewById(R.id.txtv_duracion);
            clicCancion = (LinearLayout) itemView.findViewById(R.id.seleccionarCancion);
        }
    }
}
