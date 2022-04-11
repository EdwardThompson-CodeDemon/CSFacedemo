package sparta.realm.csfacedemo.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import sparta.realm.DataManagement.Models.Query;
import sparta.realm.Realm;
import sparta.realm.csfacedemo.R;
import sparta.realm.csfacedemo.models.Device;
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.RecognitionSession;
import sparta.realm.spartautils.svars;


public class SessionSelectionAdapter extends BaseAdapter {

    ArrayList<RecognitionSession> recognitionSessions;

    public SessionSelectionAdapter(ArrayList<RecognitionSession> recognitionSessions) {
        this.recognitionSessions = recognitionSessions;

    }

    @Override
    public int getCount() {
        return recognitionSessions.size();
    }

    @Override
    public Object getItem(int position) {
        return recognitionSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recognition_session, null);
        RecognitionSession recognitionSession = recognitionSessions.get(position);
        ((TextView) convertView.findViewById(R.id.name)).setText(recognitionSession.name);
        Device d = Realm.databaseManager.loadObject(Device.class, new Query().setTableFilters("transaction_no='" + recognitionSession.creator_device+ "'"));
        if (d != null) {
            ((TextView) convertView.findViewById(R.id.info1)).setText("Created by a "+d.model);
        }
        ((TextView) convertView.findViewById(R.id.info2)).setText(Realm.databaseManager.getRecordCount(Member.class,"session='"+recognitionSession.transaction_no+"'")+" members");



        return convertView;
    }
}
