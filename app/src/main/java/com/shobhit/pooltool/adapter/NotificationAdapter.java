package com.shobhit.pooltool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.model.NotificationObject;
import com.shobhit.pooltool.R;

import java.util.List;

/**
 * Created by Shobhit-pc on 1/8/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationObject> allNotificationsLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView notification,date;
        ImageView ivNotification;

        public MyViewHolder(View view) {
            super(view);
            notification = (TextView) view.findViewById(R.id.textViewNotificationMessage);
            date = (TextView) view.findViewById(R.id.textViewNotificationDate);
            ivNotification = (ImageView) view.findViewById(R.id.imageViewNotificationImage);
        }
    }


    public NotificationAdapter(List<NotificationObject> notificationList) {
        this.allNotificationsLists = notificationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notificationview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
        NotificationObject notificationObject = allNotificationsLists.get(position);
        holder.notification.setText(notificationObject.getNotificationView());
        holder.date.setText(notificationObject.getDate());
        holder.ivNotification.setImageResource(notificationObject.getImage());
    }

    @Override
    public int getItemCount() {
        return allNotificationsLists.size();
    }
}