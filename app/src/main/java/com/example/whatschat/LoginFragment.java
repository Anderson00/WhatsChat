package com.example.whatschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.ui.main.MessagesFragment;
import com.example.whatschat.ui.main.PlaceholderFragment;
import com.example.whatschat.ui.main.SectionsPagerAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginFragment extends Fragment {

    private Uri imageUri;
    private final static int CAMERA_PIC_REQUEST = 1;
    private CircleImageView profileImg;

    public LoginFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_layout_fragment, container, false);

        OnReplaceFragment implReplaceFrag = (OnReplaceFragment) getContext();

        profileImg = (CircleImageView) root.findViewById(R.id.profile_image);
        AppCompatButton buttonEntrar = root.findViewById(R.id.button_entrar);

        profileImg.setOnClickListener(event -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        });

        buttonEntrar.setOnClickListener(event -> {
            implReplaceFrag.replaceFragment(new HomeFragment());
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_PIC_REQUEST){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            profileImg.setImageBitmap(image);
        }
    }

    public interface OnReplaceFragment{
        void replaceFragment(Fragment fragment);
    }
}