package com.kgec.knowyourweather;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {

    private String mTempareture,mIcon,mCity,mWeatherType;
    private int mCondition;


    public static  WeatherData fromJson(JSONObject jsonObject){

        WeatherData data=new WeatherData();
        try {
            data.mCity=jsonObject.getString("name");
            data.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            data.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            data.mIcon=updateWeatherIcon(data.mCondition);

            double temp_result=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int rounded_value= (int) Math.rint(temp_result);

            data.mTempareture=Integer.toString(rounded_value);
            return data;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String updateWeatherIcon(int condition) {

        if(condition>=0 && condition<=300)

        // thee are the name of icon
        {
            return "thunderstrom1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstrom1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "dunno";
    }

    public String getmTempareture() {
        return mTempareture+"°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public void setmTempareture(String mTempareture) {
        this.mTempareture = mTempareture;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmWeatherType(String mWeatherType) {
        this.mWeatherType = mWeatherType;
    }

    public void setmCondition(int mCondition) {
        this.mCondition = mCondition;
    }
}