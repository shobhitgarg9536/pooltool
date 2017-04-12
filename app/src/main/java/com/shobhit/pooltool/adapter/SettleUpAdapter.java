package com.shobhit.pooltool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.R;
import com.shobhit.pooltool.model.SettleUpObject;

import java.util.List;

/**
 * Created by Shobhit-pc on 1/12/2017.
 */

public class SettleUpAdapter extends RecyclerView.Adapter<SettleUpAdapter.MyViewHolder> {

    private List<SettleUpObject> settleUpLists;
    private int flag ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contact , amount;
        public Button pay;
        public ImageView contactImage;
        final RecyclerView.ViewHolder holder = null;


        public MyViewHolder(View view) {
            super(view);
            contact = (TextView) view.findViewById(R.id.textViewSettleUpContact);
            amount = (TextView) view.findViewById(R.id.textViewSettleUpAmount);
            pay = (Button) view.findViewById(R.id.buttonSettleUpPay);
            contactImage = (ImageView) view.findViewById(R.id.imageViewsettleUpImage);

        }
    }


    public SettleUpAdapter(List<SettleUpObject> settleUpLists , int flag) {
        this.settleUpLists = settleUpLists;
        this.flag = flag;
    }

    @Override
    public SettleUpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settleuppaidview, parent, false);

        Button btpay  = (Button) itemView.findViewById(R.id.buttonSettleUpPay);
        if(flag == 0)
            btpay.setText("PAY");
        else
            btpay.setText("ASK");

        return new SettleUpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettleUpAdapter.MyViewHolder holder, int position) {
        final SettleUpObject settleUpObject = settleUpLists.get(position);
        holder.contact.setText(settleUpObject.getUserName());
        holder.amount.setText(settleUpObject.getAmount());
        holder.contactImage.setImageResource(settleUpObject.getImage());


    }


    @Override
    public int getItemCount() {
        return settleUpLists.size();
    }
}