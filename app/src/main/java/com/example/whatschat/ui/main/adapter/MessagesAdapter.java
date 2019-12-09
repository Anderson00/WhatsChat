package com.example.whatschat.ui.main.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whatschat.R;
import com.example.whatschat.model.Message;
import com.example.whatschat.ui.main.MessagesHomeAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesAdapterViewHolder> {

    private List<Message> msgs;
    private String uuid;

    public MessagesAdapter(){
        this.msgs = new ArrayList<>();
        this.uuid = FirebaseAuth.getInstance().getUid();
    }

    public void addMessage(Message msg){
        this.msgs.add(msg);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessagesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_chat_list, parent, false);
        return new MessagesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapterViewHolder holder, int position) {
        Message msg = this.msgs.get(position);
        if(uuid.equals(msg.getFromId())){
            holder.contentMsg.setGravity(Gravity.RIGHT);
        }else{
            holder.parent.setGravity(Gravity.LEFT);
        }

        holder.contentMsg.setText(msg.getContent());
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public class MessagesAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout parent;
        TextView contentMsg;

        public MessagesAdapterViewHolder(View view){
            super(view);

            parent = view.findViewById(R.id.msg_parent);
            contentMsg = view.findViewById(R.id.msg);
        }
    }
}
