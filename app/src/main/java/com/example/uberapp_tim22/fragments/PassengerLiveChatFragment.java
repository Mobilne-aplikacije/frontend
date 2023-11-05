package com.example.uberapp_tim22.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim22.DTO.IdAndEmailDTO;
import com.example.uberapp_tim22.DTO.NewLocationDTO;
import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.adapters.MessageAdapter;
import com.example.uberapp_tim22.model.enums.VehicleName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class PassengerLiveChatFragment extends Fragment {

    private WebSocket webSocket;
    private WebSocket webSocket2;

    private MessageAdapter adapter;
    private ListView messsageList;
    private EditText messageBox;
    private TextView send;
    private Long driverId, rideId, myId;
    private SharedPreferences sharedPreferences;

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_chat, container, false);

        messsageList = view.findViewById(R.id.messageList);
        messageBox = view.findViewById(R.id.messageBox);
        send = view.findViewById(R.id.send);

//        sharedPreferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        myId = sharedPreferences.getLong("pref_myId", 0);
//        driverId = sharedPreferences.getLong("pref_driverId", 0);
//        rideId = sharedPreferences.getLong("pref_rideId", 0);
//
//        Log.d("Fragment live chat myId", String.valueOf(myId));
//        Log.d("Fragment live chat driverId", String.valueOf(driverId));
//        Log.d("Fragment live chat rideId", String.valueOf(rideId));

        instantiateWebSocket();

        adapter = new MessageAdapter(getContext());
        messsageList.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString();
                if (!message.isEmpty()) {
                    webSocket2.send(message);
                    messageBox.setText("");

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", message);
                        jsonObject.put("byServer", false);
                        adapter.addItem(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    private void instantiateWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.1.17:8084/socket/websocket").build();
        SocketListener socketListener = new SocketListener();
        webSocket = client.newWebSocket(request, socketListener);
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Connection Established!", Toast.LENGTH_LONG).show();

                // Create a new WebSocket instance for sending messages to another URL
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("ws://192.168.1.17:8084/chat").build();
                webSocket2 = client.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onOpen(WebSocket webSocket, Response response) {
                        // This method is called when the new WebSocket connection is established
                        // You can perform any necessary actions here
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, String text) {
                        getActivity().runOnUiThread(() -> {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("message", text);
                                jsonObject.put("byServer", true);
                                adapter.addItem(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    // Implement other methods as needed

                    // Note: You can use the same pattern as the SocketListener class
                    // to implement the required methods for webSocket2
                });

                // Send a message to the new WebSocket instance
                webSocket2.send("Hello from the new WebSocket instance!");
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            getActivity().runOnUiThread(() -> {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", text);
                    jsonObject.put("byServer", true);
                    adapter.addItem(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            if (webSocket != null) {
                webSocket.cancel();
            }        }

        @Override
        public void onFailure(WebSocket webSocket, final Throwable t, @Nullable final Response response) {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "No connection Established!", Toast.LENGTH_LONG).show();
            });
        }
    }
}



