package com.madmobiledevs.ajooba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class contactUsActivity extends AppCompatActivity {

    RelativeLayout call_Btn, whatsApp_Btn, email_Btn;

    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        call_Btn = findViewById(R.id.call_Btn_ContctUs);
        whatsApp_Btn = findViewById(R.id.whatsApp_Btn_ContctUs);
        email_Btn = findViewById(R.id.email_Btn_ContctUs);

        call_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phn_Number = "9665888225";
                makePhoneCall(phn_Number);
            }
        });

        whatsApp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+"+919665888225";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        email_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "ajoobavegetables@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);
            }
        });
    }

    private void makePhoneCall(String number) {

        if (ContextCompat.checkSelfPermission(contactUsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(contactUsActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }

    }
}