package com.myfutureapp.dashboard.application.adapter;

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
import com.myfutureapp.dashboard.application.model.OldApplicationResponse.OldApplicationModel.HistoryModel;
import com.myfutureapp.util.DateUtil;
import com.myfutureapp.util.Helper;

import java.util.List;

public class OldApplicationAdapter extends RecyclerView.Adapter<OldApplicationAdapter.ViewHolder> {

    List<HistoryModel> list;
    private final Context context;

    public OldApplicationAdapter(Context context, List<HistoryModel> list) {
        this.context = context;
        this.list = list;
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
        if (list.get(position).web_link != null) {
            holder.id_Link.setText(list.get(position).web_link);
        }

        if (list.get(position).rejection.date != null) {
            holder.id_date.setText(DateUtil.convertyyyymmddhhmmsstoddmmyyyy(list.get(position).rejection.date));
        }
        if (Helper.isContainValue(list.get(position).rejection.row)) {
            holder.id_application_status.setText(list.get(position).rejection.row);
            holder.id_application_status.setVisibility(View.VISIBLE);
        } else {
            holder.id_application_status.setVisibility(View.GONE);
        }
        if (Helper.isContainValue(list.get(position).rejection.color)) {
            if (list.get(position).rejection.color.equalsIgnoreCase("orange")) {
                holder.id_application_status.setTextColor(context.getResources().getColor(R.color.orange));
            } else if (list.get(position).rejection.color.equalsIgnoreCase("red")) {
                holder.id_application_status.setTextColor(context.getResources().getColor(R.color.tomato));
            } else if (list.get(position).rejection.color.equalsIgnoreCase("green")) {
                holder.id_application_status.setTextColor(context.getResources().getColor(R.color.green));
            }
        }

        holder.scheduleInterviewLL.setVisibility(View.GONE);
        holder.scheduleInterviewText.setVisibility(View.GONE);


        holder.jobItemMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holder.scheduleInterviewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(List<HistoryModel> dataItemList) {
        this.list.clear();
        this.list.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void addMoreData(List<HistoryModel> dataItemList) {
        this.list.addAll(dataItemList);
        notifyDataSetChanged();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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