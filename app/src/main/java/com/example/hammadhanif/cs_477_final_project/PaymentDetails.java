package com.example.hammadhanif.cs_477_final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDetails extends AppCompatActivity {

    TextView text1;
    TextView text22;
    TextView text3;
    Button PostJob;
    double lat;
    double longitude;
    int num_jobs_posted =0;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<String> data_add;
    DatabaseReference mDatabase;

    String location;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        text1 = (TextView) findViewById(R.id.text_idd);
        text22 = (TextView) findViewById(R.id.textamout);
        text3=(TextView)findViewById(R.id.textstatus);
        PostJob = (Button) findViewById(R.id.PostJob);
        Intent intent = getIntent();
        data_add = new ArrayList();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        }catch(JSONException e)
        {
            e.printStackTrace();
        }


        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        FirebaseUser user = firebaseAuth.getCurrentUser();

        String user_id = user.getUid();

//retrieving first name and last name
        mDatabase.child("Users").child(user_id).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                location = (String) dataSnapshot.child("Location").getValue();

                Toast.makeText(PaymentDetails.this,
                        "Location" + location, Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showDetails(JSONObject response, String paymentAmount) {

        try {
            text1.setText(response.getString("id"));
            text22.setText(response.getString("state"));
            text3.setText("$" + paymentAmount);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


    public void onClickPostJob(View view) {

        data_add.clear();
        data_add.add("4450 Rivanna River Way");
        data_add.add("10457 Presidents Park Drive");
        data_add.add("4352 Mason Pond Drive");
        data_add.add("Fair Oaks Mall");
        num_jobs_posted = data_add.size()-1;
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt("size",data_add.size());

        for(int i=0; i < data_add.size(); i++)
        {
            String address = data_add.get(i);
            Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Geocoder geo = new Geocoder(PaymentDetails.this);
            List<Address> list_address = new ArrayList<>();

            try {
                list_address = geo.getFromLocationName(address, 1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            if (list_address.size() > 0) {
                Address add = list_address.get(0);
                lat = add.getLatitude();
                longitude = add.getLongitude();
               // SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("lat" +  Integer.toString(i), Double.toString(lat));
                editor.putString("longitude" +  Integer.toString(i), Double.toString(longitude));
                editor.apply();
            }

        }
//        String address = data_add.get(0);
//        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
//        Geocoder geo = new Geocoder(PaymentDetails.this);
//        List<Address> list_address = new ArrayList<>();
//
//        try {
//            list_address = geo.getFromLocationName(address, 1);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (list_address.size() > 0) {
//            Address add = list_address.get(0);
//            lat = add.getLatitude();
//            longitude = add.getLongitude();
//            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//            editor.clear();
//            editor.putString("lat", Double.toString(lat));
//            editor.putString("longitude", Double.toString(longitude));
//            editor.apply();
//        }
    }
}
