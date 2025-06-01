package com.example.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarUsuario extends AppCompatActivity {
    TextView iniciarSesion;
    EditText nombre, apellidoP, apellidoM, email, pass;
    Button registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        iniciarSesion = findViewById(R.id.iniciarSesion);
        nombre = findViewById(R.id.nombre);
        apellidoP = findViewById(R.id.apellidoP);
        apellidoM = findViewById(R.id.apellidoM);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        registrar = findViewById(R.id.registrar);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regreso = new Intent(RegistrarUsuario.this, Inicio.class);
                startActivity(regreso);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

    }

    private void registrarUsuario() {
        String baseUrl = "https://b724-2806-2f0-56c0-fe66-f42e-6d86-b37a-d551.ngrok-free.app/Frecuency/registrar.php";

        // Obtener datos
        String nombreVal = nombre.getText().toString().trim();
        String apePVal = apellidoP.getText().toString().trim();
        String apeMVal = apellidoM.getText().toString().trim();
        String emailVal = email.getText().toString().trim();
        String passVal = pass.getText().toString().trim();

        // Construir URL
        String url = baseUrl +
                "?nombre=" + nombreVal +
                "&apeP=" + apePVal +
                "&apeM=" + apeMVal +
                "&email=" + emailVal +
                "&pass=" + passVal;

        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getInt("usr") == 1) {
                            Intent in = new Intent(RegistrarUsuario.this, Inicio.class);
                            Toast.makeText(RegistrarUsuario.this, "Registrado!", Toast.LENGTH_SHORT).show();
                            startActivity(in);
                            finish();
                        } else if(response.getInt("usr") == -1){
                            email.setText("");
                            pass.setText("");
                            Toast.makeText(RegistrarUsuario.this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                        }else {
                            // Error al registrar
                            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistrarUsuario.this, "Error de respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VolleyError", error.toString());
                    Toast.makeText(RegistrarUsuario.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(pet);
    }

}
