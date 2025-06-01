package com.example.mymusic;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerInfoCancion extends AppCompatActivity {
    ImageView imagenCancion, play_pause, next, before;
    TextView nombreCancion, nombreArtista, tiempoAct, tiempoTotal;
    SeekBar seekBar;

    private Handler handler;
    private Runnable actualizador;
    private boolean reproduciendo = true;
    private long inicio;
    private long tiempoPausado = 0;
    private long duracionTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_info_cancion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imagenCancion = findViewById(R.id.album_image);
        play_pause = findViewById(R.id.play_pause);
        next = findViewById(R.id.avanzar_lista);
        before = findViewById(R.id.retroceder_lista);
        nombreCancion = findViewById(R.id.titulo_cancion);
        nombreArtista = findViewById(R.id.nombre_Artista);
        tiempoAct = findViewById(R.id.tiempo_llevado);
        tiempoTotal = findViewById(R.id.tiempo_restante);
        seekBar = findViewById(R.id.progress_song);
        play_pause.setImageResource(android.R.drawable.ic_media_pause);

        colocarDatosArtista();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tiempoAct.setText(formatearTiempo(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pausar mientras se arrastra
                reproduciendo = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Cuando se suelta, actualizar tiempos
                tiempoPausado = seekBar.getProgress();
                inicio = System.currentTimeMillis();
                reproduciendo = true;
                handler.post(actualizador);
            }
        });
    }

    private void colocarDatosArtista() {
        nombreCancion.setText(getIntent().getStringExtra("titulo"));
        nombreArtista.setText(getIntent().getStringExtra("artista"));
        tiempoTotal.setText(getIntent().getStringExtra("tiempo"));

        duracionTotal = tiempoStringAMillis(tiempoTotal.getText().toString());
        seekBar.setMax((int) duracionTotal);

        inicio = System.currentTimeMillis();
        handler = new Handler();

        actualizador = new Runnable() {
            @Override
            public void run() {
                if (reproduciendo) {
                    long ahora = System.currentTimeMillis();
                    long transcurrido = (ahora - inicio) + tiempoPausado;

                    if (transcurrido <= duracionTotal) {
                        seekBar.setProgress((int) transcurrido);
                        tiempoAct.setText(formatearTiempo(transcurrido));
                        handler.postDelayed(this, 1000);
                    } else {
                        seekBar.setProgress((int) duracionTotal);
                        tiempoAct.setText(formatearTiempo(duracionTotal));
                        reproduciendo = false;
                    }
                }
            }
        };

        handler.post(actualizador);

        // Botón play/pause
        play_pause.setOnClickListener(v -> {
            if (reproduciendo) {
                // Pausar
                reproduciendo = false;
                tiempoPausado += System.currentTimeMillis() - inicio;
                play_pause.setImageResource(android.R.drawable.ic_media_play); // Cambia el ícono
            } else {
                // Reanudar
                inicio = System.currentTimeMillis();
                reproduciendo = true;
                handler.post(actualizador);
                play_pause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });
    }

    public long tiempoStringAMillis(String tiempo) {
        String[] partes = tiempo.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        int segundos = Integer.parseInt(partes[2]);
        return (horas * 3600 + minutos * 60 + segundos) * 1000L;
    }
    private String formatearTiempo(long millis) {
        int segundos = (int) (millis / 1000) % 60;
        int minutos = (int) ((millis / (1000 * 60)) % 60);
        int horas = (int) ((millis / (1000 * 60 * 60)));

        if (horas > 0) {
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%02d:%02d", minutos, segundos);
        }
    }

}