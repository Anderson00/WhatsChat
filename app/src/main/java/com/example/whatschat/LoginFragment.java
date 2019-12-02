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
import android.view.Window;
import android.widget.TextView;

import com.example.whatschat.model.Application;
import com.example.whatschat.model.HomeMessage;
import com.example.whatschat.ui.main.MessagesFragment;
import com.example.whatschat.ui.main.PlaceholderFragment;
import com.example.whatschat.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class LoginFragment extends Fragment {

    private Uri imageUri;
    private final static int CAMERA_PIC_REQUEST = 1;
    private CircleImageView profileImg;
    private FirebaseAuth auth;

    public LoginFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        //showProgressDialog();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(auth.getCurrentUser() != null){
            Application.getInstance().setCurrentUser(auth.getCurrentUser());

            OnReplaceFragment onReplaceFragment = (OnReplaceFragment) getContext();
            onReplaceFragment.replaceFragment(new HomeFragment());
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_layout_fragment, container, false);

        OnReplaceFragment implReplaceFrag = (OnReplaceFragment) getContext();

        profileImg = (CircleImageView) root.findViewById(R.id.profile_image);
        TextInputEditText inputEmail = root.findViewById(R.id.input_email);
        TextInputEditText inputPass = root.findViewById(R.id.input_pass);
        AppCompatButton buttonEntrar = root.findViewById(R.id.button_entrar);
        AppCompatButton buttonCadastrar = root.findViewById(R.id.button_cadastrar);
        TextView msgError = root.findViewById(R.id.error_msg);

        profileImg.setOnClickListener(event -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        });

        buttonEntrar.setOnClickListener(event -> {
            login(inputEmail.getText().toString(), inputPass.getText().toString(), msgError);
            //implReplaceFrag.replaceFragment(new HomeFragment());
        });

        buttonCadastrar.setOnClickListener(event -> {
            implReplaceFrag.replaceFragment(new CadastrarFragment());
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

    public void login(String email, String pass, TextView msgError){
        if(email.isEmpty()){
            msgError.setText(getResources().getString(R.string.email_enter_empty));
            msgError.setVisibility(View.VISIBLE);
            return;
        }

        if(pass.isEmpty()){
            msgError.setText(getResources().getString(R.string.pass_enter_empty));
            msgError.setVisibility(View.VISIBLE);
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            ApplicationSingleton.getInstance().setUser(auth.getCurrentUser());
                            OnReplaceFragment onReplaceFragment = (OnReplaceFragment) getContext();
                            onReplaceFragment.replaceFragment(new HomeFragment());
                        }else{
                            msgError.setText("Email ou Senha incorretos");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void showProgressDialog(){
        AppCompatDialog dialog = new AppCompatDialog(getContext());
        MaterialProgressBar progressBar = new MaterialProgressBar(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(progressBar);
        dialog.show();

    }

    public interface OnReplaceFragment{
        void replaceFragment(Fragment fragment);
    }
}
