package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends AppCompatActivity {
    EditText et_user, et_password;
    Button btn_login;
    SharedPreferences archivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_user = findViewById(R.id.user);
        et_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);

        archivo = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);

        if(archivo.contains("id_usuario")){
            Intent ini = new Intent(this, MainActivity.class);
            startActivity(ini);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        String url = "http://192.168.1.133/Frecuency/ingreso.php?usr=";
        url += et_user.getText().toString();
        url += "&pass=";
        url += et_password.getText().toString();
        JsonObjectRequest pet = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("usr") != -1) {
                        Intent in = new Intent(Inicio.this, MainActivity.class);
                        SharedPreferences.Editor editor = archivo.edit();
                        editor.putInt("id_usuario", response.getInt("usr"));
                        editor.apply();
                        startActivity(in);
                        finish();
                    }else {
                        et_user.setText("");
                        et_password.setText("");
                        Toast toast = Toast.makeText(Inicio.this, "Datos Incorrectos", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("yo", error.toString());
                        Toast.makeText(Inicio.this, "Error en la base de datos.", Toast.LENGTH_SHORT).show();
                    }

                });
        RequestQueue lanzarPeticion = Volley.newRequestQueue(this);
        lanzarPeticion.add(pet);
    }
}