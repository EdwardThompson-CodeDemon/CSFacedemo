package sparta.realm.csfacedemo;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.Services.DatabaseManager;
import sparta.realm.csfacedemo.utils.GPS.FusedLocation;
import sparta.realm.spartautils.sparta_loc_util;



public class LocationChecker {

    private static LocationChecker instance;
    private Context context;
    public static String logTag = "LocationChecker";
    public static Location latestLocation;
    FusedLocation fusedLocation;

    private LocationChecker(Context context) {

        this.context=context;
        fusedLocation = new FusedLocation(context, current_location -> {
            Log.e(logTag, current_location.getLatitude() + " " + current_location.getLongitude() + "\nAccuracy: " + current_location.getAccuracy());
            latestLocation = current_location;
        });
    }

    public static LocationChecker getInstance(Context context) {
        if (instance == null) {
            instance = new LocationChecker(context);
        }
        return instance;
    }

    public void InitializeChecks() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                Configuration configuration = Globals.working_object(context, Configuration.class, "1");


                if (DatabaseManager.gps != null&&Globals.myDevice()!=null) {
                    Log.e(logTag, DatabaseManager.gps.getLatitude() + " " + DatabaseManager.gps.getLongitude());
                    if (DatabaseManager.gps.getLatitude() != 0) {
                        sparta.realm.csfacedemo.models.Location location = new sparta.realm.csfacedemo.models.Location(Globals.myDevice().transaction_no, "" + DatabaseManager.gps.getLatitude(), "" + DatabaseManager.gps.getLongitude(), "" + DatabaseManager.gps.getAltitude(), "" + DatabaseManager.gps.getSpeed(), "" + DatabaseManager.gps.getAccuracy());
                        Realm.databaseManager.insertObject(location);
                        MyApplication.rcso.upload("14");

                    }

                }


            }
        }, 30 * 1000, 30 * 1000);





    }



}
