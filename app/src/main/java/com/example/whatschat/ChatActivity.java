package com.example.whatschat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.model.Message;
import com.example.whatschat.ui.main.adapter.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

        this.adapter = new MessagesAdapter();
        RecyclerView recyclerView = findViewById(R.id.messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.adapter);

        Message m1 = new Message();
        m1.setContent("OI");
        m1.setFromId(FirebaseAuth.getInstance().getUid());

        Message m2 = new Message();
        m2.setContent("Tudo bem?");
        m2.setFromId("werwer");

        Message m3 = new Message();
        m3.setContent("Tudo bem?");
        m3.setFromId("werwer");

        this.adapter.addMessage(m1);
        this.adapter.addMessage(m2);
        this.adapter.addMessage(m3);

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

    }
}
