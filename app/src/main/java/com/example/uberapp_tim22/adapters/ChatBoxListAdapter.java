package com.example.uberapp_tim22.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.R;

import java.util.List;

public class ChatBoxListAdapter extends RecyclerView.Adapter<ChatBoxListAdapter.ChatBoxViewHolder>{

    private List<ResponseChatDTO> chatBoxList;
    private ChatBoxItemClickListener chatBoxItemClickListener;

    public ChatBoxListAdapter(ChatBoxItemClickListener chatBoxItemClickListener) {
        this.chatBoxItemClickListener = chatBoxItemClickListener;
    }

    public void setCheckBoxlist(List<ResponseChatDTO> chatBoxList) {
        this.chatBoxList = chatBoxList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_box_of_list, parent, false);
        return new ChatBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBoxViewHolder holder, int position) {
        ResponseChatDTO chatBox = chatBoxList.get(position);
        holder.bind(chatBox);
    }

    @Override
    public int getItemCount() {
        return chatBoxList != null ? chatBoxList.size() : 0;
    }

    class ChatBoxViewHolder extends RecyclerView.ViewHolder {

        private TextView chatBoxTitleTextView;
        private TextView chatBoxDetailsTextView;

        ChatBoxViewHolder(@NonNull View itemView) {
            super(itemView);
            chatBoxTitleTextView = itemView.findViewById(R.id.chatBoxTitleTextView);
            chatBoxDetailsTextView = itemView.findViewById(R.id.chatBoxDetailsTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ResponseChatDTO chatBox = chatBoxList.get(position);
                        chatBoxItemClickListener.onChatBoxItemClick(chatBox);
                    }
                }
            });
        }

        void bind(ResponseChatDTO chatBox) {
            chatBoxTitleTextView.setText(chatBox.getOtherName());
            chatBoxDetailsTextView.setText(chatBox.getMessages().get(chatBox.getMessages().size()-1).getMessage());
        }
    }

    public interface ChatBoxItemClickListener {
        void onChatBoxItemClick(ResponseChatDTO chatBox);
    }

}
