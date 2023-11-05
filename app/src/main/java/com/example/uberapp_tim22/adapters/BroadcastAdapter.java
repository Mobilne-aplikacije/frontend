package com.example.uberapp_tim22.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.model.HopInMessage;

import java.util.List;

public class BroadcastAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> messages;

    public BroadcastAdapter(Context context, List<String> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_broadcast_proba, parent, false);
        return new BroadcastAdapter.Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class Holder extends RecyclerView.ViewHolder {
        ListView messageText;

        private Holder(View itemView) {
            super(itemView);
            messageText = (ListView) itemView.findViewById(R.id.listView);
        }

//        void bind(HopInMessage message) {
//            messageText.setText(message.getMessage());
//
//            timeText.setText(message.getTime());
//        }
    }
}

