package com.example.mymusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class VerInfoCancion extends AppCompatActivity {
    ImageView imagenCancion, play_pause, next, before, regresar;
    TextView nombreCancion, nombreArtista, tiempoAct, tiempoTotal;
    SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();
    private Runnable actualizadorSeekBar;

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
        regresar = findViewById(R.id.regresar);
        play_pause.setImageResource(android.R.drawable.ic_media_pause);
        colocarDatosArtista();
        reproducirMusica();

        play_pause.setOnClickListener(v -> reproducirMusica());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tiempoAct.setText(formatearTiempo(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(actualizadorSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && isPlaying) {
                    handler.post(actualizadorSeekBar);
                }
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresar = new Intent(VerInfoCancion.this, VerCancion.class);
                startActivity(regresar);
            }
        });
    }
    private void reproducirMusica() {
        if (mediaPlayer == null) {
            String baseUrl = getResources().getString(R.string.base_url);
            String urlCancion = baseUrl + getIntent().getStringExtra("cancion");
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(urlCancion);
                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    isPlaying = true;
                    play_pause.setImageResource(android.R.drawable.ic_media_pause);
                    configurarSeekBar();
                });
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnCompletionListener(mp -> {
                    isPlaying = false;
                    play_pause.setImageResource(android.R.drawable.ic_media_play);
                    seekBar.setProgress(seekBar.getMax());
                    handler.removeCallbacks(actualizadorSeekBar);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
                play_pause.setImageResource(android.R.drawable.ic_media_play);
                handler.removeCallbacks(actualizadorSeekBar);
            } else {
                mediaPlayer.start();
                isPlaying = true;
                play_pause.setImageResource(android.R.drawable.ic_media_pause);
                handler.post(actualizadorSeekBar);
            }
        }
    }

    private void configurarSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());

        actualizadorSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && isPlaying) {
                    int posicion = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(posicion);
                    tiempoAct.setText(formatearTiempo(posicion));
                    tiempoTotal.setText(formatearTiempo(mediaPlayer.getDuration()));
                    handler.postDelayed(this, 500); // actualiza cada medio segundo
                }
            }
        };
        handler.post(actualizadorSeekBar);
    }


    private void colocarDatosArtista() {
        nombreCancion.setText(getIntent().getStringExtra("titulo"));
        nombreArtista.setText(getIntent().getStringExtra("artista"));
        tiempoTotal.setText(getIntent().getStringExtra("tiempo"));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(actualizadorSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}