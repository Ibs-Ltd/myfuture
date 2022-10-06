package com.myfutureapp.dashboard.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileWorkExperienceAdapter extends RecyclerView.Adapter<ProfileWorkExperienceAdapter.myHolder> {

    List<UserProfileResponse.UserDataModel.WorkExperinceData> work_experience_data;
    WorkSelect workSelect;
    private final Context context;

    public ProfileWorkExperienceAdapter(Context context, List<UserProfileResponse.UserDataModel.WorkExperinceData> work_experience_data, WorkSelect workSelect) {
        this.work_experience_data = work_experience_data;
        this.context = context;
        this.workSelect = workSelect;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.work_experiene_adapter_view, parent, false);
        return new myHolder(view);
    }

    public void notifyItem(List<UserProfileResponse.UserDataModel.WorkExperinceData> work_experience_data) {
        this.work_experience_data.clear();
        this.work_experience_data.addAll(work_experience_data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.tvDesignation.setText(work_experience_data.get(position).designation);
        holder.tvCompany.setText(work_experience_data.get(position).company);
        try {
            String myDate = work_experience_data.get(position).start_date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(myDate);
            long millis = date.getTime();
            holder.tvStartMonth.setText(DateUtil.mnthName(millis) + " " + DateUtil.yearCalender(millis) + " - ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if (work_experience_data.get(position).end_date != null && work_experience_data.get(position).end_date.length() > 0) {
                String myDate = work_experience_data.get(position).end_date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(myDate);
                long millis = date.getTime();
                holder.tvEndMonth.setText(DateUtil.mnthName(millis) + " " + DateUtil.yearCalender(millis));
            } else {
                holder.tvEndMonth.setText("Present");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (work_experience_data.get(position).role.length() != 0) {
            holder.tvJobtype.setText(" (" + work_experience_data.get(position).role + ")");
        }
        holder.tvCtc.setText(work_experience_data.get(position).salary + " in Lakhs per Annum (CTC)");
//        holder.tvJobdescription.setText(work_experience_data.get(position).job_description);
        holder.experinceMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workSelect.workSelect(work_experience_data.get(position));
            }
        });
        /*holder.tv_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickEvent.onItemClicked(view,position);
                index=position;
                notifyDataSetChanged();

            }
        });*/

       /* if (index==position){
            holder.tv_city_name.setBackgroundColor(context.getResources().getColor(R.color.apptheme));
        }
        else {
            holder.tv_city_name.setBackgroundColor(context.getResources().getColor(R.color.white_color));

        }
*/

    }

    @Override
    public int getItemCount() {
        return work_experience_data.size();
    }

    public interface WorkSelect {
        void workSelect(UserProfileResponse.UserDataModel.WorkExperinceData workExperinceData);
    }

    public class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDesignation, tvCompany, tvStartMonth, tvEndMonth, tvJobtype, tvCtc, tvJobdescription;
        LinearLayout experinceMainCard;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            experinceMainCard = itemView.findViewById(R.id.experinceMainCard);
            tvDesignation = itemView.findViewById(R.id.tv_Designation);
            tvCompany = itemView.findViewById(R.id.tv_Company);
            tvStartMonth = itemView.findViewById(R.id.tv_StartMonth);
            tvEndMonth = itemView.findViewById(R.id.tv_EndMonth);
            tvJobtype = itemView.findViewById(R.id.tv_Jobtype);
            tvCtc = itemView.findViewById(R.id.tv_ctc);
            tvJobdescription = itemView.findViewById(R.id.tv_jobdescription);
            // tv_city_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // onItemClickEvent.onItemClicked(view,getAdapterPosition());
        }
    }

   /* public void setOnItemClicked(OnItemClickEvent onItemClickEvent){
        this.onItemClickEvent=onItemClickEvent;
    }*/
}

