package com.myfutureapp.dashboard.adapter;

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
import com.myfutureapp.util.Helper;

import java.util.ArrayList;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> navData;
    private final NavigationSelection navigationSelection;

    public NavAdapter(Context context, ArrayList<String> navData, NavigationSelection navigationSelection) {
        this.context = context;
        this.navData = navData;
        this.navigationSelection = navigationSelection;
    }

    @NonNull
    @Override
    public NavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Helper.isContainValue(navData.get(position))) {
            holder.navName.setText(navData.get(position));
        }
        if (navData.get(position).equalsIgnoreCase("Home")) {
            holder.nav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.nav_home));
        } else if (navData.get(position).equalsIgnoreCase("Applications")) {
            holder.nav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_application_nav));
        } else if (navData.get(position).equalsIgnoreCase("Bookmarks")) {
            holder.nav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.nav_bookmarks));
        } else if (navData.get(position).equalsIgnoreCase("Profile")) {
            holder.nav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile_nav));
        } else if (navData.get(position).equalsIgnoreCase("Sign Out")) {
            holder.nav_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.nav_sign_out));
        }
        holder.navMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationSelection.navigationSelected(navData.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return navData.size();
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

    public interface NavigationSelection {

        void navigationSelected(String navTitle, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView navName;
        LinearLayout navMain;
        ImageView nav_icon;

        ViewHolder(View v) {
            super(v);
            navMain = v.findViewById(R.id.navMain);
            navName = v.findViewById(R.id.navName);
            nav_icon = v.findViewById(R.id.nav_icon);
        }
    }
}
