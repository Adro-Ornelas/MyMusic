package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.Principal;

public class Ver extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Asigna id a toolbar y activa
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
    }

    // Inflar options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Opciones del menu (navega entre activities o logout)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.opc_principal){

            Intent aPrc = new Intent(this, MainActivity.class);
            startActivity(aPrc);

        } else if(item.getItemId() == R.id.opc_ver){

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.opc_modificar) {

            Intent aMod = new Intent(this, Modificar.class);
            startActivity(aMod);

        } else if(item.getItemId() == R.id.opc_eliminar) {

            Intent aElim = new Intent(this, Eliminar.class);
            startActivity(aElim);

        } else if(item.getItemId() == R.id.opc_logout) {
            if(archivo.contains("id_usuario")){
                Intent cerrar = new Intent(this, Inicio.class);
                SharedPreferences.Editor editor = archivo.edit();
                editor.remove("id_usuario");
                editor.apply();
                startActivity(cerrar);
                finish();
            }
        } else if(item.getItemId() == R.id.opc_creadores) {

            Intent aCrea = new Intent(this, Creadores.class);
            startActivity(aCrea);

        } else if(item.getItemId() == R.id.opc_contactos) {

            Intent aCont = new Intent(this, Contactos.class);
            startActivity(aCont);

        } else if (item.getItemId() == R.id.perfil){
            Intent perfil = new Intent(this, Perfil.class);
            startActivity(perfil);
        }
        return super.onOptionsItemSelected(item);
    }
}