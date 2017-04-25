package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xh on 2017/4/25.
 * 该工具类的作用是解析服务器返回的数据-
 */


public class Utility {
    /***
     *
     * @param response 网络请求放回的json数据
     * @return  标识是不是解析成功的boolean变量
     */
    public static boolean handleProvinceResponse(String response){
        if(TextUtils.isEmpty(response)){
            try{

                JSONArray allProvince = new JSONArray(response);
                for (int i = 0;i<allProvince.length();i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province .setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch(Exception e ){
                e.printStackTrace();
            }

        }
        return false;
    }
    public static boolean handleCityResponse(String response,int provinceId){
        if(TextUtils.isEmpty(response)){
            try{
                JSONArray allCity = new JSONArray(response);
                for(int i= 0;i<allCity.length();i++){
                    JSONObject citydObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(citydObject.getString("name"));
                    city.setCityCode(citydObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int cityId){
        if(TextUtils.isEmpty(response)){
            try{
              JSONArray allCouties = new JSONArray(response);
                for(int i= 0;i<allCouties.length();i++){
                    JSONObject countyObject = allCouties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
