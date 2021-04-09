package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements WeatherDelegate {

    MyApplication mApp;
    ImageView mIcon;
    TextView mTemperature;
    TextView mCity;
    Button mUpdate;
    /*
    Добавить остальные поля
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (MyApplication) getApplicationContext();

        mTemperature = findViewById(R.id.textTemp);
        mIcon = findViewById(R.id.imgWeather);
        mUpdate = findViewById(R.id.btnUpdate);
        mCity = findViewById(R.id.textCityName);
        /*
        Добавить остальное
        */

        mApp.getWeather().setDelegate(this);

        View.OnClickListener updateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onUpdatePress(v);
            }
        };
        mUpdate.setOnClickListener(updateListener);
    }


    private void onUpdatePress(View v)
    {
        mApp.getWeather().update();
    }


    @Override
    public void onUpdate()
    {
        mTemperature.setText(mApp.getWeather().currentTemperature + " °C");
        Picasso.with(this).load(mApp.getWeather().icon).into(mIcon);
        mCity.setText(mApp.getWeather().getCity());
    }


    @Override
    public void onError()
    {
        Toast.makeText(this, "Connection error!", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cities:
                cities();
                return true;

            case R.id.about:
                about();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void about()
    {
        //Intent intent = new Intent(getApplicationContext(), About.class);
        //startActivity(intent);
    }


    private void cities()
    {
        Intent intent = new Intent(getApplicationContext(), Cities.class);
        startActivity(intent);
    }
}