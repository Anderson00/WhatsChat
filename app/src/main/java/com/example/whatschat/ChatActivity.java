package com.example.whatschat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whatschat.model.Application;
import com.example.whatschat.model.Contato;
import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.model.Message;
import com.example.whatschat.model.Usuario;
import com.example.whatschat.ui.main.adapter.MessagesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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
    private Usuario me;

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

        EditText msgField = (EditText) findViewById(R.id.msg_field);
        Button btSend = (Button) findViewById(R.id.send_button);
        btSend.setOnClickListener(event -> {
            if(msgField.getText().toString().isEmpty())
                return;

            Message msg = new Message();
            msg.setFromId(FirebaseAuth.getInstance().getUid());
            msg.setToId(profileTarget.getTargetUuid());
            msg.setContent(msgField.getText().toString());
            msg.setTimestamp(System.currentTimeMillis());

            sendMessage(msg);
            msgField.setText("");
        });


        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(Usuario.class);
                        fetchMessages();
                    }
                });
    }

    private void fetchMessages(){
        if (me != null){

            FirebaseFirestore.getInstance().collection("conversas")
                    .document(me.getUuid())
                    .collection(profileTarget.getTargetUuid())
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                            if(documentChanges != null){
                                for(DocumentChange doc : documentChanges){
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        Message msg = doc.getDocument().toObject(Message.class);
                                        adapter.addMessage(msg);
                                    }
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void sendMessage(Message msg){
        if(msg.getContent().isEmpty())
            return;
        //                 this.adapter.addMessage(msg);
        FirebaseFirestore.getInstance().collection("conversas")
                .document(msg.getFromId())
                .collection(msg.getToId())
                .add(msg)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Contato contato = new Contato();
                        contato.setUuid(msg.getToId());
                        contato.setProfileImgURL(profileTarget.getImgProfile());
                        contato.setLastMessage(msg.getContent());
                        contato.setTimestamp(msg.getTimestamp());
                        contato.setName(profileTarget.getNameProfile());

                        FirebaseFirestore.getInstance().collection("last-messages")
                                .document(msg.getFromId())
                                .collection("contatos")
                                .document(msg.getToId())
                                .set(contato);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        FirebaseFirestore.getInstance().collection("conversas")
                .document(msg.getToId())
                .collection(msg.getFromId())
                .add(msg)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Usuario usuario = ApplicationSingleton.getInstance().getUsuario();

                        Contato contato = new Contato();
                        contato.setUuid(msg.getToId());
                        contato.setProfileImgURL(usuario.getProfileIconURI());
                        contato.setLastMessage(msg.getContent());
                        contato.setTimestamp(msg.getTimestamp());
                        contato.setName(usuario.getUsername());

                        FirebaseFirestore.getInstance().collection("last-messages")
                                .document(msg.getToId())
                                .collection("contatos")
                                .document(msg.getFromId())
                                .set(contato);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
