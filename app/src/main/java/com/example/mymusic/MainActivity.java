package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adaptadores.adaptadorTablaVista;
import Adaptadores.adaptadorVerCancion;
import Global.Info;
import POJO.Cancion;
import POJO.Playlist;
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
        nombrarPlaylist();
    }

    private void nombrarTablas() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String baseUrl = getResources().getString(R.string.base_url);
        String url = baseUrl + "nombreTablas.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            // Llena el arrayList nombeTablas de cada nombew de tabla
                            String nombre = response.getString(i);
                            nombreTabla titulo = new nombreTabla();
                            titulo.setNombre(nombre);
                            Info.nombreTablas.add(titulo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Establece ViewHolder del recycler view
                    adaptadorTablaVista adapter = new adaptadorTablaVista();
                    adapter.context = this;
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(linearLayoutManager);
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);

    }

    private void nombrarPlaylist() {
        RequestQueue queue = Volley.newRequestQueue(this);
        int id = archivo.getInt("id_usuario", 0);
        String baseUrl = getResources().getString(R.string.base_url);
        String url = baseUrl + "verPlaylists.php?usr=" + id;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Info.listaPlaylists.clear(); // Limpia la lista anterior
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONArray item = response.getJSONArray(i);
                                Playlist playlist = new Playlist();
                                playlist.setPlaylist(item.getInt(0));           // ID_playlist
                                playlist.setNombrePlaylist(item.getString(2));  // Nombre
                                playlist.setFechaCreacion(item.getString(3));   // Fecha_creacion

                                Info.listaPlaylists.add(playlist);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error al leer la playlist", Toast.LENGTH_SHORT).show();
                        }
                        // Establece ViewHolder del recycler view
                        adaptadorTablaVista adapter = new adaptadorTablaVista();
                        adapter.context = MainActivity.this;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Toast.makeText(MainActivity.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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

            /*
            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);*/

            Intent aVer = new Intent(this, VerCancion.class);
            startActivity(aVer);
        } else if(item.getItemId() == R.id.opc_modificar) {

            Intent aMod = new Intent(this, Modificar.class);
            startActivity(aMod);

        } else if(item.getItemId() == R.id.opc_eliminar) {

            Intent aElim = new Intent(this, Eliminar.class);
            startActivity(aElim);

        } else if(item.getItemId() == R.id.opc_logout) {

            // Si el usuario existe, lo borra de shared preferences y regres a a inicio
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