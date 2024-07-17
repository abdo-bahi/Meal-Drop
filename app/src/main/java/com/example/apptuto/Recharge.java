package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Recharge extends AppCompatActivity {
    BottomNavigationView bottomNavigationView_admin;

    public static final String TAG = "TAG";
    EditText creditRecharge , phonenumber;
    Button  gohome;
    FirebaseFirestore fStore;
    TextView ajoutCreditBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        bottomNavigationView_admin = findViewById(R.id.bottom_nav_admin);
        bottomNavigationView_admin.setSelectedItemId(R.id.rechargeActivity);

        bottomNavigationView_admin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.rechargeActivity: return true;
                    case R.id.cmdActivity:
                        startActivity(new Intent(getApplicationContext(),Order.class));
                        overridePendingTransition(0,0);break;
                    case R.id.addMenuActivity:
                        startActivity(new Intent(getApplicationContext(),AddMenu.class));
                        overridePendingTransition(0,0);break;
                }
                return false;
            }
        });

        creditRecharge = findViewById(R.id.creditRechargeTxt);
        phonenumber = findViewById(R.id.phonenumberTxT);
        ajoutCreditBtn = findViewById(R.id.ajoutCredit);

        fStore = FirebaseFirestore.getInstance();



        ajoutCreditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(creditRecharge.getText().toString().isEmpty()){
                    creditRecharge.setError("Ajouter credit");
                    return;
                }
                if(phonenumber.getText().toString().isEmpty()){
                    phonenumber.setError("Ajouter un n telephone");
                    return;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Recharge.this);
                alertDialog.setTitle("Ajouter au credit ?");
                alertDialog.setMessage("voulez-vous ajouter "+ creditRecharge.getText().toString() + " au "+ phonenumber.getText().toString());
                alertDialog.setNegativeButton("Annuler", null);
                alertDialog.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone, addcredit;
                        addcredit = creditRecharge.getText().toString().trim();
                        phone = phonenumber.getText().toString().trim();

                        CollectionReference usersCollectionRef = fStore.collection("users");
                        Query rechargeQuery = usersCollectionRef
                                .whereEqualTo("phone", phone );
                        rechargeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        String id = document.getString("usersId");
                                        String oldCredit = document.getString("credit");

                                        Integer i = Integer.parseInt(oldCredit) + Integer.parseInt(addcredit);
                                        String newCredit = String.valueOf(i);
                                        DocumentReference rechargeRef =fStore.collection("users").document(id);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("credit", newCredit);
                                        rechargeRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG,"OnSuccess : doc updated" );
                                                Toast.makeText(Recharge.this, "Credit ajouter", Toast.LENGTH_LONG).show();
                                                creditRecharge.setText("");
                                                phonenumber.setText("");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG,"OnFailure", e);
                                                Toast.makeText(Recharge.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }


                                }else {
                                    Toast.makeText(Recharge.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });




    }
}