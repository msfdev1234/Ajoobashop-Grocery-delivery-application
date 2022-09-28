package com.ajoobashop.ajooba.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ajoobashop.ajooba.R;

public class TimingsChsDialog extends Dialog implements
        android.view.View.OnClickListener{

    public TextView time1_Btn, time2_Btn, time3_Btn, time4_Btn, time5_Btn, time6_Btn, time7_Btn;
    public TextView day_TxtVw;

    private String day;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timings_pop_up_layout);

        time1_Btn = findViewById(R.id.time1);
        time2_Btn = findViewById(R.id.time2);
        time3_Btn = findViewById(R.id.time3);
        time4_Btn = findViewById(R.id.time4);
        time5_Btn = findViewById(R.id.time5);
        time6_Btn = findViewById(R.id.time6);
        time7_Btn = findViewById(R.id.time7);

        day_TxtVw = findViewById(R.id.day_Txt_Vw);

        setTexts(day);

    }

    private void setTexts(String day) {

        day_TxtVw.setText(this.day);
    }

    public TimingsChsDialog(Activity a, String day) {
        super(a);

        this.day = day;
        this.activity = a;

    }

    @Override
    public void onClick(View view) {

    }
}
