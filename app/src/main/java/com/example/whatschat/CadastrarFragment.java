package com.example.whatschat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class CadastrarFragment extends Fragment {

    private Uri imageUri;
    private final static int CAMERA_PIC_REQUEST = 1;
    private CircleImageView profileImg;
    private FirebaseAuth auth;

    public CadastrarFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.cadastrar_layout_fragment, container, false);

        //OnReplaceFragment implReplaceFrag = (OnReplaceFragment) container.getContext();

        profileImg = (CircleImageView) root.findViewById(R.id.profile_image);
        AppCompatButton buttonEntrar = root.findViewById(R.id.button_entrar);

        TextInputEditText inputName = root.findViewById(R.id.input_name);
        TextInputEditText inputEmail = root.findViewById(R.id.input_email);
        TextInputEditText inputPass = root.findViewById(R.id.input_pass);
        TextInputEditText inputReenterPass = root.findViewById(R.id.input_reenter_pass);

        TextView textMsgError = root.findViewById(R.id.error_msg);
        

        profileImg.setOnClickListener(event -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        });

        buttonEntrar.setOnClickListener(event -> {
            boolean isValid = validateForm(inputName.getText().toString(),
                    inputEmail.getText().toString(),
                    inputPass.getText().toString(),
                    inputReenterPass.getText().toString(),
                    textMsgError);

            if(isValid == false) return;

            auth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.root, new HomeFragment());
                                transaction.commit();
                            }else{
                                textMsgError.setText("Verifique sua conexão com a internet");
                            }
                        }
                    });
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

    public boolean validateForm(String nick, String email, String pass, String reenter, TextView msgError){
        if(nick.isEmpty()){
            msgError.setText(getResources().getString(R.string.nick_enter_empty));
            return false;
        }

        if(email.isEmpty()){
            msgError.setText(getResources().getString(R.string.email_enter_empty));
            return false;
        }

        if(pass.isEmpty()){
            msgError.setText(getResources().getString(R.string.pass_enter_empty));
            return false;
        }

        if(!pass.equals(reenter)){
            msgError.setText(getResources().getString(R.string.pass_and_reenter_incorrect));
            return false;
        }

        return true;
    }
}
