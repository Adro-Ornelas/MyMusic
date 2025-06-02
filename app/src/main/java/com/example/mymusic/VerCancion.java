package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import Adaptadores.adaptadorTablaVista;
import Adaptadores.adaptadorVerCancion;
import Global.Info;
import POJO.Album;
import POJO.Cancion;

public class VerCancion extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences archivo;
    private boolean cancionesCargadas = false;
    private boolean albumsCargados = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_cancion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Aignar y soportar toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView Para mostrar tablas
        recyclerView = findViewById(R.id.rv_VerCancion);
        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        nombrarCanciones();     // Llena el arrayList de la información de cada cancion;
        nombrarAlbums();
    }

    private void nombrarCanciones() {
        // Busca las canciones en el servidor y llena el arrayList "listaCanciones" de los elementos obtenidos;

        String baseUrl = getResources().getString(R.string.base_url);
        String url = baseUrl + "nombreCanciones.php";

        // Recibe un arreglo de objetos json
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Info.listaCanciones.clear();
                            for(int i = 0; i < response.length(); ++i){

                                JSONObject objCancion = response.getJSONObject(i);
                                Cancion newCancion = new Cancion();
                                newCancion.setId_cancion(Integer.parseInt(objCancion.getString("id_cancion")));
                                newCancion.setId_album(Integer.parseInt(objCancion.getString("id_album")));
                                newCancion.setId_genero(Integer.parseInt(objCancion.getString("id_genero")));
                                newCancion.setTitulo(objCancion.getString("titulo"));
                                newCancion.setArtista(objCancion.getString("artista"));
                                newCancion.setDuracion(objCancion.getString("duracion"));
                                newCancion.setFecha(objCancion.getString("fecha"));
                                newCancion.setMusica(objCancion.getString("audio"));

                                Info.listaCanciones.add(newCancion);
                            }
                            cancionesCargadas = true;
                            verificarYCrearAdaptador();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        // Establece ViewHolder del recycler view
                        adaptadorVerCancion adapter = new adaptadorVerCancion();
                        adapter.context = VerCancion.this;
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VerCancion.this,
                                LinearLayoutManager.VERTICAL, false);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerCancion.this, "E:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void nombrarAlbums() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String baseUrl = getResources().getString(R.string.base_url);
        String url = baseUrl + "verAlbum.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Info.listaAlbums.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONArray item = response.getJSONArray(i);
                                Album album = new Album();
                                album.setID_album(item.getInt(0));           // ID_playlist
                                album.setID_discografia(item.getInt(1));  // ID_discografia
                                album.setTituloAlbum(item.getString(2));   // Titulo Album
                                album.setFechaAlbum(item.getString(3));   // Fecha Album
                                album.setPortadaAlbum(item.getString(4)); // Ubicacion portada
                                Info.listaAlbums.add(album);
                            }
                            albumsCargados = true;
                            verificarYCrearAdaptador();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VerCancion.this, "Error al leer la playlist", Toast.LENGTH_SHORT).show();
                        }
                        // Establece ViewHolder del recycler view

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        Toast.makeText(VerCancion.this, "Error en la conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

            Intent aPrc = new Intent(this, MainActivity.class);
            startActivity(aPrc);
        } else if(item.getItemId() == R.id.opc_ver){

            Toast.makeText(this, "Ya se encuentra aquí.", Toast.LENGTH_SHORT).show();
            /*
            Intent aVer = new Intent(this, Ver.class);
            startActivity(aVer);*/

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

        }else if (item.getItemId() == R.id.perfil){
            Intent perfil = new Intent(this, Perfil.class);
            startActivity(perfil);
        }
        return super.onOptionsItemSelected(item);
    }
    private void verificarYCrearAdaptador() {
        if (cancionesCargadas && albumsCargados) {
            adaptadorVerCancion adapter = new adaptadorVerCancion();
            adapter.context = VerCancion.this;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

}
