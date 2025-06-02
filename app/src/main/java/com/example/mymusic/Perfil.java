package com.example.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Perfil extends AppCompatActivity {
    ImageView regresar;
    Button editar, eliminar;
    TextView nombre, apeP, apeM, correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        regresar = findViewById(R.id.regresar);
        nombre = findViewById(R.id.nombre);
        apeP = findViewById(R.id.apellidoP);
        apeM = findViewById(R.id.apellidoM);
        correo = findViewById(R.id.email);
        editar = findViewById(R.id.editar);
        eliminar = findViewById(R.id.eliminar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regresar = new Intent(Perfil.this, MainActivity.class);
                startActivity(regresar);
            }
        });
    }
}