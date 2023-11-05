package com.example.uberapp_tim22.DTO.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim22.DTO.ChatMessagesDTO;
import com.example.uberapp_tim22.DTO.HopInMessageDTO;
import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.adapters.ChatAdapter;
import com.example.uberapp_tim22.model.HopInMessage;
import com.example.uberapp_tim22.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class InboxChatFragment  extends Fragment {

    private RecyclerView chatRecycler;
    private ChatAdapter chatAdapter;
    private EditText messageET;
    private List<HopInMessage> allMessages;
    private Timer t = new Timer();
    private ResponseChatDTO inbox;
    private BroadcastReceiver broadcastReceiver;
    private Long globalId;
    private Long myId, otherId, rideId;
    private ResponseChatDTO responseChatDTO;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        globalId = sharedPreferences.getLong("pref_id", 0L);

        Bundle bundle = getArguments();
        responseChatDTO = (ResponseChatDTO) bundle.getSerializable("responseChat");
        myId = (Long) bundle.getSerializable("myIdd");
        otherId = (Long) bundle.getSerializable("otherIdd");

        Intent intentUserService = new Intent(requireContext(), UserService.class);
        intentUserService.putExtra("method", "getMessages");
        intentUserService.putExtra("myId", myId);
        intentUserService.putExtra("otherId", otherId);
        requireContext().startService(intentUserService);

        allMessages = new ArrayList<>();
        for (ChatMessagesDTO chatMessagesDTO : responseChatDTO.getMessages()) {
            if (chatMessagesDTO.getSenderId() != globalId) {
                allMessages.add(new HopInMessage(chatMessagesDTO.getMessage(), 0, chatMessagesDTO.getSendingTime()));
            } else {
                allMessages.add(new HopInMessage(chatMessagesDTO.getMessage(), 1, chatMessagesDTO.getSendingTime()));
            }
        }

        chatRecycler = view.findViewById(R.id.chatRV);
        chatAdapter = new ChatAdapter(requireContext(), allMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        chatRecycler.setLayoutManager(layoutManager);
        chatRecycler.setAdapter(chatAdapter);

        setBroadcastLoadMessages();
        getMessages();

        messageET = view.findViewById(R.id.enterMessageET);
        Button sendBtn = view.findViewById(R.id.chatSendBtn);
        sendBtn.setOnClickListener(v -> {
            if (messageET.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter message!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                allMessages.add(0, new HopInMessage(messageET.getText().toString(), 1, LocalDateTime.now().toString()));
            }


            chatAdapter.notifyDataSetChanged();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ChatMessagesDTO message = new ChatMessagesDTO(myId, otherId, messageET.getText().toString(), LocalDateTime.now().toString(), null);
                    Intent intentUserService1 = new Intent(requireContext(), UserService.class);
                    intentUserService1.putExtra("method", "sendMessage");
                    intentUserService1.putExtra("message", message);
                    requireContext().startService(intentUserService1);
                    messageET.setText("");
                }
        });

        return view;
    }

    private void getMessages() {
        Intent intentUserService = new Intent(requireContext(), UserService.class);
        intentUserService.putExtra("method", "getMessages");
        intentUserService.putExtra("myId", myId);
        intentUserService.putExtra("otherId", otherId);
        requireContext().startService(intentUserService);
    }

    private void setBroadcastLoadMessages() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                ResponseChatDTO dto = (ResponseChatDTO) extras.get("messages");
                inbox = dto;
                allMessages.clear();

                for (ChatMessagesDTO m : dto.getMessages()) {
                    if (m.getSenderId() != myId) {
                        allMessages.add(new HopInMessage(m.getMessage(), 0, m.getSendingTime()));
                    } else {
                        allMessages.add(new HopInMessage(m.getMessage(), 1, m.getSendingTime()));
                    }
                }
                chatAdapter.notifyDataSetChanged();

                new Handler(Looper.getMainLooper()).postDelayed(() -> getMessages(), 2000);
            }
        };
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("chatActivity"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }
}
