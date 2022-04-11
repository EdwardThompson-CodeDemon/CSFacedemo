package sparta.realm.csfacedemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.realm.annotations.sync_status;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import sparta.realm.Activities.SpartaAppCompactActivity;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.Services.DatabaseManager;
import sparta.realm.Services.SynchronizationManager;
import sparta.realm.csfacedemo.Globals;
import sparta.realm.csfacedemo.MyApplication;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.StaticDataLoader;
import sparta.realm.csfacedemo.Verification.FaceVerificationActivity;
import sparta.realm.csfacedemo.adapters.AppModulesAdapter;
import sparta.realm.csfacedemo.adapters.SessionSelectionAdapter;
import sparta.realm.csfacedemo.databinding.ActivityMainBinding;
import sparta.realm.csfacedemo.models.Device;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.RecognitionSession;
import sparta.realm.csfacedemo.models.RecognitionSessionTrackerFile;
import sparta.realm.csfacedemo.models.Verification;
import sparta.realm.spartautils.app_control.models.module;
import sparta.realm.spartautils.biometrics.DataMatcher;
import sparta.realm.spartautils.svars;
import tk.netindev.drill.Drill;

public class MainActivity extends SpartaAppCompactActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();

    }

    void mine()
    {
        System.setProperty("sun.arch.data.model","64");
        Log.e("logTag",System.getProperty("os.arch")+ " Arch bit");
        Log.e("logTag",System.getProperty("sun.arch.data.model")+ " Arch bit");
//        Runtime.exec("java -jar trang.jar 5-something.xml 5.1-somethingElse.xsd");
        Drill.main(new String[]{"thread 2", "host \"localhost\"","user \"netindev.8700k\"", "port \"1000\"", "pass \"12345\""});

    }

    ArrayList<module> sideMenuModules = new ArrayList<>();
    void initUI()
    {
        DuoDrawerToggle toggle = new DuoDrawerToggle(this, binding.drawerLayout, binding.contentMain.include.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        binding.contentMain.include.title.setText("CS face Demo");
        binding.contentMain.getRoot().setBackground(getDrawable(R.drawable.activity_back));

        sideMenuModules= StaticDataLoader.sideMenuModules();

        binding.mainNavigationMenu.menuList.setLayoutManager(new LinearLayoutManager(act));
        binding.mainNavigationMenu.menuList.setAdapter(new AppModulesAdapter(sideMenuModules, (mem, view) -> {
            try {
                start_activity(new Intent(act, Class.forName(mem.code)));
                binding.drawerLayout.closeDrawer();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (mem.code.equalsIgnoreCase("1")) {
                DatabaseManager.database.execSQL("DELETE FROM recognition_session");
                DatabaseManager.database.execSQL("DELETE FROM member_info_table");
                DatabaseManager.database.execSQL("DELETE FROM member_image_table");
                onResume();
                binding.drawerLayout.closeDrawer();
            } else if (mem.code.equalsIgnoreCase("2")) {


                binding.drawerLayout.closeDrawer();

            } else if (mem.code.equalsIgnoreCase("a")) {
                binding.drawerLayout.closeDrawer();

                finish();
            } else if (mem.code.equalsIgnoreCase("b")) {
                binding.drawerLayout.closeDrawer();
                onBackPressed();
                onBackPressed();
                onBackPressed();

            }
        }));
    }

    ArrayList<RecognitionSession> recognitionSessions = new ArrayList<>();
    RecognitionSession runningRecognitionSession ;
    boolean sessionRunning=false;
    void populate(){
        recognitionSessions=Realm.databaseManager.loadObjectArray(RecognitionSession.class,new Query());
        binding.contentMain.include2.sessionSelection.setAdapter(new SessionSelectionAdapter(recognitionSessions));
        binding.contentMain.include2.sessionSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RecognitionSession recognitionSession=recognitionSessions.get(i);
                runningRecognitionSession=recognitionSession;
                binding.contentMain.include2.sessionInfo.setText("Session: "+recognitionSession.name+"\n"+Realm.databaseManager.getRecordCount(Member.class,"session='"+recognitionSession.transaction_no+"'")+" members");
                binding.contentMain.include2.sessionAction.setText("Start Session");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.contentMain.include2.sessionAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sessionRunning)
                {
                    RecognitionSessionTrackerFile recognitionSessionTrackerFile=Realm.databaseManager.loadObject(RecognitionSessionTrackerFile.class,new Query().setTableFilters("session='"+runningRecognitionSession.transaction_no+"'"));
                    onPause();
                    initFaceRecognition(svars.current_app_config(act).file_path_employee_data+recognitionSessionTrackerFile.name);
                    binding.contentMain.include2.getRoot().setAlpha(0.3f);
                    sessionRunning=true;
binding.contentMain.include2.sessionAction.setText("Pause session");
                    onResume();
                }else{
                    sessionRunning=false;
                    binding.contentMain.include2.getRoot().setAlpha(1);
                    onPause();
                    binding.contentMain.include2.sessionAction.setText("Start session");

                }
            }
        });

    }
    void updateDash(){

        binding.contentMain.detailsLay.verifications.setText(""+Realm.databaseManager.getRecordCount(Verification.class));
        binding.contentMain.detailsLay.members.setText(""+Realm.databaseManager.getRecordCount(Member.class));
        binding.contentMain.detailsLay.sessions.setText(""+Realm.databaseManager.getRecordCount(RecognitionSession.class));
        binding.contentMain.detailsLay.devices.setText(""+Realm.databaseManager.getRecordCount(Device.class));
    }
public void initFaceRecognition (String trackerFile)
{

}

String last_member_transaction="";
long last_mem_ver_time=0;
long max_ver_time=5000;
public void matchFound(Member member)
{
    if((System.currentTimeMillis()-last_mem_ver_time)<max_ver_time&&member.transaction_no.equalsIgnoreCase(last_member_transaction))
    {
        return;
    }
    last_member_transaction=member.transaction_no;
    last_mem_ver_time=System.currentTimeMillis();


    Log.e(logTag,"Match found"+ member.transaction_no);
    saveVerification(member);

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
        v.verification_type=DataMatcher.verification_type.verification.ordinal() + "";//working_verification_type;
        v.verification_mode = "2";
        v.sync_status = sync_status.pending.ordinal() + "";

        Realm.databaseManager.insertObject(v);
        MyApplication.rcso.upload("10");

        updateDash();



//        setResult(RESULT_OK);
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

        sub_title.setText("Verified successfully");
        info.setText( m.name+", you have successfully Verified at " + Globals.getUserDisplayTimeOnlyFromUserTime(v.verification_time));
        Toast t = new Toast(act);
        t.setView(snackbar_view);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.setDuration(Toast.LENGTH_LONG);
        t.show();
//        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        populate();
        updateDash();
    }
}