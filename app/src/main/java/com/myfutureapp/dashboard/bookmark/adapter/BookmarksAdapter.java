package com.myfutureapp.dashboard.bookmark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private final Context context;


    public BookmarksAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 7;
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
        TextView id_company_name;
        ImageView id_company_icon;
        ImageView id_bookmark;
        TextView id_job_title;
        TextView id_job_timing_status;
        TextView id_salary;
        TextView id_work_experience;
        TextView id_location;
        TextView id_deadline_date;


        ViewHolder(View v) {
            super(v);
            id_company_name = v.findViewById(R.id.id_company_name);
            id_company_icon = v.findViewById(R.id.logo);
            id_bookmark = v.findViewById(R.id.id_bookmark);
            id_job_title = v.findViewById(R.id.id_job_title);
            id_job_timing_status = v.findViewById(R.id.id_job_timing_status);
            id_salary = v.findViewById(R.id.id_salary);
            id_work_experience = v.findViewById(R.id.id_work_experience);
            id_location = v.findViewById(R.id.id_location);
            id_deadline_date = v.findViewById(R.id.id_deadline_date);

        }
    }
}