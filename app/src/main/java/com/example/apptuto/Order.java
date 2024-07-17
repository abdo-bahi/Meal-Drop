package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    private TextView deconnection;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private RecyclerView recyclerView;
    private ArrayList<ViewOrder> orderArraylist = new ArrayList<>();
    private ViewOrderAdapter viewOrderAdapter;
    private BottomNavigationView bottomNavigationView_admin;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        deconnection = findViewById(R.id.deconnection1Txt);
        recyclerView = findViewById(R.id.recyclerOrder);


        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        viewOrderAdapter = new ViewOrderAdapter(this,orderArraylist);
        recyclerView.setAdapter(viewOrderAdapter);


        bottomNavigationView_admin = findViewById(R.id.bottom_nav_admin);
        bottomNavigationView_admin.setSelectedItemId(R.id.cmdActivity);

        bottomNavigationView_admin.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.cmdActivity: return true;
                    case R.id.rechargeActivity:
                        startActivity(new Intent(getApplicationContext(),Recharge.class));
                        overridePendingTransition(0,0);break;
                    case R.id.addMenuActivity:
                        startActivity(new Intent(getApplicationContext(),AddMenu.class));
                        overridePendingTransition(0,0);break;
                }
                return false;
            }
        });
        EventChangelistner();




        deconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginUI.class));
                finish();
            }
        });

    }


    private void EventChangelistner() {

        userID = fAuth.getCurrentUser().getUid();
        CollectionReference userscollectionReference = fStore.collection("users");
        Query rechargeQuery = userscollectionReference
                .whereEqualTo("usersId", userID );
        rechargeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()){
                        String buvname = document.getString("fname");
                        CollectionReference ordercollectionref = fStore.collection("orders");
                        Query orderQuery  = ordercollectionref
                                .whereEqualTo("fname", buvname);
                        orderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        ViewOrder viewOrder = document.toObject(ViewOrder.class);
                                        orderArraylist.add(viewOrder);
                                    }
                                    viewOrderAdapter.notifyDataSetChanged();

                                }else {
                                    Toast.makeText(Order.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


                    }


                }else {
                    Toast.makeText(Order.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}