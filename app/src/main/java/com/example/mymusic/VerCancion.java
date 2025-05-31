package com.example.mymusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adaptadores.adaptadorVerCancion;
import Global.Info;
import POJO.Cancion;

public class VerCancion extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // RecyclerView Para mostrar tablas
        recyclerView = findViewById(R.id.recyclerview);

        nombrarCanciones();     // Llena el arrayList de la información de cada cancion;

        // Llena recycler view
        adaptadorVerCancion miAdp = new adaptadorVerCancion();
        miAdp.context = this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(miAdp);
    }

    private void nombrarCanciones() {
        // Busca las canciones en el servidor y llena el arrayList "listaCanciones" de los elementos obtenidos;

        String url = "https://b724-2806-2f0-56c0-fe66-f42e-6d86-b37a-d551.ngrok-free.app/Frecuency/nombreTablas.php";

        // Recibe un arreglo de objetos json
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
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
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerCancion.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}