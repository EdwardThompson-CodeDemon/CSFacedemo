package sparta.realm.csfacedemo.Verification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.luxand.FSDK;
import com.luxand.FSDK.HTracker;

import sparta.realm.Activities.SpartaAppCompactActivity;
import sparta.realm.DataManagement.Models.Query;

import sparta.realm.Realm;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.activities.MainActivity;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.MemberImage;
import sparta.realm.spartautils.biometrics.DataMatcher;
import sparta.realm.spartautils.biometrics.face.CaptureHandler;
import sparta.realm.spartautils.biometrics.face.Preview;
import sparta.realm.spartautils.matching_interface;
import sparta.realm.spartautils.svars;


public class FaceVerificationActivity extends MainActivity {

    private boolean mIsFailed = false;
    private Preview mPreview;
    private CaptureHandler mDraw;
    private final String database = "MemoryR080.dat";
    private final String help_text = "Luxand Face Recognition\n\nJust tap any detected face and name it. The app will recognize this face further. For best results, hold the device at arm's length. You may slowly rotate the head for the app to memorize you at multiple views. The app can memorize several persons. If a face is not recognized, tap and name it again.\n\nThe SDK is available for mobile developers: www.luxand.com/facesdk";

    public static float sDensity = 1.0f;

    public void showErrorAndClose(String error, int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error + ": " + code)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .show();
    }

    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false) // cancel with button only
                .show();
    }

    private void resetTrackerParameters() {
        int errpos[] = new int[1];
        FSDK.SetTrackerMultipleParameters(mDraw.mTracker, "DetectGender=true;DetectAge=true;ContinuousVideoFeed=true;FacialFeatureJitterSuppression=0;RecognitionPrecision=1;Threshold=0.996;Threshold2=0.9995;ThresholdFeed=0.97;MemoryLimit=2000;HandleArbitraryRotations=false;DetermineFaceRotationAngle=false;InternalResizeWidth=70;FaceDetectionThreshold=3;", errpos);
        if (errpos[0] != 0) {
            showErrorAndClose("Error setting tracker parameters, position", errpos[0]);
        }
    }
private void resetTrackerParameters(HTracker hTracker) {
        int errpos[] = new int[1];
        FSDK.SetTrackerMultipleParameters(hTracker, "DetectGender=true;DetectAge=true;ContinuousVideoFeed=true;FacialFeatureJitterSuppression=0;RecognitionPrecision=1;Threshold=0.996;Threshold2=0.9995;ThresholdFeed=0.97;MemoryLimit=2000;HandleArbitraryRotations=false;DetermineFaceRotationAngle=false;InternalResizeWidth=70;FaceDetectionThreshold=3;", errpos);
        if (errpos[0] != 0) {
            showErrorAndClose("Error setting tracker parameters, position", errpos[0]);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sDensity = getResources().getDisplayMetrics().scaledDensity;
        initFaceRecognition(svars.current_app_config(act).file_path_employee_data+"Idlefile.dat");

    }
    boolean face_initialized = false;
    boolean camera_initialized = false;

     @Override
     public void initFaceRecognition(String tracker_file) {
        face_initialized = true;
        this.trackerFile = tracker_file;

        int res = 0;// FSDK.ActivateLibrary("ihdBXU3FcZAawN2qayK9PG3kGz1BocN0EjHOe6hn2LhCubiwJYP7XsbIxildd0hfE9Tio36fGMdwoH4kC0HJNjzs5GbdWchRPmn5O/omstCi37+w7VNFkOgWxDhQSiDn4Apb77g0FwoNvyhVgE7lBx9DxcSnqvniTyKidXlHCak=");
        if (res != FSDK.FSDKE_OK) {
            mIsFailed = true;
            showErrorAndClose("FaceSDK activation failed", res);
        } else {
            FSDK.Initialize();

            mDraw = findViewById(R.id.face_graphics_overlay);
            mPreview = findViewById(R.id.face_graphics_preview);
            mDraw.setVisibility(View.VISIBLE);
            mPreview.setVisibility(View.VISIBLE);
            mDraw.vt = DataMatcher.verification_type.verification;


            mDraw.setup_handler(new matching_interface() {
                @Override
                public void on_match_complete(boolean match_found, String mils) {

                }

                @Override
                public void on_match_found(String employee_id, String data_index, String match_time, int v_type, int verrification_mode) {
//                    if (Globals.myself().sid.equalsIgnoreCase(employee_id)) {

//                        match_found(m, data_index);
//
//                    }
                    Member m = Realm.databaseManager.loadObject(Member.class, new Query().setTableFilters("transaction_no='" + employee_id + "'"));
                    m.profile_photo = Realm.databaseManager.loadObject(MemberImage.class, new Query().setTableFilters("member_id='" + m.transaction_no + "'"));
matchFound(m);
                }

                @Override
                public void on_finger_match_found(String fp_id, int score, String match_time) {

                }


                @Override
                public void on_match_progress_changed(int progress) {

                }

                @Override
                public void on_match_faild_reason_found(int reason, String employee_id) {

                }
            });
if(!camera_initialized) {
    mPreview.setup_overlay(mDraw, 1, 90);//face :0 for back and 1 for front
    camera_initialized=true;
}
HTracker hTracker=new HTracker();
            mDraw.vm = new Verifier();


            svars.set_current_device(act, svars.DEVICE.GENERAL.ordinal());

            if (FSDK.FSDKE_OK != FSDK.LoadTrackerMemoryFromFile(hTracker, trackerFile)) {
                res = FSDK.CreateTracker(hTracker);
                if (FSDK.FSDKE_OK != res) {
                    showErrorAndClose("Error creating tracker", res);
                }
            }

            resetTrackerParameters(hTracker);

            mDraw.mTracker =hTracker;

            resumeProcessingFrames();

        }
    }

    String trackerFile = "";

    @Override
    public void onPause() {
        super.onPause();
        if (face_initialized) {
            pauseProcessingFrames();

            FSDK.SaveTrackerMemoryToFile(mDraw.mTracker, trackerFile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (face_initialized) {
            if (mIsFailed) return;
            resumeProcessingFrames();
        }

    }

    private void pauseProcessingFrames() {
        mDraw.mStopping = 1;

        // It is essential to limit wait time, because mStopped will not be set to 0, if no frames are feeded to mDraw
        for (int i = 0; i < 100; ++i) {
            if (mDraw.mStopped != 0) break;
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
            }
        }
    }

    private void resumeProcessingFrames() {
        mDraw.mStopped = 0;
        mDraw.mStopping = 0;
    }
}


