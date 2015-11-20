package com.github.andreracz.react.geolocation;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;


public class GeolocationModule extends ReactContextBaseJavaModule implements LocationListener {
    
    
    public GeolocationModule(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        this.activity = activity;
    }
    
    Activity activity = null;
    Callback errorCb = null;
    Callback cb = null;
    
    
    @ReactMethod
    public void getCurrentPosition(ReadableMap params, Callback errorCallback, Callback callback) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, false);
        
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            java.util.List<String> providers = locationManager.getAllProviders();
            for(String p: providers) {
                Log.i("GeolocationModule", "Provider: " + p);
                location = locationManager.getLastKnownLocation(p);
                if (location != null) {
                    Log.i("GeolocationModule", "Found one");
                    break;
                }
            }
        }
        if (location == null) {
            java.util.List<String> providers = locationManager.getAllProviders();
            cb = callback;
            errorCb = errorCallback;
            for(String p: providers) {
                Log.d("GeolocationModule", "Listening to provider: " + p);
                locationManager.requestSingleUpdate(p, this, null);
            }
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    cb = null;
                    Callback callback = errorCb;
                    errorCb = null;
                    callback.invoke("Error, location not found");
                }
            }).start();
            
        } else {
            callback.invoke(location.getLatitude(), location.getLongitude(), location.getAccuracy());
        }
    }
    
    @Override
    public void onProviderDisabled(String provider) {
       Log.d("GeolocationModule", "Provider: " + provider + " disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("GeolocationModule", "Provider: " + provider + " enabled");

    }

    @Override
    public  void onStatusChanged(String provider, int status, Bundle extras) {
         Log.d("GeolocationModule", "Provider: " + provider + " changed status to: " + status);
        
    }
    
    @Override
    public synchronized void onLocationChanged(Location location) {
        Log.d("GeolocationModule", "location: " + location);
        if (cb != null) {
            Callback callback = cb;
            errorCb = null;
            cb = null;
            callback.invoke(location.getLatitude(), location.getLongitude(), location.getAccuracy());
        } else {
            Log.d("GeolocationModule", "ignoring");
        }
        
    }
    
    public String getName() {
        return "GeolocationModule";
    }

    
   
}