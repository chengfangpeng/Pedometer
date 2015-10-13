package com.cnwir.pedometer.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.cnwir.pedometer.R;

/**
 * Created by cfp on 15-9-8.
 */
public class CommonDialog extends DialogFragment{

    private OnPositiveListener mPListener;

    private OnNegativeListener mNListener;

    private Activity mActivity;

    private String postiveStr;

    private String negativeStr;

    private String title;


    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.common_dialog_layout, null, false);



        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setPositiveButton(postiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPListener.onClick();
            }
        });
        builder.setNegativeButton(negativeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setView(view);
        Dialog dialog = builder.create();
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private String getValue(final int resId) {
        return mActivity.getString(resId);
    }

    public interface  OnPositiveListener {
        void onClick();
    }

    public interface OnNegativeListener{

        void onClick();
    }

    public void setPositiveStr(String positiveStr){

        this.postiveStr = positiveStr;
    }

    public void setNegativeStr(String negativeStr){

        this.negativeStr = negativeStr;
    }

    public void setTitle(String title){

        this.title = title;
    }

    public void setPositiveListener(OnPositiveListener mListener){
        this.mPListener = mListener;

    }
    public void setNegativeListener(OnNegativeListener mNListener){

        this.mNListener = mNListener;
    }



}
