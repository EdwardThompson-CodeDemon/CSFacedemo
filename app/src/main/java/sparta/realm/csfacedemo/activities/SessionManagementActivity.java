package sparta.realm.csfacedemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import sparta.realm.Activities.SpartaAppCompactActivity;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.adapters.RecognitionSessionAdapter;
import sparta.realm.csfacedemo.databinding.ActivitySesionManagementBinding;
import sparta.realm.csfacedemo.models.RecognitionSession;

public class SessionManagementActivity extends SpartaAppCompactActivity {
ActivitySesionManagementBinding binding;
ArrayList<RecognitionSession> recognitionSessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySesionManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
    }

    void initUI() {
        binding.include.title.setText("Session management");
        binding.include.back.setOnClickListener(v -> onBackPressed());
        binding.fab.setOnClickListener(v -> start_activity(new Intent(act,ImportPhotosActivity.class)));
        binding.sessionsList.setLayoutManager(new LinearLayoutManager(this));
        recognitionSessions=Realm.databaseManager.loadObjectArray(RecognitionSession.class,new Query());
        binding.sessionsList.setAdapter(new RecognitionSessionAdapter(recognitionSessions, (mem, view) -> {

            Intent intent = new Intent(act, MemberRecords.class);
            intent.putExtra("session", mem.transaction_no);


            Pair<View, String> p0 = Pair.create(view, "main_content");
//            Pair<View, String> p1 = Pair.create(view.findViewById(R.id.name), "name");
//            Pair<View, String> p2 = Pair.create(view.findViewById(R.id.fence_purpose), "fence_purpose");
//            Pair<View, String> p3 = Pair.create(view.findViewById(R.id.fence_area), "fence_area");
//
//
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(act, p0);
            startActivity(intent, options.toBundle());
//                start_activity(intent);
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
recognitionSessions= (Realm.databaseManager.loadObjectArray(RecognitionSession.class, new Query()));
        binding.sessionsList.getAdapter().notifyDataSetChanged();
    }
}