package com.myfutureapp.dashboard.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;

import java.util.List;

public class StateListAdapter extends RecyclerView.Adapter<StateListAdapter.myHolder> {
    int index;
    private final List<ProfileStateListResponseModel.ProfileStateListDataModel> statelistG;
    private final Context context;
    private final StateSelected stateSelected;

    public StateListAdapter(Context context, List<ProfileStateListResponseModel.ProfileStateListDataModel> statelistG, StateSelected stateSelected) {
        this.context = context;
        this.statelistG = statelistG;
        this.stateSelected = stateSelected;
    }

    @NonNull
    @Override
    public StateListAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.state_list_adapter_layout, parent, false);
        return new StateListAdapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateListAdapter.myHolder holder, int position) {
        holder.tv_state_name.setText("   " + statelistG.get(position).name);
        holder.tv_state_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateSelected.stateSelected(statelistG.get(position));
            }
        });

       /* if (index==position){
            holder.tv_state_name.setBackgroundColor(context.getResources().getColor(R.color.apptheme));
        }
        else {
            holder.tv_state_name.setBackgroundColor(context.getResources().getColor(R.color.white_color));

        }*/


    }

    public void notifyItem(List<ProfileStateListResponseModel.ProfileStateListDataModel> dataItemList) {
        this.statelistG.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void notifyItemPostion(int postion, ProfileStateListResponseModel.ProfileStateListDataModel dataItemList) {
        this.statelistG.set(postion, dataItemList);
        notifyDataSetChanged();
    }

    public void clearAll() {
        statelistG.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statelistG.size();
    }

    public interface StateSelected {
        void stateSelected(ProfileStateListResponseModel.ProfileStateListDataModel profileStateListData);
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tv_state_name;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            tv_state_name = itemView.findViewById(R.id.tv_state_name);
        }
    }
}

