package sparta.realm.csfacedemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.adapters.MemberAdapter;
import sparta.realm.csfacedemo.databinding.ActivityMemberRecordsBinding;
import sparta.realm.csfacedemo.databinding.ActivitySesionManagementBinding;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.RecognitionSession;
import sparta.realm.csfacedemo.utils.FastScrolRecyclerview.FastScrollRecyclerViewItemDecoration;

public class MemberRecords extends AppCompatActivity {
    ActivityMemberRecordsBinding binding;
    ArrayList<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
//        DatabaseManager.log_event(this, "Checking Member records ...");

    }

    LinearLayoutManager linearLayoutManager;

    void initUI() {
        binding.include.title.setText("Member accounts");

        binding.include.back.setOnClickListener(v -> onBackPressed());
        members = Realm.databaseManager.loadObjectArray(Member.class, new Query().setOrderFilters(true, "name"));

        binding.memberList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.memberList.setLayoutManager(linearLayoutManager);
        HashMap<String, Integer> mapIndex = calculateIndexesForName(members);
        binding.memberList.setAdapter(new MemberAdapter(members, mapIndex,new MemberAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Member mem, View view) {

            }
        }));

        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
        binding.memberList.addItemDecoration(decoration);
        binding.memberList.setItemAnimator(new DefaultItemAnimator());
    }


    private HashMap<String, Integer> calculateIndexesForName_(ArrayList<String> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i);
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }
    private HashMap<String, Integer> calculateIndexesForName(ArrayList<Member> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        int its=items.size();
        for (int i = 0; i < its; i++) {
            String name = items.get(i).name.trim();
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

}