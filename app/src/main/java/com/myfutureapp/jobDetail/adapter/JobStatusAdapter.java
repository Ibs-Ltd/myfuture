package com.myfutureapp.jobDetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.jobDetail.model.JobDetailResponse.JobDetailModel.ProgressObject;
import com.myfutureapp.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobStatusAdapter extends RecyclerView.Adapter<JobStatusAdapter.ViewHolder> {

    private final Context context;
    private final List<ProgressObject> progressObjects;
    private final int size;
    private final ScheduleInterview scheduleInterview;


    public JobStatusAdapter(Context context, List<ProgressObject> progress_obj, ScheduleInterview scheduleInterview) {
        this.context = context;
        this.progressObjects = progress_obj;
        this.scheduleInterview = scheduleInterview;
        size = progress_obj.size();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_status_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.aboveView.setVisibility(View.GONE);
        }
        if (position == size - 1) {
            holder.belowView.setVisibility(View.GONE);
        }

        holder.jobStatusCircle.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (progressObjects.get(position).show_schedule_button == 0) {
            holder.scheduleInterviewLL.setVisibility(View.GONE);
        } else {
            holder.scheduleInterviewLL.setVisibility(View.VISIBLE);
        }

        holder.statusText.setText(progressObjects.get(position).row);
        if (progressObjects.get(position).date != null) {
            holder.statusDate.setText(DateUtil.convertyyyymmddhhmmsstoddmmyyyy(progressObjects.get(position).date));
        }
        if (progressObjects.get(position).is_done == 1) {
            holder.jobStatusCircle.setImageDrawable(context.getDrawable(R.drawable.ic_green_dot));
        } else {
            holder.jobStatusCircle.setImageDrawable(context.getDrawable(R.drawable.ic_orange_dot));
        }
        holder.scheduleInterviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleInterview.scheduleInterview();
            }
        });

    }

    @Override
    public int getItemCount() {
        return progressObjects.size();
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

    public interface ScheduleInterview {
        void scheduleInterview();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View aboveView, belowView;
        CircleImageView jobStatusCircle;
        TextView statusText, statusDate;
        LinearLayout scheduleInterviewLL;

        ViewHolder(View v) {
            super(v);
            aboveView = (View) v.findViewById(R.id.aboveView);
            belowView = (View) v.findViewById(R.id.belowView);
            jobStatusCircle = (CircleImageView) v.findViewById(R.id.jobStatusCircle);
            statusText = (TextView) v.findViewById(R.id.statusText);
            statusDate = (TextView) v.findViewById(R.id.statusDate);
            scheduleInterviewLL = (LinearLayout) v.findViewById(R.id.scheduleInterviewLL);
        }
    }
}