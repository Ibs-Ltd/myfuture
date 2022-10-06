package com.myfutureapp.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.util.DateUtil;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class OngoingApplicationAdapter extends RecyclerView.Adapter<OngoingApplicationAdapter.ViewHolder> {

    private final Context context;
    private final JobSelectedForDetail jobSelectedForDetail;
    List<OngoingApplicationsResponse.OngoingApplicationdetail> list = new ArrayList<>();

    public OngoingApplicationAdapter(Context context, List<OngoingApplicationsResponse.OngoingApplicationdetail> list, JobSelectedForDetail jobSelectedForDetail) {
        this.context = context;
        this.list = list;
        this.jobSelectedForDetail = jobSelectedForDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_applications_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (list.get(position).company_name != null) {
            holder.id_company_name.setText(list.get(position).company_name);
        }
        if (list.get(position).designation != null) {
            holder.id_job_title.setText(list.get(position).designation);
        }
        /*if (list.get(position).web_link != null) {
            holder.id_Link.setText(list.get(position).web_link);
        }*/

        if (list.get(position).opportunity_applied_datetime != null) {
            holder.id_date.setText(DateUtil.convertyyyymmddhhmmsstoddmmyyyy(list.get(position).opportunity_applied_datetime));
        }
        if (list.get(position).show_schedule_button == 0) {
            if (Helper.isContainValue(list.get(position).nextStep.row)) {
                holder.id_application_status.setText(list.get(position).nextStep.row);
                holder.id_application_status.setVisibility(View.VISIBLE);
            } else {
                holder.id_application_status.setVisibility(View.GONE);
            }
            if (Helper.isContainValue(list.get(position).nextStep.color)) {
                if (list.get(position).nextStep.color.equalsIgnoreCase("orange")) {
                    holder.id_application_status.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (list.get(position).nextStep.color.equalsIgnoreCase("red")) {
                    holder.id_application_status.setTextColor(context.getResources().getColor(R.color.tomato));
                } else if (list.get(position).nextStep.color.equalsIgnoreCase("green")) {
                    holder.id_application_status.setTextColor(context.getResources().getColor(R.color.green));
                }
            }

            holder.scheduleInterviewLL.setVisibility(View.GONE);
            holder.scheduleInterviewText.setVisibility(View.GONE);
        } else {
            holder.scheduleInterviewLL.setVisibility(View.VISIBLE);
            holder.scheduleInterviewText.setVisibility(View.VISIBLE);
            holder.id_application_status.setVisibility(View.GONE);
        }


        holder.jobItemMainLL.setOnClickListener(view -> jobSelectedForDetail.JobDetailSelected(list.get(position)));

        holder.scheduleInterviewLL.setOnClickListener(v -> {
            Helper.postDelayThreeSecond(holder.scheduleInterviewLL);
            Helper.postDelayThreeSecond(holder.jobItemMainLL);
            jobSelectedForDetail.scheduleInterviewTime(String.valueOf(list.get(position).id));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyItem(List<OngoingApplicationsResponse.OngoingApplicationdetail> dataItemList) {
        this.list.clear();
        this.list.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void addMoreData(List<OngoingApplicationsResponse.OngoingApplicationdetail> dataItemList) {
        int initialSize = list.size();
        this.list.addAll(dataItemList);
        notifyItemRangeInserted(initialSize, list.size());
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    public interface JobSelectedForDetail {
        void JobDetailSelected(OngoingApplicationsResponse.OngoingApplicationdetail selectedJob);

        void scheduleInterviewTime(String jobId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_company_name;
        TextView id_job_title;
        TextView id_date;
        TextView id_application_status, scheduleInterviewText;
        LinearLayout scheduleInterviewLL;
        TextView id_Link;
        CardView jobItemMainLL;


        ViewHolder(View v) {
            super(v);
            id_company_name = v.findViewById(R.id.id_company_name);
            id_date = v.findViewById(R.id.id_date);
            id_job_title = v.findViewById(R.id.id_job_title);
            id_application_status = v.findViewById(R.id.id_application_status);
            id_Link = v.findViewById(R.id.id_Link);
            jobItemMainLL = v.findViewById(R.id.jobItemMainLL);

            scheduleInterviewLL = v.findViewById(R.id.scheduleInterviewLL);
            scheduleInterviewText = v.findViewById(R.id.scheduleInterviewText);


        }
    }
}