package com.example.chatboot.utilites;

import android.content.Context;
import android.content.SharedPreferences;

public class PerferenceManager {
    private final SharedPreferences sharedPreferences;

    public PerferenceManager(Context context){
        sharedPreferences=context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public void putBoolean(String Key,Boolean value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(Key,value);
        editor.apply();
    }
    public boolean getBoolean(String Key){
        return sharedPreferences.getBoolean(Key,false);
    }
    public void putString(String Key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Key,value);
        editor.apply();
    }
    public String getString(String Key){
        return sharedPreferences.getString(Key,null);
    }

    public void claer(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
