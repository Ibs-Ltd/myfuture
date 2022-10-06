package com.myfutureapp.enrollment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myfutureapp.R;
import com.myfutureapp.enrollment.model.EnrollUserResponse.EnrollModel.EnrollUserDataModel;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClearedJobAdapter extends RecyclerView.Adapter<ClearedJobAdapter.ViewHolder> {

    private final Context context;
    private final List<EnrollUserDataModel> user_data;


    public ClearedJobAdapter(Context context, List<EnrollUserDataModel> user_data) {
        this.context = context;
        this.user_data = user_data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cleared_job_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Helper.isContainValue(user_data.get(position).designation)) {
            holder.clearedRole.setText(user_data.get(position).designation);
        }
        if (Helper.isContainValue(user_data.get(position).name)) {
            holder.clearedUserName.setText(user_data.get(position).name);
        }
        if (Helper.isContainValue(user_data.get(position).company)) {
            holder.clearedCompanyName.setText(user_data.get(position).company);
        }
        if (Helper.isContainValue(user_data.get(position).profile_url)) {
            Glide.with(context)
                    .load(user_data.get(position).profile_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_vector_default_profile)
                    .into(holder.clearedUserPic);
        }
    }


    @Override
    public int getItemCount() {
        return user_data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(ArrayList dataItemList) {
//        this.graduationList.addAll(dataItemList);
        notifyDataSetChanged();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView clearedStatus, clearedCompanyName, clearedRole, clearedUserName;
        CircleImageView clearedUserPic;

        ViewHolder(View v) {
            super(v);
            clearedStatus = (TextView) v.findViewById(R.id.clearedStatus);
            clearedCompanyName = (TextView) v.findViewById(R.id.clearedCompanyName);
            clearedRole = (TextView) v.findViewById(R.id.clearedRole);
            clearedUserName = (TextView) v.findViewById(R.id.clearedUserName);
            clearedUserPic = (CircleImageView) v.findViewById(R.id.clearedUserPic);
        }
    }
}