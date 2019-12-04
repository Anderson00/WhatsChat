package com.example.whatschat.ui.main;

import com.example.whatschat.MainActivity;
import com.example.whatschat.R;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatschat.model.HomeMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesHomeAdapter extends RecyclerView.Adapter<MessagesHomeAdapter.MessagesHomeAdapterViewHolder> {

    public List<HomeMessage> homeMessages;

    public MessagesHomeAdapter(List<HomeMessage> homeMessages) {
        this.homeMessages = homeMessages;
    }

    @NonNull
    @Override
    public MessagesHomeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);
        return new MessagesHomeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessagesHomeAdapterViewHolder holder, int position) {
        HomeMessage hm = homeMessages.get(position);
        holder.nameProfile.setText(hm.getNameProfile());
        holder.msgProfile.setText(hm.getMsgProfile());
        holder.dateMsgProfile.setText(hm.getDateMsgProfile());
        if(hm.getMsgQuant() > 0)
            holder.quantMessage.setText(hm.getMsgQuant()+"");
        else
            holder.quantMessage.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.CHAT_ACTIVITY);
            intent.putExtra("USER_PROFILE", hm);
            holder.itemView.getContext().startActivity(intent);
        });

        Picasso.get().load(hm.getImgProfile()).into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return (homeMessages != null)? homeMessages.size() : 0;
    }


    public static class MessagesHomeAdapterViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imgProfile;
        public TextView nameProfile;
        public TextView msgProfile;
        public TextView dateMsgProfile;
        public TextView quantMessage;

        public MessagesHomeAdapterViewHolder(View view){
            super(view);

            imgProfile = (CircleImageView)view.findViewById(R.id.profile_image);
            nameProfile = (TextView)view.findViewById(R.id.profile_name);
            msgProfile = (TextView)view.findViewById(R.id.profile_msg);
            dateMsgProfile = (TextView)view.findViewById(R.id.last_msg_time);
            quantMessage = (TextView) view.findViewById(R.id.msg_quant);
        }

    }
}
