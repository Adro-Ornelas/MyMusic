package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import Adaptadores.adaptadorTablaVista;
import Global.Info;
import POJO.nombreTabla;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    SharedPreferences archivo;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        // RecyclerView Para mostrar tablas
        recyclerView = findViewById(R.id.recyclerview);
        nombrarTablas();
    }

    private void nombrarTablas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.133/Frecuency/nombreTablas.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String nombre = response.getString(i);
                            nombreTabla titulo = new nombreTabla();
                            titulo.setNombre(nombre);
                            Info.nombreTablas.add(titulo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adaptadorTablaVista adapter = new adaptadorTablaVista();
                    adapter.context = this;
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(linearLayoutManager);
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);

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

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.opc_ver){

            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);

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

        }
        return super.onOptionsItemSelected(item);
    }
}