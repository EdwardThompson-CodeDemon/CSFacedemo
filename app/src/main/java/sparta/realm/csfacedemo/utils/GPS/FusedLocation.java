package sparta.realm.csfacedemo.utils.GPS;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;


public class FusedLocation {
    Context act;
    FusedLocationProviderClient mFusedLocationClient;
    Location myLocation = null;
    static final int REQUEST_PERMS = 1;

    public interface LocationChangListener {
        void onLocationUpdated(Location current_location);

    }

    LocationChangListener main_handler = new LocationChangListener() {
        @Override
        public void onLocationUpdated(Location current_location) {

        }
    };

    public FusedLocation(final Context act, LocationChangListener handler_) {
        this.act = act;
        this.main_handler = handler_;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act);

        update_tm.schedule(new TimerTask() {
            @Override
            public void run() {
Looper.prepare();
                new Handler().post(() -> getLastLocation());
Looper.loop();
            }
        }, 100, 2000);


    }

    Timer update_tm = new Timer();

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(Task<Location> task) {
                Location location = task.getResult();
                if (location == null) {
                    requestNewLocationData();
                    Log.e("Main", " location is null men");
                } else {
                    Log.e("my coords==> ", "lats " + location.getLatitude() + " longs " + location.getLongitude());
                    main_handler.onLocationUpdated(location);
                }
            }
        });
    }

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act);
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.e("my refreshed coords==> ", "lats " + mLastLocation.getLatitude() + " longs " + mLastLocation.getLongitude());
            main_handler.onLocationUpdated(mLastLocation);
        }
    };
}
