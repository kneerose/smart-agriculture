package com.example.smartagriculture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button on, off,start,stop,onc,offc;
    TextView tempv,humidityv,date,day;
    DatabaseReference temp,hmdty,soil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        on = findViewById(R.id.b1);
        off = findViewById(R.id.b2);
        start =findViewById(R.id.wstart);
        stop=findViewById(R.id.wstop);
        onc=findViewById(R.id.c1);
        offc =findViewById(R.id.c2);
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date =findViewById(R.id.date);
        date.setText(date_n);
        String weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        day =findViewById(R.id.day);
        day.setText(weekday_name);
        tempv=findViewById(R.id.temperaturev);
        humidityv=findViewById(R.id.humidityv);
        temp= FirebaseDatabase.getInstance().getReference().child("temperature").child("Value");
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String soil = dataSnapshot.getValue(String.class);
                tempv.setText(soil);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
            }
        });
        soil =FirebaseDatabase.getInstance().getReference().child("Soil_moisture").child("Value");
        soil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ss=dataSnapshot.getValue(String.class);
                if (ss.equals("Dry"))
                {
                    notificationd();
                }
                else if (ss.equals("Wet"))
                {
                    notificationw();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT);
            }
        });
        hmdty=FirebaseDatabase.getInstance().getReference().child("humidity").child("Value");
        hmdty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String hm=dataSnapshot.getValue(String.class);
                humidityv.setText(hm);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);
            }
        });
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Fan");

                myRef.setValue("ON");
                on.setBackgroundResource(R.drawable.off);
                off.setBackgroundResource(R.drawable.on);


            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Fan");

                myRef.setValue("OFF");
                on.setBackgroundResource(R.drawable.on);
                off.setBackgroundResource(R.drawable.off);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Water_pump");

                myRef.setValue("ON");
                start.setBackgroundResource(R.drawable.off);
                stop.setBackgroundResource(R.drawable.on);

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Water_pump");

                myRef.setValue("OFF");
                stop.setBackgroundResource(R.drawable.off);
                start.setBackgroundResource(R.drawable.on);
            }
        });
        onc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Coil");

                myRef.setValue("ON");
                onc.setBackgroundResource(R.drawable.off);
                offc.setBackgroundResource(R.drawable.on);
            }
        });
        offc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Coil");

                myRef.setValue("OFF");
                offc.setBackgroundResource(R.drawable.off);
                onc.setBackgroundResource(R.drawable.on);
            }
        });
        WebView myWebView = findViewById(R.id.webview);
        myWebView.loadUrl("http://kalimatimarket.gov.np/daily-price-information");
        myWebView.setInitialScale(1);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pulltorefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void refreshData() {
        WebView myWebView = findViewById(R.id.webview);
        myWebView.loadUrl("http://kalimatimarket.gov.np/daily-price-information");
        myWebView.setInitialScale(1);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
    }

    public void goToSo(View view) {
        goToUrl("https://garden.org/learn/library/foodguide/veggie/#cat301");
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    private void notificationw ()
    {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.assistance)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.assistance))
                .setContentTitle("Alert!!")
                .setContentText("Soil is Wet")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        Uri path =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Alert";
           String description = "Soil is Wet";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,  NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(0,builder.build());

    }
    private void notificationd ()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.assistance)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.assistance))
                .setContentTitle("Alert!!")
                .setContentText("Soil is dry")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        Uri path =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "02";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Alert";
            String description = "Soil is dry";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }
        notificationManager.notify(1,builder.build());
        
    }

}