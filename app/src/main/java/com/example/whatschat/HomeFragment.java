package com.example.whatschat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.ui.main.MessagesFragment;
import com.example.whatschat.ui.main.PageViewModel;
import com.example.whatschat.ui.main.PlaceholderFragment;
import com.example.whatschat.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

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
        final MessagesFragment msgFragment = new MessagesFragment();
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

                msgFragment.addHomeMessage(new HomeMessage("", "Anderson", "iai tudo bem?", new Date()));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
            }
        });

        return root;
    }
}
