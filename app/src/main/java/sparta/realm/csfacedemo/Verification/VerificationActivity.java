package sparta.realm.csfacedemo.Verification;

import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.realm.annotations.sync_status;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import sparta.realm.Activities.SpartaAppCompactFingerPrintActivity;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.Services.DatabaseManager;
import sparta.realm.Services.SynchronizationManager;
import sparta.realm.csfacedemo.Globals;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.databinding.ActivityMainBinding;
import sparta.realm.csfacedemo.databinding.ActivityVerifficationBinding;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.Verification;
import sparta.realm.spartautils.biometrics.DataMatcher;
import sparta.realm.spartautils.svars;




public class VerificationActivity extends SpartaAppCompactFingerPrintActivity {
    DataMatcher.verification_type working_verification_type;
    int working_verification_mode = 1;
    Member myself;
    ActivityVerifficationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifficationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        working_verification_type = DataMatcher.verification_type.valueOf(getIntent().getStringExtra("verification_type"));


        initUi();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.time.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                    }
                });
            }
        }, 100, 10000);
    }

    void initUi() {

        binding.include.title.setText("Verification");
        binding.include.back.setOnClickListener(v -> onBackPressed());
        binding.subTitle.setText(working_verification_type == DataMatcher.verification_type.clock_in ? "Clocking in" : working_verification_type == DataMatcher.verification_type.clock_out ? "Clocking out" : "Verifying");
        binding.oneOnOneName.setText(myself.name);
        // Realm.databaseManager.loadObject(MemberImage.class, new Query().setTableFilters("member_id='" + myself.sid + "'"));

        try {
            binding.imageView3.setImageURI(Uri.parse(Uri.parse(svars.current_app_config(act).file_path_employee_data) + myself.profile_photo.image));

        } catch (Exception ex) {

        }

    }

    public void match_found(Member found_employee, final String data_index) {
//        Log.e(logTag,"Match found =>"+ employee_id + " found in " + match_time + "ms");
//        if (found_employee != null && found_employee.sid.equalsIgnoreCase(myself.sid)) {
        if (found_employee != null) {
            Log.e("Match found =>", "and employee found=>" + found_employee.name);

            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    saveVerification(found_employee);

                }
            });

        } else {

        }


    }

    void saveVerification(Member m) {

        Verification v = new Verification();
        v.transaction_no = Globals.getTransactionNo();
//        v.sid = v.transaction_no;
        v.verification_time = svars.sdf_db_time.format(Calendar.getInstance().getTime());
        v.verification_date = svars.sdf_db_time.format(Calendar.getInstance().getTime());

        v.lat = DatabaseManager.gps.getLatitude() + "";
        v.lon = DatabaseManager.gps.getLongitude() + "";

        v.member_id = m.sid;
        v.user_id = m.sid;
        v.verification_type = working_verification_type.ordinal() + "";//working_verification_type;
        v.verification_mode = "2";
        v.sync_status = sync_status.pending.ordinal() + "";

        Realm.databaseManager.insertObject(v);



        setResult(RESULT_OK);
        View snackbar_view = LayoutInflater.from(act).inflate(R.layout.dialog_verification_confirmation, null);
        TextView sub_title = snackbar_view.findViewById(R.id.sub_title);
        TextView member_name = snackbar_view.findViewById(R.id.name);
        TextView member_no = snackbar_view.findViewById(R.id.info1);
        CircleImageView member_icon = snackbar_view.findViewById(R.id.icon);
        ImageView icon = snackbar_view.findViewById(R.id.imageView2);
        snackbar_view.findViewById(R.id.member_view).setVisibility(View.VISIBLE);
        icon.setColorFilter(Color.GREEN);
        sub_title.setTextColor(Color.GREEN);
        TextView info = snackbar_view.findViewById(R.id.content);
        member_name.setText(m.name);
//        member_no.setText(m.member_no);
        member_icon.setImageURI(null);
        member_icon.setImageURI(Uri.parse(Uri.parse(svars.current_app_config(Realm.context).file_path_employee_data) + m.profile_photo.image));

        sub_title.setText(working_verification_type == DataMatcher.verification_type.clock_in ? "Clocked in successfully" : working_verification_type == DataMatcher.verification_type.clock_out ? "Clocked out successfully" : "Verified successfully");
        info.setText((working_verification_type == DataMatcher.verification_type.clock_in ? "You have successfully clocked in at " : working_verification_type == DataMatcher.verification_type.clock_out ? "You have successfully clocked out at " : "You have successfully Verified at ") + Globals.getUserDisplayTimeOnlyFromUserTime(v.verification_time));
        Toast t = new Toast(act);
        t.setView(snackbar_view);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setDuration(Toast.LENGTH_LONG);
        t.show();
        finish();
    }

}