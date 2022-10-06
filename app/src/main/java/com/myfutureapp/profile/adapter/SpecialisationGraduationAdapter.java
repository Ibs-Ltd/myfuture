package com.myfutureapp.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.profile.model.SpecialisationGraduationModel;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class SpecialisationGraduationAdapter extends RecyclerView.Adapter<SpecialisationGraduationAdapter.ViewHolder> {

    private final Context context;
    private final List<SpecialisationGraduationModel> specialisationList;
    private final SpecialisationCourseSelected courseSelected;

    public SpecialisationGraduationAdapter(Context context, List<SpecialisationGraduationModel> specialisationList, SpecialisationCourseSelected courseSelected) {
        this.context = context;
        this.specialisationList = specialisationList;
        this.courseSelected = courseSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_detail_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Helper.isContainValue(specialisationList.get(position).courseName)) {
            holder.courseName.setText(specialisationList.get(position).courseName);
        }
        holder.courseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseSelected.courseChoosen(specialisationList.get(position).courseName);
                for (int i = 0; i < specialisationList.size(); i++) {
                    specialisationList.get(i).isSelected = false;
                }
                specialisationList.get(position).isSelected = true;
                notifyDataSetChanged();
            }
        });
        if (!specialisationList.get(position).isSelected) {
            holder.courseLL.setBackground(context.getResources().getDrawable(R.drawable.grey_btn));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.warm_gray));

        } else {
            holder.courseLL.setBackground(context.getResources().getDrawable(R.drawable.orange_btn));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.white));

        }
    }

    public void setPoistionSelected(int position) {
        for (int i = 0; i < specialisationList.size(); i++) {
            specialisationList.get(i).isSelected = false;
        }
        specialisationList.get(position).isSelected = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return specialisationList.size();
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

    public interface SpecialisationCourseSelected {

        void courseChoosen(String subcourseName);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        LinearLayout courseLL;

        ViewHolder(View v) {
            super(v);
            courseLL = v.findViewById(R.id.courseLL);
            courseName = v.findViewById(R.id.courseName);
        }
    }
}