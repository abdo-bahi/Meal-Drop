package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText mFullname,mEmail,mPasseWord,mPhone;
   private TextView mRegisterbtn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID, credit ="0" , bool= "false";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullname = findViewById(R.id.Fullname);
        mEmail = findViewById(R.id.Email);
        mPasseWord = findViewById(R.id.PasseWord);
        mPhone = findViewById(R.id.Phone);
        mRegisterbtn = findViewById(R.id.Registerbtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String passeword = mPasseWord.getText().toString().trim();
                String fullname = mFullname.getText().toString();
                String phone = mPhone.getText().toString();
                //code ajouter 2
                if(fullname.equals("cloud") || fullname.equals("stike") || fullname.equals("etu_club") || fullname.equals("Visiteur")){
                    mFullname.setError("user name reservè ");
                    return;
                }
                //code ajouter 2
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("e-mail est requis");
                    return;
                }
                if (TextUtils.isEmpty(passeword)){
                    mPasseWord.setError("Mot de passe est requis");
                    return;
                }
                if (passeword.length() < 6){
                    mPasseWord.setError("le mot de passe doit être 6 caractères ou plus");
                    return;
                }
                if(email.equals("Visiteur@gmail.com")){
                    mEmail.setError("se compte est reservè");
                    return;
                }
                // register user firebase
                fAuth.createUserWithEmailAndPassword(email,passeword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this,"utilisateur créé",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname",fullname);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("boolean", bool);
                            user.put("credit", credit);
                            user.put("usersId",userID);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"le profil utilisateur est créé pour"+ userID );

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"OnFailure :"+ e.toString() );

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LoginUI.class));


                        }else {
                            Toast.makeText(Register.this,"Erreur"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });




            }
        });


    }
}