package com.shobhit.pooltool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Shobhit-pc on 1/14/2017.
 */

public class ManageGroupAdapter extends RecyclerView.Adapter<ManageGroupAdapter.MyViewHolder> {

    private List<ManageGroupObject> groupMemberLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;
        ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewNotificationMessage);
            icon = (ImageView) view.findViewById(R.id.imageViewNotificationImage);
            date = (TextView) itemView.findViewById(R.id.textViewNotificationDate);

        }
    }


    public ManageGroupAdapter(List<ManageGroupObject> groupMemberList) {
        this.groupMemberLists = groupMemberList;
    }

    @Override
    public ManageGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificationview, parent, false);

        return new ManageGroupAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ManageGroupAdapter.MyViewHolder holder, int position) {
        ManageGroupObject manageGroupObject = groupMemberLists.get(position);
        holder.name.setText(manageGroupObject.getName());
        holder.icon.setImageResource(manageGroupObject.getImage());
        if(manageGroupObject.getAdmin().equals("1"))
            holder.date.setText("Admin");
        else if(manageGroupObject.getAdmin().equals("0"))
            holder.date.setText("not Admin");
        else
            holder.date.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return groupMemberLists.size();
    }
}