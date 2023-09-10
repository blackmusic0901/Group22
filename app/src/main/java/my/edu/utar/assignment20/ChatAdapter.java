package my.edu.utar.assignment20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int USER_MESSAGE_VIEW = 0;
    private static final int OTHER_MESSAGE_VIEW = 1;
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).isUserMessage()) {
            return USER_MESSAGE_VIEW;
        } else {
            return OTHER_MESSAGE_VIEW;
        }
    }

    // ChatAdapter.java
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == USER_MESSAGE_VIEW) {
            view = inflater.inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_other_message, parent, false);
            return new OtherMessageViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (viewHolder.getItemViewType() == USER_MESSAGE_VIEW) {
            ((UserMessageViewHolder) viewHolder).bind(chatMessage);
        } else {
            ((OtherMessageViewHolder) viewHolder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // ViewHolder for user messages
    private static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView userMessageTextView;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
        }

        void bind(ChatMessage chatMessage) {
            userMessageTextView.setText(chatMessage.getText());
        }
    }

    // ViewHolder for other users' messages
    private static class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView otherMessageTextView;

        OtherMessageViewHolder(View itemView) {
            super(itemView);
            otherMessageTextView = itemView.findViewById(R.id.otherMessageTextView);
        }

        void bind(ChatMessage chatMessage) {
            otherMessageTextView.setText(chatMessage.getText());
        }
    }
}
