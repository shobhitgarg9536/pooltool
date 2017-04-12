package com.shobhit.pooltool.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.R;

/**
 * Created by Shobhit-pc on 1/3/2017.
 */

public class CheckInternetConnectivity {
    AlertDialog.Builder alertDialog;
        public static boolean isNetConnected(Context mContext) {
            NetworkInfo netInfo = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    public void alertDialog( Context mContext){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        ImageView balanceimage = (ImageView) dialog.findViewById(R.id.imageViewalert);
        balanceimage.setImageResource(R.drawable.interneterror);
        TextView alerttext = (TextView) dialog.findViewById(R.id.textViewalertdialog);
        alerttext.setText("Network unavailable. Please try again later");
        Button yesButton = (Button) dialog.findViewById(R.id.buttonYesAlertDialog);
        yesButton.setText("OK");
        Button noButton = (Button) dialog.findViewById(R.id.buttonNoAlertDialog);
        noButton.setText("See Details");
            noButton.setVisibility(View.GONE);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    }