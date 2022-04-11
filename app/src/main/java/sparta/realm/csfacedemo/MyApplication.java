package sparta.realm.csfacedemo;

import android.app.Application;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sparta.realm.Realm;
import sparta.realm.RealmClientCallbackInterface;
import sparta.realm.csfacedemo.models.RealmDynamics.spartaDynamics;
import sparta.realm.spartautils.svars;

import sparta.realm.utils.AppConfig;


public class MyApplication extends Application {

    private static Context baseContext;
    private static Context appContext;
    public static AppConfig Timecapture = null;

    public static String logTag = "Application";
    public static RealmClientSyncOverride rcso = new RealmClientSyncOverride();
    public static SyncInterface syncInterface = new SyncInterface() {
    };
    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
        baseContext = getBaseContext();
//        Timecapture = new AppConfig("http://ta.cs4africa.com:9090",
//                null,
//                "U.I.P.A.",
//                "MAIN CAMPUS",
//                "/Authentication/Login/Submit", false
//
//        );
//        Timecapture = new AppConfig("https://tabiz.cs4africa.com/vss",////vss
//                null,
//                "VSS",
//                "VSS",
//                "/Authentication/Login/Submit", false
//
//        );

 Timecapture = new AppConfig("https://tabiz.cs4africa.com/tanda_demo",//Demo
                null,
                "Capture Solutions",
                "TEST ACCOUNT",
                "/Authentication/Login/Submit", false

        );


        Realm.Initialize(this, new spartaDynamics(), BuildConfig.VERSION_NAME, BuildConfig.APPLICATION_ID, Timecapture, "ihdBXU3FcZAawN2qayK9PG3kGz1BocN0EjHOe6hn2LhCubiwJYP7XsbIxildd0hfE9Tio36fGMdwoH4kC0HJNjzs5GbdWchRPmn5O/omstCi37+w7VNFkOgWxDhQSiDn4Apb77g0FwoNvyhVgE7lBx9DxcSnqvniTyKidXlHCak=");


        svars.set_sync_interval_mins(this, 20);
        LocationChecker locationChecker=LocationChecker.getInstance(appContext);
        locationChecker.InitializeChecks();
//        DatabaseManager.database.execSQL("DELETE FROM random_call");
//        DatabaseManager.database.execSQL("DELETE FROM clock_info_table");
//        DatabaseManager.database.execSQL("DROP TABLE geo_fence_point");
//        DatabaseManager.database.execSQL("DROP TABLE geo_fence");
//        DatabaseManager.database.execSQL("DELETE FROM messages");
//        DatabaseManager.database.execSQL("DELETE FROM member_info_table");
//        DatabaseManager.database.execSQL("DELETE FROM user_table");
//        DatabaseManager.database.execSQL("DELETE FROM member_image_table");
//
//        DatabaseManager.database.execSQL("DELETE FROM games");
//        DatabaseManager.database.execSQL("DELETE FROM game_invites");
//        DatabaseManager.database.execSQL("DELETE FROM game_invite_status");

    }
    public static void attatchInterface(SyncInterface syncInterface) {
        MyApplication.syncInterface = syncInterface;
    }

    public static Boolean online = false;
    public static long last_typing_time = 0;
    public static long last_recording_audio_time = 0;

    public static void Initialize(SyncInterface syncInterface) {

        MyApplication.syncInterface = syncInterface;
        rcso.Initialize(new RealmClientCallbackInterface.Stub() {
            @Override
            public void on_status_changed(String s) throws RemoteException {
                Log.e(logTag, "Status changed" + s);
            }

            @Override
            public void on_info_updated(String s) throws RemoteException {
                Log.e(logTag, "On Info updated " + s);
                MyApplication.syncInterface.onInfoUpdated(s);

            }

            @Override
            public void on_main_percentage_changed(int i) throws RemoteException {

            }

            @Override
            public void on_secondary_progress_changed(int i) throws RemoteException {

            }

            @Override
            public void onSynchronizationBegun() throws RemoteException {

            }

            @Override
            public void onServiceSynchronizationCompleted(String service_id) throws RemoteException {


                MyApplication.syncInterface.onSyncCompleted(service_id);
            }


            @Override
            public void onSynchronizationCompleted() throws RemoteException {

            }

            @Override
            public List<String> OnAboutToUploadObjects(String s, List<String> list) throws RemoteException {
                return null;
            }

            @Override
            public String OnAboutToDownloadObjects(String s) throws RemoteException {
                return null;
            }

            @Override
            public String OnDownloadedObjects(String s, String s1) throws RemoteException {
                return s1;
            }

            @Override
            public ParcelFileDescriptor OnDownloadedData(String s, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                return null;
            }
        });



    }


    public static Context getAppContext() {
        return appContext;
    }
}
