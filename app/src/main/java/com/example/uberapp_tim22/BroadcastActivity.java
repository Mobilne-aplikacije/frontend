package com.example.uberapp_tim22;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.uberapp_tim22.adapters.BroadcastAdapter;
import com.example.uberapp_tim22.adapters.ChatAdapter;
import com.example.uberapp_tim22.service.ServiceUtils;
import com.example.uberapp_tim22.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastActivity extends AppCompatActivity {

    private BroadcastAdapter adapter;
    private BroadcastReceiver broadcastReceiver;
    private List<String> data;
    private Timer t = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_proba);
        data = new ArrayList<>();
        data.add("asd");

        Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
        intentUserService.putExtra("method", "getPassengers");

        startService(intentUserService);



        RecyclerView broadcastAdapterRecycler = findViewById(R.id.listView);

        adapter = new BroadcastAdapter(this, this.data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        broadcastAdapterRecycler.setLayoutManager(layoutManager);
        broadcastAdapterRecycler.setAdapter(adapter);
        Log.i("Adapter","ADAPTER");

        setBroadcastLoadPassengers();

    }

    private void setBroadcastLoadPassengers() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                List<String> strings = extras.getStringArrayList("passengers");
                data.clear();
                Log.i("ASD", "ASD");
                for (String d : strings) {
                    data.add(d);
                }

                adapter.notifyDataSetChanged();
                t.schedule(new TimerTask() {
                    public void run () {
                        getPassengers();
                        }
                    },2000);
            }
        };


        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("broadcastActivity"));
    }


    private void getPassengers() {
        Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
        intentUserService.putExtra("method", "getPassengers");
        startService(intentUserService);
    }
}
