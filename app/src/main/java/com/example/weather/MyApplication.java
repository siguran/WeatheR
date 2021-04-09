package com.example.weather;



import android.app.Application;

import java.util.ArrayList;
import java.util.WeakHashMap;



public class MyApplication extends Application
{
    private  ArrayList<String> mCityManager;
    private Weather mWeather;


    public ArrayList<String> getCityManager()
    {
        return mCityManager;
    }


    public  Weather getWeather()
    {
        return  mWeather;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();

        mCityManager = new ArrayList<String>();
        mCityManager.add("Нижний Тагил");
        mWeather = new Weather(
                this,
                mCityManager.get(0),
                "952c234844b8eb6e044f37cc906bd3ad",
                "ru",
                900000);
    }
}

