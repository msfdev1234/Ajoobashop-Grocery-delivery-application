package com.madmobiledevs.ajooba;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrdersActivity extends AppCompatActivity {

    private RelativeLayout tab1_Btn, tab2_Btn, tab3_Btn;
    private TextView active_TxtVw, cancelled_TxtVw, completed_TextVw;
    private View line1, line2, line3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        tab1_Btn = findViewById(R.id.tab1_Btn_Orders);
        tab2_Btn = findViewById(R.id.tab2_Btn_Orders);
        tab3_Btn = findViewById(R.id.tab3_Btn_Orders);

        active_TxtVw = findViewById(R.id.active_TextVw);
        cancelled_TxtVw = findViewById(R.id.canceled_TextVw);
        completed_TextVw = findViewById(R.id.completed_TextVw);

        line1 =findViewById(R.id.active_Line);
        line2 =findViewById(R.id.canceled_Line);
        line3 =findViewById(R.id.completed_Line);



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_Orders,new tab1ActiveOrders()).commit();

        tab1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                active_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line1.setVisibility(View.VISIBLE);

                cancelled_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line2.setVisibility(View.INVISIBLE);

                completed_TextVw.setTextColor(Color.parseColor("#FFFFFF"));
                line3.setVisibility(View.INVISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_Orders,new tab1ActiveOrders()).commit();

            }
        });

        tab2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelled_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line2.setVisibility(View.VISIBLE);

                active_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line1.setVisibility(View.INVISIBLE);

                completed_TextVw.setTextColor(Color.parseColor("#FFFFFF"));
                line3.setVisibility(View.INVISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_Orders,new tab2CancelledOrders()).commit();
            }
        });

        tab3_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completed_TextVw.setTextColor(Color.parseColor("#FFFFFF"));
                line3.setVisibility(View.VISIBLE);

                active_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line1.setVisibility(View.INVISIBLE);

                cancelled_TxtVw.setTextColor(Color.parseColor("#FFFFFF"));
                line2.setVisibility(View.INVISIBLE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_Orders,new tab3CompletedOrders()).commit();
            }
        });


    }
}