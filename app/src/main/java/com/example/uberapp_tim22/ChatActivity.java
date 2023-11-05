package com.example.uberapp_tim22;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim22.DTO.ChatMessagesDTO;
import com.example.uberapp_tim22.DTO.HopInInboxReturnedDTO;
import com.example.uberapp_tim22.DTO.HopInMessageDTO;
import com.example.uberapp_tim22.DTO.HopInUserReturnedDTO;
import com.example.uberapp_tim22.DTO.HopInMessageReturnedDTO;
import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.adapters.ChatAdapter;
import com.example.uberapp_tim22.model.HopInMessage;
import com.example.uberapp_tim22.service.UserService;
import com.example.uberapp_tim22.tools.Globals;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecycler;
    private ChatAdapter chatAdapter;
    private EditText messageET;
    private List<HopInMessage> allMessages;
    private Timer t = new Timer();
    private ResponseChatDTO inbox;
    private BroadcastReceiver broadcastReceiver;
    private Long globalId;
    private Long myId, otherId, rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        globalId = sharedPreferences.getLong("pref_id", 0L);

        Bundle bundle = getIntent().getExtras();
        ResponseChatDTO responseChatDTO = (ResponseChatDTO) bundle.getSerializable("responseChat");
        myId = (Long) bundle.getSerializable("myIdd");
        otherId = (Long) bundle.getSerializable("otherIdd");
        rideId = (Long) bundle.getSerializable("rideIdd");

        Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
        intentUserService.putExtra("method", "getMessages");
        intentUserService.putExtra("myId", myId);
        intentUserService.putExtra("otherId", otherId);

        startService(intentUserService);

        Long id = responseChatDTO.getMyId() == globalId ? responseChatDTO.getOtherId() : responseChatDTO.getMyId();
        allMessages = new ArrayList<>();
        for (ChatMessagesDTO chatMessagesDTO : responseChatDTO.getMessages()) {
            if (chatMessagesDTO.getSenderId() != globalId) {
                allMessages.add(new HopInMessage(chatMessagesDTO.getMessage(), 0, chatMessagesDTO.getSendingTime()));
            } else {
                allMessages.add(new HopInMessage(chatMessagesDTO.getMessage(), 1, chatMessagesDTO.getSendingTime()));
            }
        }

        chatRecycler = (RecyclerView) findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(this, this.allMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(chatAdapter);

        setBroadcastLoadMessages();
        getMessages();

        messageET = findViewById(R.id.enterMessageET);
        Button sendBtn = findViewById(R.id.chatSendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (messageET.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Enter message!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("PORUKE", "USAO");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    allMessages.add(0, new HopInMessage(messageET.getText().toString(), 1, LocalDateTime.now().toString()));
                }
                Log.d("PORUKE", allMessages.toString());

                chatAdapter.notifyDataSetChanged();
                HopInMessageDTO message = new HopInMessageDTO(myId, messageET.getText().toString(), rideId);
                Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
                intentUserService.putExtra("method", "sendMessage");
                intentUserService.putExtra("message", message);
                startService(intentUserService);
                messageET.setText("");
            }
        });
    }

    private void getMessages() {
        Intent intentUserService = new Intent(getApplicationContext(), UserService.class);
        intentUserService.putExtra("method", "getMessages");
        intentUserService.putExtra("myId", myId);
        intentUserService.putExtra("otherId", otherId);
        startService(intentUserService);
    }

    private void setBroadcastLoadMessages() {
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                ResponseChatDTO dto = (ResponseChatDTO) extras.get("messages");
                inbox = dto;
                allMessages.clear();

                for(ChatMessagesDTO m : dto.getMessages()) {
                    if (m.getSenderId() != myId) {
                        allMessages.add(new HopInMessage(m.getMessage(), 0, m.getSendingTime()));
                    }
                    else {
                        allMessages.add(new HopInMessage(m.getMessage(), 1, m.getSendingTime()));
                    }
                }
                chatAdapter.notifyDataSetChanged();
                t.schedule(new TimerTask() {

                    public void run() {
                        getMessages();
                    }
                }, 2000);

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("chatActivity"));


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("PAUZIRAJ", "TU");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}

