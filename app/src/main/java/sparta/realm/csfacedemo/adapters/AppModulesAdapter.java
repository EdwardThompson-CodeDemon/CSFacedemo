package sparta.realm.csfacedemo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sparta.realm.csfacedemo.R;
import sparta.realm.spartautils.app_control.models.module;



public class AppModulesAdapter extends RecyclerView.Adapter<AppModulesAdapter.view> {

    Context cntxt;
    public ArrayList<module> items;
    onItemClickListener listener;

    public interface onItemClickListener {

        void onItemClick(module mem, View view);
    }


    public AppModulesAdapter(ArrayList<module> items, onItemClickListener listener) {
        this.cntxt = cntxt;
        this.items = items;
        this.listener = listener;


    }

    @NonNull
    @Override
    public view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.cntxt = parent.getContext();
        View view = LayoutInflater.from(cntxt).inflate(R.layout.item_module, parent, false);

        return new view(view);
    }

    @Override
    public void onBindViewHolder(@NonNull view holder, int position) {
        module obj = items.get(position);
        holder.moduleName.setText(obj.name);

        holder.icon.setImageDrawable(obj.icon);


        holder.icon.setColorFilter(Color.GRAY);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(obj, holder.itemView);

            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class view extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView moduleName;
        public String sid;
        public ImageView icon;


        view(View itemView) {
            super(itemView);

            moduleName = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);


        }

        @Override
        public void onClick(View view) {

        }
    }
}
