package com.example.apptuto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnItemClick{


Application application = (Application) this.getApplication();
ArrayList<CartItem> mainCartList;

    private TextView connection, userHI, credit;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, categorieM = "Burger" , buvetteN = "cloud" ,userName;
    Button addmenubtn;
    private TextView textPizza,textBurger, textDrink, textSandwich, textPlat, textDessert,
            textCloud, textStike, textetuClub;

    private RecyclerView recyclerView ;
    protected int isChekedFood = 1, isChekedBuv;
    private  DocumentSnapshot menuQueriedDocument;
    private ArrayList<Article> articleArrayList= new ArrayList<>();
    private ArticleAdapter articleAdapter;
    private BottomNavigationView bottomNavigationView_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         mainCartList = application.getCartArrayListe();

        credit = findViewById(R.id.creditTxt);
        connection = findViewById(R.id.connectionTxt);
        userHI = findViewById(R.id.userHitxt);
        textPizza = findViewById(R.id.pizzaSwitch);
        textBurger = findViewById(R.id.burgerSwitch);
        textDrink = findViewById(R.id.drinkSwitch);
        textSandwich = findViewById(R.id.SandwichSwitch);
        textPlat = findViewById(R.id.platSwitch);
        textDessert = findViewById(R.id.dessertSwitch);
        textCloud = findViewById(R.id.cloudeName);
        textStike = findViewById(R.id.stikeName);
        textetuClub = findViewById(R.id.etuclubName);
        recyclerView = findViewById(R.id.recyclerArticle);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        textPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 0 : break;
                    case 1 : textBurger.setBackgroundResource(R.drawable.box2);
                    textBurger.setTextColor(getResources().getColor(R.color.black));


                        break;
                    case 2 :textDrink.setBackgroundResource(R.drawable.box2);
                        textDrink.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 3 :textSandwich.setBackgroundResource(R.drawable.box2);
                        textSandwich.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 4 : textPlat.setBackgroundResource(R.drawable.box2);
                        textPlat.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 5 : textDessert.setBackgroundResource(R.drawable.box2);
                        textDessert.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textPizza.setBackgroundResource(R.drawable.box1);
                textPizza.setTextColor(getResources().getColor(R.color.pink));
                isChekedFood = 0;
                categorieM = "Pizza";
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 0 : textPizza.setBackgroundResource(R.drawable.box2);
                        textPizza.setTextColor(getResources().getColor(R.color.black));break;
                    case 1 :
                        break;
                    case 2 :textDrink.setBackgroundResource(R.drawable.box2);
                        textDrink.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 3 :textSandwich.setBackgroundResource(R.drawable.box2);
                        textSandwich.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 4 : textPlat.setBackgroundResource(R.drawable.box2);
                        textPlat.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 5 : textDessert.setBackgroundResource(R.drawable.box2);
                        textDessert.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textBurger.setBackgroundResource(R.drawable.box1);
                textBurger.setTextColor(getResources().getColor(R.color.pink));
                categorieM = "Burger";
                isChekedFood = 1;
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 2 :
                        break;
                    case 0 :textPizza.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black)); break;
                    case 1 : textBurger.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black));
                        break;

                    case 3 :textSandwich.setBackgroundResource(R.drawable.box2);
                        textSandwich.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 4 : textPlat.setBackgroundResource(R.drawable.box2);
                        textPlat.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 5 : textDessert.setBackgroundResource(R.drawable.box2);
                        textDessert.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textDrink.setBackgroundResource(R.drawable.box1);
                textDrink.setTextColor(getResources().getColor(R.color.pink));
                categorieM= "drink";
                isChekedFood = 2;
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textSandwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 3 :
                        break;
                    case 0 :textPizza.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black)); break;
                    case 1 : textBurger.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black));
                        break;

                    case 2 :textDrink.setBackgroundResource(R.drawable.box2);
                        textDrink.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 4 : textPlat.setBackgroundResource(R.drawable.box2);
                        textPlat.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 5 : textDessert.setBackgroundResource(R.drawable.box2);
                        textDessert.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textSandwich.setBackgroundResource(R.drawable.box1);
                textSandwich.setTextColor(getResources().getColor(R.color.pink));
                categorieM = "Sandwitch";
                isChekedFood = 3;
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textPlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 4 :
                        break;
                    case 0 :textPizza.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black)); break;
                    case 1 : textBurger.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black));
                        break;

                    case 3 :textSandwich.setBackgroundResource(R.drawable.box2);
                        textSandwich.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 2 : textDrink.setBackgroundResource(R.drawable.box2);
                        textDrink.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 5 : textDessert.setBackgroundResource(R.drawable.box2);
                        textDessert.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textPlat.setBackgroundResource(R.drawable.box1);
                textPlat.setTextColor(getResources().getColor(R.color.pink));
                categorieM = "Plat";
                isChekedFood = 4;
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (isChekedFood){
                    case 5 :
                        break;
                    case 0 :textPizza.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black)); break;
                    case 1 : textBurger.setBackgroundResource(R.drawable.box2);
                        textBurger.setTextColor(getResources().getColor(R.color.black));
                        break;

                    case 3 :textSandwich.setBackgroundResource(R.drawable.box2);
                        textSandwich.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 4 : textPlat.setBackgroundResource(R.drawable.box2);
                        textPlat.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 2 : textDrink.setBackgroundResource(R.drawable.box2);
                        textDrink.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
                textDessert.setBackgroundResource(R.drawable.box1);
                textDessert.setTextColor(getResources().getColor(R.color.pink));
                categorieM = "Dessert";
                isChekedFood = 5;
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        textCloud.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                switch (isChekedBuv){
                    case 1 : textStike.setBackgroundResource(R.drawable.buvname);
                    case 2 : textetuClub.setBackgroundResource(R.drawable.buvname);
                }
                textCloud.setBackgroundResource(R.drawable.buvname2);
                isChekedBuv = 0;
                buvetteN = "cloud";
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

        });

        textStike.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                switch (isChekedBuv){
                    case 0 : textCloud.setBackgroundResource(R.drawable.buvname); break;
                    case 2 : textetuClub.setBackgroundResource(R.drawable.buvname); break;
                }
                textStike.setBackgroundResource(R.drawable.buvname2);
                isChekedBuv = 1;
                buvetteN = "stike";
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

        });

        textetuClub.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                switch (isChekedBuv){
                    case 0 : textCloud.setBackgroundResource(R.drawable.buvname);break;
                    case 1 : textStike.setBackgroundResource(R.drawable.buvname);break;
                }
                textetuClub.setBackgroundResource(R.drawable.buvname2);
                isChekedBuv = 2;
                buvetteN = "etu_club";
                CollectionReference menuCollectionRef = fStore.collection(buvetteN);
                Query menuQuery = menuCollectionRef
                        .whereEqualTo("Categorie", categorieM );
                menuQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            articleArrayList.clear();
                            for (QueryDocumentSnapshot document: task.getResult() ){

                                Article article = document.toObject(Article.class);
                                articleArrayList.add(article);
                            }
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(MainActivity.this, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        articleAdapter = new ArticleAdapter(this, articleArrayList, this);
        recyclerView.setAdapter(articleAdapter);



        if (fAuth.getCurrentUser() != null && userName != "Visiteur") {
            connection.setText("Se déconnecter");

        } else {

    if(fAuth.getCurrentUser() == null) {
fAuth.signInWithEmailAndPassword("Visiteur@gmail.com","1234567").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(! task.isSuccessful()){
            Toast.makeText(MainActivity.this,"error : "+task.getException(),Toast.LENGTH_SHORT).show();
        }
    }
});}
            connection.setText("se connecter");
        }

        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();

                }

                startActivity(new Intent(getApplicationContext(), LoginUI.class));
                finish();
            }
        });

    bottomNavigationView_user = findViewById(R.id.butom_nav_user);
    bottomNavigationView_user.setSelectedItemId(R.id.homeActivity);


    bottomNavigationView_user.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.homeActivity: return true;
                case R.id.cartActivity:

                    startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    overridePendingTransition(0,0);
            }
            return false;
        }
    });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (fAuth.getCurrentUser() != null) {

            fStore = FirebaseFirestore.getInstance();
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference userRef = fStore.collection("users").document(userID);
            userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (documentSnapshot.exists()) {
                        userName = documentSnapshot.getString("fname");
                        String creditid = documentSnapshot.getString("credit");
                        //code ajouter 3
                        if (userName.equals("cloud") || userName.equals("stike") || userName.equals("etu_club")) {

                            switch (userName){
                                case "cloud" : application.setIsChekedBuv(0);break;
                                case "stike" : application.setIsChekedBuv(1);break;
                                case "etu_club" : application.setIsChekedBuv(2);break;
                            }

                            startActivity(new Intent(getApplicationContext(), AddMenu.class));
                        } else{ if(userName.equals("Visiteur")){
                            connection.setText("se connecter");
                        }
                        //code ajouter 3

                            credit.setText(creditid + "  DA");
                            userHI.setText(userName);
                        }

                    }

                }
            });
        }

    }


    @Override
    public void addCartWork(int position) {
        if (userHI.getText().toString().equals("Visiteur")) {
            Toast.makeText(MainActivity.this,"vous etes pas eregistre !", Toast.LENGTH_LONG).show();

        } else {
            AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
            alertDialog.setTitle("Ajouter au Cart");
            alertDialog.setMessage("Voulez-vous ajouter "+ articleArrayList.get(position).getNameArticle()+" au cart d'achat ?");
            alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(mainCartList.isEmpty()){

                        mainCartList.add(new CartItem(articleArrayList.get(position).getNameArticle(),
                                articleArrayList.get(position).getPrixArticle(),
                                1));

                        application.setTotalPrix(Integer.parseInt(articleArrayList.get(position).getPrixArticle()));
                        Toast.makeText(MainActivity.this, "cart created", Toast.LENGTH_SHORT).show();
                        application.setIsChekedBuv(isChekedBuv);
                    }
                    else if(application.getIsChekedBuv() == isChekedBuv){

                        if(existe(position)){
                            Toast.makeText(MainActivity.this, "item already in cart", Toast.LENGTH_SHORT).show();
                        }
                        else {mainCartList.add(new CartItem(articleArrayList.get(position).getNameArticle(),
                                articleArrayList.get(position).getPrixArticle(),
                                1));
                            application.setTotalPrix(application.getTotalPrix() + Integer.parseInt(articleArrayList.get(position).getPrixArticle()));
                            Toast.makeText(MainActivity.this, "item added cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        AlertDialog.Builder alertDialog1 =new AlertDialog.Builder(MainActivity.this);
                        alertDialog1.setTitle("Supprimer cart precedante ?");
                        alertDialog1.setMessage("Vous pouvais cosulter une commande d\'une seul buvette a la fois cette action va cree une nouvelle cart");
                        alertDialog1.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainCartList.clear();
                                mainCartList.add(new CartItem(articleArrayList.get(position).getNameArticle(),
                                        articleArrayList.get(position).getPrixArticle(),
                                        1));

                                application.setTotalPrix(Integer.parseInt(articleArrayList.get(position).getPrixArticle()));

                                application.setIsChekedBuv(isChekedBuv);
                                Toast.makeText(MainActivity.this, "new cart created", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog1.setNegativeButton("Annuler",null);

                        alertDialog1.show();
                    }

                }
            });
            alertDialog.setNegativeButton("Annuler",null);

            alertDialog.show();

        }
    }


    public boolean existe(int position){
        for(int i = 0; i<mainCartList.size(); i++ ){
            if(mainCartList.get(i).getCartname() == articleArrayList.get(position).getNameArticle()){
                return true;
            }

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
