package com.madmobiledevs.ajooba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Vegeteable_SubCategory_Activity extends AppCompatActivity {

    private CardView non_Chopped_Btn, chopped_Btn;

    private TextView topText;
    private String choosen_Value;
    private String valueCategory;
    private ImageView back_Btn_ImgVw;
    private RelativeLayout wholeSubCategory, fullLayout;
    private ImageView empty_Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegeteable__sub_category_);

        topText = findViewById(R.id.top_TxtVw_subCategory);
        back_Btn_ImgVw = findViewById(R.id.back_Btn_subCategory);
        wholeSubCategory =findViewById(R.id.whole_subCategory);
        fullLayout = findViewById(R.id.full_Layoyt_SubCat);
        empty_Icon = findViewById(R.id.empty_subCategory_ImgVw);

        valueCategory = getIntent().getStringExtra("category");
        topText.setText(valueCategory);



        non_Chopped_Btn = findViewById(R.id.Non_chopped_Veg_Chs_Btn_Home);
        chopped_Btn = findViewById(R.id.chopped_Veg_Chs_Btn_Home);

        non_Chopped_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosen_Value = "Non-Chopped";

                Intent intent = new Intent(Vegeteable_SubCategory_Activity.this,ProductActivity.class);
                intent.putExtra("valueSubCategory", choosen_Value);
                intent.putExtra("valueCategory", valueCategory);
                startActivity(intent);
            }
        });

        chopped_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosen_Value = "Chopped";

                Intent intent = new Intent(Vegeteable_SubCategory_Activity.this,ProductActivity.class);
                intent.putExtra("valueSubCategory", choosen_Value);
                intent.putExtra("valueCategory", valueCategory);
                startActivity(intent);
            }
        });

        back_Btn_ImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vegeteable_SubCategory_Activity.super.onBackPressed();
            }
        });

        if (!valueCategory.equals("vegetables")){
            wholeSubCategory.setVisibility(View.INVISIBLE);
            fullLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            empty_Icon.setVisibility(View.VISIBLE);
        }
    }

}