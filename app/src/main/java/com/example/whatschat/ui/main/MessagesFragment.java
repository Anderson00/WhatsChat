package com.example.whatschat.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whatschat.R;
import com.example.whatschat.model.HomeMessage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessagesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private MessagesHomeAdapter adapter;
    private List<HomeMessage> messages = new ArrayList<HomeMessage>();

    public static MessagesFragment newInstance(int index) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        setupRecycler(root);

        return root;
    }

    private void setupRecycler(View view){
        if(adapter != null)
            return;
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.msg_list);
        recycler.setPadding(0,150,0,150);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.adapter = new MessagesHomeAdapter(this.messages);
        recycler.setAdapter(adapter);
    }

    public void addHomeMessage(HomeMessage hMessage){
        messages.add(hMessage);
        adapter.notifyDataSetChanged();
    }
}