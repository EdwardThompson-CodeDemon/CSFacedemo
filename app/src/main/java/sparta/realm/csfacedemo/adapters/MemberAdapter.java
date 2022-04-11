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
import sparta.realm.csfacedemo.models.Member;
import sparta.realm.csfacedemo.models.MemberImage;
import sparta.realm.csfacedemo.utils.FastScrolRecyclerview.FastScrollRecyclerViewInterface;
import sparta.realm.spartautils.svars;



public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.view>  implements FastScrollRecyclerViewInterface {

    Context cntxt;
    public ArrayList<Member> Members;
    onItemClickListener listener;
    HashMap<String, Integer> mapIndex;

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return mapIndex;
    }

    public interface onItemClickListener {

        void onItemClick(Member mem, View view);
    }


    public MemberAdapter(ArrayList<Member> Members, HashMap<String, Integer> mapIndex, onItemClickListener listener) {
        this.Members = Members;
        this.mapIndex = mapIndex;
        this.listener = listener;


    }

    @NonNull
    @Override
    public view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cntxt=parent.getContext();
        View view = LayoutInflater.from(cntxt).inflate(R.layout.item_member, parent, false);

        return new view(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view holder, int position) {
        Member obj = Members.get(position);
        MemberImage mi = Realm.databaseManager.loadObject(MemberImage.class, new Query().setTableFilters("member_id='" + obj.transaction_no + "'"));
        obj.profile_photo = mi;
        if(mi!=null&&mi.image!=null) {
            holder.icon.setImageURI(null);
            holder.icon.setImageURI(Uri.parse(Uri.parse(svars.current_app_config(Realm.context).file_path_employee_data) + obj.profile_photo.image));
        }
        holder.name.setText(obj.name);
        holder.member_no.setText(obj.member_no);

        holder.itemView.setOnClickListener(view -> listener.onItemClick(obj,holder.itemView));


    }


    @Override
    public int getItemCount() {
        return Members.size();
    }

    public class view extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, member_no;
        public CircleImageView icon;


        view(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            member_no = itemView.findViewById(R.id.info1);
            icon = itemView.findViewById(R.id.icon);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
