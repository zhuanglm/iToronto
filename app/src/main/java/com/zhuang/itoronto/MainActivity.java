package com.zhuang.itoronto;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends FragmentActivity  {

    private TextView text_username;
    private ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_username = (TextView) findViewById(R.id.text_username);
        final TextView text_email = (TextView) findViewById(R.id.textEmail_Value);
        final TextView text_city = (TextView) findViewById(R.id.textCity_Value);

        Button mSignoffButton = (Button) findViewById(R.id.sign_out_button);
        mSignoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        if(DataUtil.accessToken != null) {
            //text_username.setText(DataDef.profile.getName());
            profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
            //profilePictureView.setProfileId(DataDef.profile.getId());

            GraphRequest request = GraphRequest.newMeRequest(
                    DataUtil.accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            if (response.getError() != null) {
                                // handle error
                            } else {
                                String email = me.optString("email");
                                String name = me.optString("name");
                                String id = me.optString("id");
                                String birthday = me.optString("birthday");
                                String gender = me.optString("gender");

                                try{
                                    String location = me.getJSONObject("location").optString("name");
                                    text_city.setText(location);
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /*if(gender.equals("male"))
                                    switch_gender.setChecked(false);
                                else
                                    switch_gender.setChecked(true);*/
                                text_username.setText(name);
                                text_email.setText(email);
                                //text_birthday.setText(birthday);
                                profilePictureView.setProfileId(id);
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,location,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        View viewBike = findViewById(R.id.Linear_bike);
        viewBike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapPager.class);  //从IntentActivity跳转到SubActivity
                intent.putExtra("name", "raymond");  //放入数据
                startActivity(intent);  //开始跳转
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }





}
