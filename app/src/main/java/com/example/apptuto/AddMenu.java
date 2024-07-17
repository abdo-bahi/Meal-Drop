package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMenu extends AppCompatActivity  {
    public static final String TAG = null;
    Spinner spinnerM;
    private ArrayList<Marticle> adminMenuList;
    private AdminMenuAdapter adminMenuAdapter;
    EditText nomMenutxt, prixMenutxt;
    String categorieM, userID, nameB, buvname;
    TextView confirmerb,seDiconnect;
    BottomNavigationView bottomNavigationView_admin;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private RecyclerView adminMenuRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        bottomNavigationView_admin = findViewById(R.id.bottom_nav_admin);
        bottomNavigationView_admin.setSelectedItemId(R.id.addMenuActivity);

        bottomNavigationView_admin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.addMenuActivity: return true;
                    case R.id.cmdActivity:
                        startActivity(new Intent(getApplicationContext(),Order.class));
                        overridePendingTransition(0,0);break;
                    case R.id.rechargeActivity:
                        startActivity(new Intent(getApplicationContext(),Recharge.class));
                        overridePendingTransition(0,0);break;
                }
                return false;
            }
        });

        spinnerM = findViewById(R.id.categorieMenu);
        nomMenutxt = findViewById(R.id.nomMenu);
        prixMenutxt = findViewById(R.id.prixMenu);
        confirmerb = findViewById(R.id.confirmerBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        seDiconnect = findViewById(R.id.deconnection2Txt);
        seDiconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginUI.class));
                finish();
            }
        });

        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorieM = spinnerM.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        DocumentReference userRef = fStore.collection("users").document(userID);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    nameB = documentSnapshot.getString("fname");

                } else {
                    Toast.makeText(AddMenu.this,"Document does not exist",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddMenu.this, "Error!", Toast.LENGTH_SHORT).show();

            }
        });
        confirmerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //code Ajouter 1
                if(nomMenutxt.getText().toString().isEmpty()){
                    nomMenutxt.setError("Ajouter nom de menu");
                }else if(prixMenutxt.getText().toString().isEmpty()){
                    prixMenutxt.setError("Ajouter un prix");
                }
                // code Ajouter 1
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMenu.this);
                    alertDialog.setTitle("Ajouter aux menu ?");
                    alertDialog.setMessage("voulez-vous ajouter "+nomMenutxt.getText().toString() + " a votre menu ?");
                    alertDialog.setNegativeButton("Annuler", null);
                    alertDialog.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String nomMenu = nomMenutxt.getText().toString();
                            String prixMenu = prixMenutxt.getText().toString();
                            String choix = categorieM;
                            DocumentReference documentReference = fStore.collection(nameB).document(nomMenu);
                            Map<String, Object> user = new HashMap<>();
                            user.put("nameArticle",nomMenu);
                            user.put("prixArticle", prixMenu);
                            user.put("Categorie", choix);
                            //user.put("fname", nameB);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddMenu.this, "Menu ajouter", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddMenu.this, "Erreur", Toast.LENGTH_SHORT).show();


                                }
                            });
                            adminMenuAdapter.notifyDataSetChanged();
                        }
                    });

                    alertDialog.show();

                }

            }
        });

//code 3
        adminMenuList = new ArrayList<>();
        buvname = String.valueOf(Application.getIsChekedBuv());



        adminMenuRecycler = findViewById(R.id.adminMenuRecyler);
        adminMenuRecycler.setLayoutManager(new LinearLayoutManager(this));

        adminMenuAdapter = new AdminMenuAdapter(AddMenu.this, adminMenuList, buvname);
        adminMenuRecycler.setAdapter(adminMenuAdapter);

        eventChangelistner();




    }
    private void eventChangelistner() {


        CollectionReference userscollectionReference = fStore.collection("users");
        Query rechargeQuery = userscollectionReference
                .whereEqualTo("usersId", userID );
        rechargeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()){
                        String buvname = document.getString("fname");
                        CollectionReference ordercollectionref = fStore.collection(buvname);
                        Query orderQuery  = ordercollectionref;

                        orderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Marticle marticle = document.toObject(Marticle.class);
                                        adminMenuList.add(marticle);
                                    }
                                    adminMenuAdapter.notifyDataSetChanged();

                                }else {
                                    Toast.makeText(AddMenu.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


                    }


                }else {
                    Toast.makeText(AddMenu.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}