package com.example.weather;

import android.app.VoiceInteractor;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Weather
{
    private final  String PROTOCOL = "https://";
    private final String HOST = "api.openweathermap.org";
    private final String HOST_IMAGE = "openweathermap.org";
    private final String REQUEST_PATH_CURRENT = "/data/2.5/weather?";

    //служебная инф. для работы
    private Context mParent;
    private String mCity;
    private String mAPIKey;
    private String mLang;
    private RequestQueue mRequestQueue;
    private int mUpdateTime = 0;
    private Timer mTimer = null;
    private WeatherDelegate mDelegate = null;

    //хранение данных о погоде
    int currentTemperature = 0;
    String icon;
    int feelsLike = 0;
    int minTemp = 0;
    int maxTemp = 0;
    int pressure = 0;
    int humidity = 0;
    String description;
    int visibility = 0;
    int windSpeed = 0;
    int windDeg = 0;
    int clouds = 0;
    long sunSet = 0;
    long sunRise = 0;


    public Weather(Context parent, String city, String apiKey, String lang, int updateTime)
    {
        mParent = parent;
        mCity = city;
        mAPIKey = apiKey;
        mLang = lang;
        mUpdateTime = updateTime;

        init();
    }


    private void init()
    {
        mRequestQueue = Volley.newRequestQueue(mParent);

        if (mUpdateTime != 0)
        {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(
                    new TimerTask()
                                       {
                                           @Override
                                           public void run() {
                                               update();
                                           }
                                       },
                0,
                 mUpdateTime );
        }
    }


    void  update()
    {
        String url = PROTOCOL
                + HOST
                + REQUEST_PATH_CURRENT
                + "q=" + mCity
                + "&"
                + "appid=" + mAPIKey
                + "&"
                + "lang=" + mLang;

        send(url);
    }


    private void send(String url)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        parse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                        onError();
                    }
                }
        );

        mRequestQueue.add(request);
    }


    private void onError()
    {
        if (mDelegate != null)
            mDelegate.onError();

        if (mTimer != null)
            mTimer.cancel();
    }


    private String prepareWeatherIconURI(String icon)
    {
        return PROTOCOL + HOST_IMAGE + "/img/wn/" + icon + "@4x.png";
    }


    private int kelvinToCel(double value)
    {
        return (int)Math.round(value - 273.15);
    }


    private void parse(JSONObject response)
    {
        try
        {
            JSONObject main = response.getJSONObject("main");
            currentTemperature = kelvinToCel(main.getDouble("temp"));

            JSONArray weather = response.getJSONArray("weather");
            JSONObject current = weather.getJSONObject(0);

            icon = prepareWeatherIconURI(current.getString("icon"));
            //feelsLike = current.getInt("feels_like");
            //minTemp = current.getInt("temp_min");
            //maxTemp = current.getInt("temp_max");
            //pressure = current.getInt("pressure");
            //humidity = current.getInt("humidity");
            //description = current.getString("description");
            //visibility = current.getInt("visibility");
            //windSpeed = current.getInt("speed");
            //windDeg = current.getInt("deg");
            //clouds = current.getInt("clouds"); // "all" ?
            //sunSet = current.getLong("sunSet");
            //sunRise = current.getLong("sunRise");

            if (mDelegate != null)
                mDelegate.onUpdate();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            onError();
        }
    }


    public void setCity(String city)
    {
        if (!city.isEmpty())
        {
            mCity = city;
            update();
        }
    }


    public void  setDelegate(WeatherDelegate delegate)
    {
        mDelegate = delegate;
    }

    public String getCity()
    {
        return mCity;
    }

}



