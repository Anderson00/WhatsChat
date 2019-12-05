package com.example.whatschat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.model.Usuario;
import com.example.whatschat.ui.main.MessagesFragment;
import com.example.whatschat.ui.main.PageViewModel;
import com.example.whatschat.ui.main.PlaceholderFragment;
import com.example.whatschat.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

    private MessagesFragment msgFragment;

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_layout_fragment, container, false);

        AppCompatActivity ctx = (AppCompatActivity) getContext();

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ctx.setSupportActionBar(toolbar);
        ctx.getSupportActionBar().setDisplayUseLogoEnabled(true);


        List<Fragment> fragments = new ArrayList<Fragment>();
        msgFragment = new MessagesFragment();
        fragments.add(new MapFragment());
        fragments.add(msgFragment);
        fragments.add(new PlaceholderFragment());


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(ctx, ctx.getSupportFragmentManager(), fragments);
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContatosActivity.ACTION);
                startActivity(intent);
                //msgFragment.addHomeMessage(new HomeMessage("", "Anderson", "iai tudo bem?", new Date()));
                //createIputDialog();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
            }
        });

        return root;
    }

    public void createIputDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog_contatos, null);
        AlertDialog.Builder inputDialogBuilder = new AlertDialog.Builder(getContext());
        inputDialogBuilder.setView(mView);

        final EditText editText = (EditText) mView.findViewById(R.id.editText);
        inputDialogBuilder
                .setTitle("Adicionar contato")
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CollectionReference ref = FirebaseFirestore.getInstance().collection("users");
                        Query query = ref.whereEqualTo("email", editText.getText().toString());

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot doc : task.getResult()){
                                    String uuid = doc.getData().get("uuid").toString();
                                    if(!uuid.equals(FirebaseAuth.getInstance().getUid())) {
                                        ref.document(FirebaseAuth.getInstance().getUid())
                                                .update("contatos", FieldValue.arrayUnion(uuid))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                    }
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog inputDialog = inputDialogBuilder.create();
        inputDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getContext(), "entrou aq", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.add_contato:
                createIputDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
