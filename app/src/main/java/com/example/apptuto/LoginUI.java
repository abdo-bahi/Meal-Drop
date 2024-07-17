package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginUI extends AppCompatActivity {

    EditText mEmail,mPasseWord;

    TextView mSignIn,mLogIn,txtVisiteur;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, bool , trues = "true";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);

        txtVisiteur = findViewById(R.id.txtvisiteur);
        mEmail = findViewById(R.id.Email);
        mPasseWord = findViewById(R.id.PasseWord);
        mLogIn = findViewById(R.id.LogIn);
        mSignIn = findViewById(R.id.SignIn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        txtVisiteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUI.this,MainActivity.class));
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUI.this, Register.class);
                startActivity(intent);

            }
        });

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String passeword = mPasseWord.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("e-mail est requis");
                    return;
                }
                if (TextUtils.isEmpty(passeword)){
                    mEmail.setError("Mot de passe est requis");
                    return;
                }
                if (passeword.length() < 6){
                    mPasseWord.setError("le mot de passe doit être >= 6 caractères");
                    return;
                }
                if(email.equals("Visiteur@gmail.com")){
                    mEmail.setError("se compte est reservè");
                    return;
                }
                // authenticate the user
                fAuth.signInWithEmailAndPassword(email,passeword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            userID = fAuth.getCurrentUser().getUid();

                            DocumentReference userRef = fStore.collection("users").document(userID);
                            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        bool = documentSnapshot.getString("boolean");
                                        if (bool.equals(trues)){
                                            Toast.makeText(LoginUI.this,"connecté avec succès",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginUI.this, AddMenu.class);
                                            startActivity(intent);

                                        }else {
                                            Toast.makeText(LoginUI.this,"connecté avec succès",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginUI.this, MainActivity.class);
                                            startActivity(intent);
                                        }

                                    } else {
                                        Toast.makeText(LoginUI.this,"Document does not exist",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginUI.this, "Error!", Toast.LENGTH_SHORT).show();

                                }
                            });





                        }else {
                            Toast.makeText(LoginUI.this,"erreur"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });



    }





    }


