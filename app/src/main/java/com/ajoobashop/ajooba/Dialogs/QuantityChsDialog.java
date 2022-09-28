package com.ajoobashop.ajooba.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajoobashop.ajooba.R;

public class QuantityChsDialog extends Dialog implements
        android.view.View.OnClickListener{

    private TextView quantity1, unit1, dash1, Tag_Mrp1, Mrp1, Tag_Rate1, rate1;
    private TextView quantity2, unit2, dash2, Tag_Mrp2, Mrp2, Tag_Rate2, rate2;

    private TextView product_Name_TxtVw;

    public RelativeLayout quantity1_Btn, quantity2_Btn;

    public Activity activity;

    String pName, q1, u1,  mrp1,  r1, q2, u2,  mrp2,  r2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quantity_pop_up_layout);

        product_Name_TxtVw = findViewById(R.id.product_Name_TxtVw_Dialog);

        quantity1_Btn = findViewById(R.id.Quantity1_Btn_Dialog);
        quantity2_Btn = findViewById(R.id.Quantity2_Btn_Dialog);

        quantity1 = findViewById(R.id.quantity1_Dialog);
        unit1 = findViewById(R.id.unit1_Dialog);
        dash1 = findViewById(R.id.sampleDash1);
        Tag_Mrp1 = findViewById(R.id.sampleMRP1);
        Mrp1 = findViewById(R.id.MRP1_Dialog);
        Tag_Rate1 = findViewById(R.id.sampleRate1);
        rate1 = findViewById(R.id.rate1_Dialog);

        quantity2 = findViewById(R.id.quantity2_Dialog);
        unit2 = findViewById(R.id.unit2_Dialog);
        dash2 = findViewById(R.id.sampleDash2);
        Tag_Mrp2 = findViewById(R.id.sampleMRP2);
        Mrp2 = findViewById(R.id.MRP2_Dialog);
        Tag_Rate2 = findViewById(R.id.sampleRate2);
        rate2 = findViewById(R.id.rate2_Dialog);

        Tag_Mrp1.setPaintFlags(Tag_Mrp1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Mrp1.setPaintFlags(Mrp1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Tag_Mrp2.setPaintFlags(Tag_Mrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Mrp2.setPaintFlags(Mrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        quantity1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(activity, "1st quantity", Toast.LENGTH_SHORT).show();
            }
        });

        quantity2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Toast.makeText(activity, "2nd quantity", Toast.LENGTH_SHORT).show();
            }
        });

        setTexts();

    }

    private void setTexts() {
        product_Name_TxtVw.setText(pName);

        quantity1.setText(q1);
        unit1.setText(u1);
        Mrp1.setText(mrp1);
        rate1.setText(r1);

        quantity2.setText(q2);
        unit2.setText(u2);
        Mrp2.setText(mrp2);
        rate2.setText(r2);
    }

    public QuantityChsDialog(Activity a, String pName,String q1,String u1, String mrp1, String r1,String q2,String u2, String mrp2, String r2) {

        super(a);
        this.activity = a;
        this.pName = pName;
        this.q1 = q1;
        this.u1 = u1;
        this.mrp1 = mrp1;
        this.r1 = r1;
        this.q2 = q2;
        this.u2 = u2;
        this.mrp2= mrp2;
        this.r2 = r2;

    }

    @Override
    public void onClick(View view) {
        /*
        if(quantity1_Btn == v.getId()) {

        } else if (quantity2_Btn == v.getId()){

        }

         */
    }
}
