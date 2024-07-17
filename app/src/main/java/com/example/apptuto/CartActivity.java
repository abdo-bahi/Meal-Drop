package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartRecyclerViewOnClick {
    Application application = (Application) this.getApplication();

    public static final String TAG = "TAG";
    private BottomNavigationView bottomNavigationView_user;
    private RecyclerView cartRecyclerView;
    private ArrayList<CartItem> cartAppArrayList;
    private CartAdapter cartAdapter;
    private RadioGroup radioGroupCart;
    private RadioButton radioButtoncmdlocal;
    private RadioButton radioButtoncmddelevery;
    private ConstraintLayout constraintLayoutlocation;
    private Spinner sPavillon,sSalle;
    private String listItems, orderIns, addresse,totalPrix,buvName , cmdTotal;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    private EditText edtInst;
    private TextView totalPrixText, textChekout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        application.getTotalPrix();
        cartAppArrayList = application.getCartArrayListe();

        totalPrixText = findViewById(R.id.text_cart_total);
        totalPrixText.setText(String.valueOf(application.getTotalPrix()) + " da");

        edtInst = findViewById(R.id.cartInstEdt);
        textChekout = findViewById(R.id.textCheckout);
        sPavillon = findViewById(R.id.pavillon_spiner);
        sSalle = findViewById(R.id.sallespiner);
        fStore = FirebaseFirestore.getInstance();
        fAuth =FirebaseAuth.getInstance();

        textChekout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( ! cartAppArrayList.isEmpty()){
                    int ischek  = application.getIsChekedBuv();
                    switch (ischek){
                        case 0 : buvName = "cloud"; break;
                        case 1 : buvName = "stike"; break;
                        case 2 : buvName = "etu_club"; break;
                    }
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
                    alertDialog.setTitle("Envoyer la commande ?");
                    alertDialog.setMessage("voulez-vous envoyer cette commannde au "+ buvName);
                    alertDialog.setNegativeButton("Annuler", null);
                    alertDialog.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listItems = "";
                            for(int j=0 ; j< cartAppArrayList.size(); j++){
                                listItems = listItems +  String.valueOf(cartAppArrayList.get(j).getCartQtn()) + " * " + cartAppArrayList.get(j).getCartname() + "\n";

                            }
                            totalPrix = String.valueOf(application.getTotalPrix());
                            orderIns = edtInst.getText().toString();



                            if(radioButtoncmdlocal.isChecked()) {
                                addresse = "Commande locale ";
                            }
                            else {
                                addresse = "Pavillon "+sPavillon.getSelectedItem().toString() + " Salle: " + sSalle.getSelectedItem().toString();
                            }
                            CollectionReference usersCollectionRef = fStore.collection("users");
                            String userID = fAuth.getCurrentUser().getUid();
                            Query cartQuery = usersCollectionRef.whereEqualTo("usersId", userID);
                            cartQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            String oldcredit = document.getString("credit");
                                            String phonenumber = document.getString("phone");
                                            if (Integer.parseInt(oldcredit) > Integer.parseInt(totalPrix)){
                                                Integer i = Integer.parseInt(oldcredit) - Integer.parseInt(totalPrix);
                                                String newCredit = String.valueOf(i);
                                                DocumentReference rechargeRef =fStore.collection("users").document(userID);
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("credit", newCredit);
                                                rechargeRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG,"OnSuccess : doc updated" );
                                                        Toast.makeText(CartActivity.this, "Credit a jour", Toast.LENGTH_LONG).show();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG,"OnFailure", e);

                                                    }
                                                });

                                                DocumentReference documentReference = fStore.collection("orders").document(userID);
                                                Map<String, Object> orders = new HashMap<>();
                                                orders.put("orderName", phonenumber);
                                                orders.put("orderList", listItems);
                                                orders.put("orderInstruction", orderIns);
                                                orders.put("orderAdress", addresse);
                                                orders.put("orderTotalprice", totalPrix);
                                                orders.put("fname", buvName);
                                                orders.put("documentid", userID);
                                                documentReference.set(orders).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(CartActivity.this, "Commande envoyer", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CartActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }else {
                                                Toast.makeText(CartActivity.this, "credit insuffisant", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    } else {
                                        Toast.makeText(CartActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    });
                    alertDialog.show();

                }
            }
        });

        bottomNavigationView_user = findViewById(R.id.butom_nav_user);
        bottomNavigationView_user.setSelectedItemId(R.id.cartActivity);

        bottomNavigationView_user.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.cartActivity: return true;
                    case R.id.homeActivity:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

        radioButtoncmddelevery = findViewById(R.id.cmd_dlerver);
        radioButtoncmdlocal = findViewById(R.id.cmd_local);



        cartRecyclerView = findViewById(R.id.recyclerViewCart);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setNestedScrollingEnabled(false);

        cartAdapter= new CartAdapter(this, cartAppArrayList, this);

        cartRecyclerView.setAdapter(cartAdapter);



    }



    @Override
    public void deletCartItem(int position) {
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
        alertDialog.setTitle("Supprimer Article");
        alertDialog.setMessage("Voulez-vous supprimer "+cartAppArrayList.get(position).getCartname()+" du cart d'achat ?");
        alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!cartAppArrayList.isEmpty()){
                    application.setTotalPrix(application.getTotalPrix() -( Integer.parseInt(cartAppArrayList.get(position).getCartPrix()) * cartAppArrayList.get(position).getCartQtn()));
                    cartAppArrayList.remove(position);
                    cartAdapter.notifyItemRemoved(position);
                    totalPrixText.setText(String.valueOf(application.getTotalPrix())+" da");
                    Toast.makeText(CartActivity.this, "item ete supprimer", Toast.LENGTH_SHORT).show();

                }
            }
        });
        alertDialog.setNegativeButton("Annuler",null);
        alertDialog.show();
    }

    @Override
    public int addItemQnt(int position) {
        application.setTotalPrix(application.getTotalPrix() + Integer.parseInt(cartAppArrayList.get(position).getCartPrix()));
        cartAppArrayList.get(position).setCartQtn(cartAppArrayList.get(position).getCartQtn() + 1);
        totalPrixText.setText(String.valueOf(application.getTotalPrix())+" da");
        return cartAppArrayList.get(position).getCartQtn();
    }

    @Override
    public int remItemQnt(int position) {
        if(!(cartAppArrayList.get(position).getCartQtn() <= 1)){
            application.setTotalPrix(application.getTotalPrix() - Integer.parseInt(cartAppArrayList.get(position).getCartPrix()));
            cartAppArrayList.get(position).setCartQtn(cartAppArrayList.get(position).getCartQtn() - 1);
            totalPrixText.setText(String.valueOf(application.getTotalPrix())+" da");
        }
        return cartAppArrayList.get(position).getCartQtn();
    }
}