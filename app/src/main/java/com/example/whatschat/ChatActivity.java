package com.example.whatschat;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatschat.model.HomeMessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {

    private HomeMessage profileTarget;

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

        TextView title = (TextView) findViewById(R.id.toolbar_title);
        TextView subtitle = (TextView) findViewById(R.id.toolbar_subtitle);

        title.setText(this.profileTarget.getNameProfile());
        subtitle.setText("online");

        Toast.makeText(this, this.profileTarget.getNameProfile(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
