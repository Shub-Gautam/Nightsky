package com.example.nightsky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class Mainpage extends AppCompatActivity {
    Button signout ;
    GoogleSignInClient mGoogleSignInClient ;
    TextView name ;
    ImageView pic ;

    EditText editText;
    Button button ;
    ImageView imageview ;
    TextView country , city , temp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        name =findViewById(R.id.textView2);
        pic =findViewById(R.id.imageView2);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Button signout = findViewById(R.id.button);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signOut();
            }
        });
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            String personName = account.getDisplayName();
            Uri personPhoto = account.getPhotoUrl();
            name.setText("Hey\n"+personName+"!");
            Glide.with(this).load(String.valueOf(personPhoto)).into(pic);
        }

        editText =findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button2);
        country = findViewById(R.id.country);
        city=findViewById(R.id.city);
        temp = findViewById(R.id.temperature);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                findweather();
            }
        });


    }
    public void findweather(){
        final String city1 = city.getText().toString();
        String url = "api.openweathermap.org/data/2.5/weather?q=delhi&appid=3dfa5eae32c84d4f24d0f8efa1b9049c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject obj1 = jsonObject.getJSONObject("sys");
                            String countryfind  = obj1.getString("country");
                            country.setText(countryfind);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Mainpage.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Mainpage.this);
        requestQueue.add(stringRequest);
    }
    private void signOut(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}