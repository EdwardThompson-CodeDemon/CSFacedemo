package sparta.realm.csfacedemo.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.models.Device;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.RecognitionSession;
import sparta.realm.spartautils.svars;


public class RecognitionSessionAdapter extends RecyclerView.Adapter<RecognitionSessionAdapter.view> {

    Context cntxt;
    public ArrayList<RecognitionSession> recognitionSessions;
    onItemClickListener listener;
    HashMap<String, Integer> mapIndex;



    public interface onItemClickListener {

        void onItemClick(RecognitionSession mem, View view);
    }


    public RecognitionSessionAdapter(ArrayList<RecognitionSession> recognitionSessions, onItemClickListener listener) {
        this.recognitionSessions = recognitionSessions;

        this.listener = listener;


    }

    @NonNull
    @Override
    public view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cntxt=parent.getContext();
        View view = LayoutInflater.from(cntxt).inflate(R.layout.item_recognition_session, parent, false);

        return new view(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view holder, int position) {
        RecognitionSession recognitionSession = recognitionSessions.get(position);
        holder.name.setText(recognitionSession.name);
        Device d = Realm.databaseManager.loadObject(Device.class, new Query().setTableFilters("transaction_no='" + recognitionSession.creator_device+ "'"));
        if (d != null) {
            holder.info1.setText("Created by a "+d.model);
        }
        holder.info2.setText(Realm.databaseManager.getRecordCount(Member.class,"session='"+recognitionSession.transaction_no+"'")+" members");


        holder.itemView.setOnClickListener(view -> listener.onItemClick(recognitionSession,holder.itemView));


    }


    @Override
    public int getItemCount() {
        return recognitionSessions.size();
    }

    public class view extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, info1,info2;


        view(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            info1 = itemView.findViewById(R.id.info1);
            info2 = itemView.findViewById(R.id.info2);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
