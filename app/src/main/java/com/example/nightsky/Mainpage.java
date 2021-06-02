package com.example.nightsky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Mainpage extends AppCompatActivity {
    Button signout ;
    GoogleSignInClient mGoogleSignInClient ;
    TextView name ;
    ImageView pic ;

    EditText editText;
    Button button ;
    ImageView imageviewshow ;
    TextView country , cityshow1 , temp ;

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
        cityshow1=findViewById(R.id.cityshow);
        temp = findViewById(R.id.temperature);
        imageviewshow = findViewById(R.id.imageView4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                findweather();
            }
        });


    }

    public void findweather(){
        final StringBuffer city1 = new StringBuffer(editText.getText().toString());

        // Showing an error dialog when no city is entered and making it to delhi
        if(city1.toString().isEmpty()){
            new MaterialAlertDialogBuilder(Mainpage.this)
                .setTitle("Warning")
                .setMessage("Enter a valid city")
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
        .show();
        city1.append("Delhi");
        }
        //-----------------------------------------------------------

        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city1+"&appid=3dfa5eae32c84d4f24d0f8efa1b9049c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject obj1 = jsonObject.getJSONObject("sys");
                            String countryfind  = obj1.getString("country");
                            country.setText(countryfind);


                            String cityfind = jsonObject.getString("name");
                            cityshow1.setText(cityfind);

                            JSONObject obj3 = jsonObject.getJSONObject("main");
                            double tempfind = obj3.getDouble("temp");
                            temp.setText(""+tempfind+"C");

                            JSONArray obj4 = jsonObject.getJSONArray("weather");
                            JSONObject jsonObject1 = obj4.getJSONObject(0);
                            String img = jsonObject1.getString("icon");

                            Picasso.get().load("http://openweathermap.org/img/wn/"+img+"@2x.png").into(imageviewshow);

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