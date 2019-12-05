package com.example.whatschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.ui.main.adapter.ContatosAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContatosActivity extends AppCompatActivity {

    public static final String ACTION = "contatosActivity";

    ContatosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contatos_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new ContatosAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String, Object> data = task.getResult().getData();
                        List<String> contatos = (List<String>) data.get("contatos");
                        for(String uuid : contatos){
                            FirebaseFirestore.getInstance().collection("users")
                                    .document(uuid)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Map<String, Object> data = task.getResult().getData();
                                            String username = data.get("username").toString();
                                            String email = data.get("email").toString();
                                            String profileIconURI = data.get("profileIconURI").toString();

                                            addContato(new HomeMessage(profileIconURI, username, email, null));
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addContato(HomeMessage contato){
        adapter.addContato(contato);
        adapter.notifyDataSetChanged();
    }
}
