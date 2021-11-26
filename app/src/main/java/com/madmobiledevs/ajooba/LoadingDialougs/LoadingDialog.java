package com.madmobiledevs.ajooba.LoadingDialougs;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.madmobiledevs.ajooba.R;


public class LoadingDialog {

    private Activity activity;
    private AlertDialog loadingDialog;


    public LoadingDialog(Activity currentActivity){
        activity=currentActivity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        loadingDialog = builder.create();


        LayoutInflater layoutInflater = activity.getLayoutInflater();

        builder.setView(layoutInflater.inflate(R.layout.loading_dialog_view,null));
        builder.setCancelable(false);

        loadingDialog = builder.create();
    }


    public void startLoadingDialog(){

        if (!activity.isFinishing()) {
            loadingDialog.show();
        }

    }

    public boolean isShowing(){
        return loadingDialog.isShowing();
    }

    public void dismissDialog(){
        loadingDialog.dismiss();
    }
}
