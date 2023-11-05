//package com.example.uberapp_tim22;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.uberapp_tim22.DTO.MessagingDTO;
//import com.example.uberapp_tim22.R;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;
//
//public class WebSocketActivity extends AppCompatActivity {
//
//
//    private static final String TAG = "WebSocketActivity";
//    private WebSocket webSocket;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_socket);
//
//        connectWebSocket();
//    }
//
//    private void connectWebSocket() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("ws://192.168.1.15:8085/socket") // Replace with your WebSocket server URL
//                .build();
//
//        WebSocketListener listener = new WebSocketListener() {
//            @Override
//            public void onOpen(WebSocket webSocket, Response response) {
//                super.onOpen(webSocket, response);
//                Log.d(TAG, "WebSocket connection opened");
//                // You can perform any actions upon successful connection here
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//                super.onMessage(webSocket, text);
//                Log.d(TAG, "Received message: " + text);
//                // Handle received messages from the WebSocket server
//            }
//
//            @Override
//            public void onClosed(WebSocket webSocket, int code, String reason) {
//                super.onClosed(webSocket, code, reason);
//                Log.d(TAG, "WebSocket connection closed. Code: " + code + ", Reason: " + reason);
//            }
//
//            @Override
//            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                super.onFailure(webSocket, t, response);
//                Log.e(TAG, "WebSocket connection failure", t);
//            }
//        };
//
//        webSocket = client.newWebSocket(request, listener);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (webSocket != null) {
//            webSocket.close(1000, "Activity destroyed");
//        }
//    }
////
////
////    private WebSocket webSocket;
////    private MessageAdapter adapter;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_web_socket);
////
////        ListView messageList = findViewById(R.id.messageList);
////        final EditText messageBox = findViewById(R.id.messageBox);
////        TextView send = findViewById(R.id.send);
////
////        instantiateWebSocket();
////
////        adapter = new MessageAdapter();
////        messageList.setAdapter(adapter);
////
////        send.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                String message = messageBox.getText().toString();
////
////                if (!message.isEmpty()) {
////
////                    webSocket.send(message);
////                    messageBox.setText("");
////
////                    JSONObject jsonObject = new JSONObject();
////
////                    try {
////                        jsonObject.put("message", message);
////                        jsonObject.put("byServer", false);
////
////                        adapter.addItem(jsonObject);
////
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        });
////    }
////
////    private void instantiateWebSocket() {
////
////        OkHttpClient client = new OkHttpClient();
////
////        //replace x.x.x.x with your machine's IP Address
////        Request request = new Request.Builder().url("http://192.168.1.15:8084/socket").build();
////        SocketListener socketListener = new SocketListener(this);
////        webSocket = client.newWebSocket(request, socketListener);
////    }
////
////    public class SocketListener extends WebSocketListener {
////
////        public WebSocketActivity activity;
////
////        public SocketListener(WebSocketActivity activity) {
////            this.activity = activity;
////        }
////
////        @Override
////        public void onOpen(WebSocket webSocket, Response response) {
////            Log.i("WebSocket", "onOpen: Connection Established!");
////            activity.runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    Toast.makeText(activity, "Connection Established!", Toast.LENGTH_LONG).show();
////                }
////            });
////        }
////
////        @Override
////        public void onMessage(WebSocket webSocket, final String text) {
////            Log.i("WebSocket", "onMessage: " + text);
////            activity.runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    JSONObject jsonObject = new JSONObject();
////
////                    try {
////                        jsonObject.put("message", text);
////                        jsonObject.put("byServer", true);
////
////                        adapter.addItem(jsonObject);
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
////        }
////
////        @Override
////        public void onMessage(WebSocket webSocket, ByteString bytes) {
////            Log.i("WebSocket", "onMessage(ByteString): " + bytes.utf8());
////            super.onMessage(webSocket, bytes);
////        }
////
////        @Override
////        public void onClosing(WebSocket webSocket, int code, String reason) {
////            Log.i("WebSocket", "onClosing: code=" + code + ", reason=" + reason);
////            super.onClosing(webSocket, code, reason);
////        }
////
////        @Override
////        public void onClosed(WebSocket webSocket, int code, String reason) {
////            Log.i("WebSocket", "onClosed: code=" + code + ", reason=" + reason);
////            super.onClosed(webSocket, code, reason);
////        }
////
////        @Override
////        public void onFailure(WebSocket webSocket, final Throwable t, @Nullable final Response response) {
////            Log.e("WebSocket", "onFailure: " + t.getMessage());
////            super.onFailure(webSocket, t, response);
////        }
////    }
////
////    public class MessageAdapter extends BaseAdapter {
////
////        List<JSONObject> messagesList = new ArrayList<>();
////
////        @Override
////        public int getCount() {
////            return messagesList.size();
////        }
////
////        @Override
////        public Object getItem(int i) {
////            return messagesList.get(i);
////        }
////
////        @Override
////        public long getItemId(int i) {
////            return i;
////        }
////
////        @Override
////        public View getView(int i, View view, ViewGroup viewGroup) {
////
////            if (view == null)
////                view = getLayoutInflater().inflate(R.layout.message_list_item, viewGroup, false);
////
////            TextView sentMessage = view.findViewById(R.id.sentMessage);
////            TextView receivedMessage = view.findViewById(R.id.receivedMessage);
////
////            JSONObject item = messagesList.get(i);
////
////            try {
////                if (item.getBoolean("byServer")) {
////
////                    receivedMessage.setVisibility(View.VISIBLE);
////                    receivedMessage.setText(item.getString("message"));
////                    sentMessage.setVisibility(View.INVISIBLE);
////
////                } else {
////
////                    sentMessage.setVisibility(View.VISIBLE);
////                    sentMessage.setText(item.getString("message"));
////                    receivedMessage.setVisibility(View.INVISIBLE);
////                }
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////            return view;
////        }
////
////        void addItem(JSONObject item) {
////
////            messagesList.add(item);
////            notifyDataSetChanged();
////        }
////    }
//}
