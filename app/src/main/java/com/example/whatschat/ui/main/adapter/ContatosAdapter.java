package com.example.whatschat.ui.main.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatschat.R;
import com.example.whatschat.model.HomeMessage;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.ContatosViewHolder> {

    public List<HomeMessage> contatos;

    public ContatosAdapter(){
        this.contatos = new ArrayList<>();
    }

    public void addContato(HomeMessage contato){
        this.contatos.add(contato);
    }

    @NonNull
    @Override
    public ContatosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contatos_recycler_list, parent, false);
        return new ContatosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatosViewHolder holder, int position) {
        HomeMessage contato = contatos.get(position);
        Picasso.get().load(contato.getImgProfile()).into(holder.profileImg);
        holder.profileName.setText(contato.getNameProfile());
        holder.profileEmail.setText(contato.getMsgProfile());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("chatActivity");
                intent.putExtra("USER_PROFILE", contato);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class ContatosViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImg;
        TextView profileName;
        TextView profileEmail;

        public ContatosViewHolder(View view){
            super(view);

            profileImg = view.findViewById(R.id.profile_image);
            profileName = view.findViewById(R.id.profile_name);
            profileEmail = view.findViewById(R.id.profile_email);

        }
    }
}
