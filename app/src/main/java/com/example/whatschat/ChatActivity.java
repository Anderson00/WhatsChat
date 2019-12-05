package com.example.whatschat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.model.Message;
import com.example.whatschat.ui.main.adapter.MessagesAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private HomeMessage profileTarget;
    private ListView msgList;
    private MessagesAdapter adapter;
    private List<Message> msgs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        profileTarget = (HomeMessage) getIntent().getExtras().get("USER_PROFILE");

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CircleImageView profileImg = findViewById(R.id.chat_img);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView subtitle = (TextView) findViewById(R.id.toolbar_subtitle);


        Picasso.get().load(profileTarget.getImgProfile()).into(profileImg);

        title.setText(this.profileTarget.getNameProfile());
        subtitle.setText("online");

        Message m1 = new Message();
        m1.setContent("OI");
        m1.setOwner(true);

        Message m2 = new Message();
        m2.setContent("Olá");
        m2.setOwner(false);

        Message m3 = new Message();
        m3.setContent("Está tudo bem?");
        m3.setOwner(false);


        this.msgs = new ArrayList<>();
        this.msgs.add(m1);
        this.msgs.add(m2);
        this.msgs.add(m3);

        this.adapter = new MessagesAdapter(this, msgs);
        ListView msgList = (ListView) findViewById(R.id.chat_msgs);
        msgList.setAdapter(this.adapter);

        EditText msgField = (EditText) findViewById(R.id.msg_field);
        Button btSend = (Button) findViewById(R.id.send_button);
        btSend.setOnClickListener(event -> {
            if(msgField.getText().toString().isEmpty())
                return;
            sendMessage(msgField.getText().toString());
            msgField.setText("");
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void sendMessage(String content){
        Message msg = new Message();
        msg.setContent(content);
        msg.setOwner(true);
        msgs.add(msg);
        adapter.notifyDataSetChanged();
    }
}
