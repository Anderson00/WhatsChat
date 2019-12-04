package com.example.whatschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatschat.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class CadastrarFragment extends Fragment {

    private Uri imageUri;
    private final static int CAMERA_PIC_REQUEST = 1;
    private CircleImageView profileImg;
    private FirebaseAuth auth;
    private AppCompatDialog dialog;

    TextInputEditText inputName;
    TextInputEditText inputEmail;
    TextView textMsgError;

    public CadastrarFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        dialog = new AppCompatDialog(getContext());
        MaterialProgressBar progressBar = new MaterialProgressBar(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(progressBar);
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

        inputName = root.findViewById(R.id.input_name);
        inputEmail = root.findViewById(R.id.input_email);
        TextInputEditText inputPass = root.findViewById(R.id.input_pass);
        TextInputEditText inputReenterPass = root.findViewById(R.id.input_reenter_pass);

        textMsgError = root.findViewById(R.id.error_msg);
        

        profileImg.setOnClickListener(event -> {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK);
            cameraIntent.setType("image/*");
            startActivityForResult(cameraIntent, 0  );
        });

        buttonEntrar.setOnClickListener(event -> {
            boolean isValid = validateForm(inputName.getText().toString(),
                    inputEmail.getText().toString(),
                    inputPass.getText().toString(),
                    inputReenterPass.getText().toString(),
                    textMsgError);

            if(isValid == false) return;

            dialog.show();

            auth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            saveUserFirebase();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            textMsgError.setText("Verifique sua conexão com a internet");
                            textMsgError.setVisibility(View.VISIBLE);
                        }
            });
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                profileImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserFirebase(){
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Usuario usuario = new Usuario();
                        usuario.setUuid(FirebaseAuth.getInstance().getUid());
                        usuario.setUsername(inputName.getText().toString());
                        usuario.setEmail(inputEmail.getText().toString());
                        usuario.setProfileIconURI(uri.toString());
                        FirebaseFirestore.getInstance().collection("users")
                                .document(usuario.getUuid())
                                .set(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.root, new HomeFragment());
                                        transaction.commit();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        textMsgError.setText("Error ao adicionar usuario");
                                        textMsgError.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Testando", Toast.LENGTH_LONG);
            }
        });
    }

    public boolean validateForm(String nick, String email, String pass, String reenter, TextView msgError){
        if(nick.isEmpty()){
            msgError.setText(getResources().getString(R.string.nick_enter_empty));
            msgError.setVisibility(View.VISIBLE);
            return false;
        }

        if(email.isEmpty()){
            msgError.setText(getResources().getString(R.string.email_enter_empty));
            msgError.setVisibility(View.VISIBLE);
            return false;
        }

        if(pass.isEmpty()){
            msgError.setText(getResources().getString(R.string.pass_enter_empty));
            msgError.setVisibility(View.VISIBLE);
            return false;
        }

        if(!pass.equals(reenter)){
            msgError.setText(getResources().getString(R.string.pass_and_reenter_incorrect));
            msgError.setVisibility(View.VISIBLE);
            return false;
        }

        if(imageUri == null){
            msgError.setText("Adicione uma imagem de perfil");
            msgError.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }
}
