package com.example.whatschat.ui.main.adapter;

import com.example.whatschat.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.model.Message;
import com.example.whatschat.ui.main.MessagesHomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends BaseAdapter {

    private List<Message> msgs;
    private Context ctx;

    public MessagesAdapter(Context ctx, List<Message> msgs){
        this.ctx = ctx;
        this.msgs = msgs;
        if(this.msgs == null)
            this.msgs = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int i) {
        return msgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(ctx).inflate(R.layout.msg_chat_list, viewGroup, false);

        Message msg = (Message) getItem(i);
        TextView textViewMsg = (TextView) view.findViewById(R.id.msg);
        LinearLayout parentMsg = (LinearLayout) view.findViewById(R.id.msg_parent);

        textViewMsg.setText(msg.getContent());
        if(msg.isOwner()){
            parentMsg.setGravity(Gravity.RIGHT);
            textViewMsg.setBackgroundResource(R.drawable.message_send);
        }else{
            parentMsg.setGravity(Gravity.LEFT);
            textViewMsg.setBackgroundResource(R.drawable.message_response);
        }


        return view;
    }

    public void addItem(Message msg){
        this.msgs.add(msg);
        this.notifyDataSetChanged();
    }
}
