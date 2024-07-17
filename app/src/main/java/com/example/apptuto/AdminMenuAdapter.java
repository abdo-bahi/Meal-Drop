package com.example.apptuto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.MyViewHolder> {
    Context context;
    private ArrayList<Marticle> adminMenuList;
    FirebaseFirestore adminFstore;
    FirebaseAuth adminFauth;
    String buvName;

    public AdminMenuAdapter(Context context, ArrayList<Marticle> adminMenuList, String buvName) {
        this.context = context;
        this.adminMenuList = adminMenuList;
        this.buvName = buvName;
        adminFstore = FirebaseFirestore.getInstance();
        adminFauth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new AdminMenuAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.adminNameArticle.setText(adminMenuList.get(position).getNameArticle());
        holder.adminPrixArticle.setText(adminMenuList.get(position).getPrixArticle() + " Da");


        holder.  deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Supprimer l\'article ? ");
                alertDialog.setMessage("Voulez-vous supprimer  "+adminMenuList.get(position).getNameArticle() +" de la menu ?");
                alertDialog.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CollectionReference userscollectionReference = adminFstore.collection("users");
                        Query rechargeQuery = userscollectionReference
                                .whereEqualTo("usersId", adminFauth.getCurrentUser().getUid() );
                        rechargeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){


                                    Toast.makeText(context, "Article supprimer", Toast.LENGTH_SHORT).show();
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        String buvname = document.getString("fname");
                                        CollectionReference ordercollectionref = adminFstore.collection(buvname);
                                        Query orderQuery  = ordercollectionref;

                                        orderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot document : task.getResult()){
                                                        DocumentReference dcRef = adminFstore.collection(buvname).document(adminMenuList.get(position).getNameArticle());
                                                        dcRef.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){



                                                                        }else {
                                                                            Toast.makeText(context, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context,"Error : " + e.getMessage(),Toast.LENGTH_SHORT).show();;
                                                            }
                                                        });
                                                    }
                                                    adminMenuList.remove(position);
                                                    notifyDataSetChanged();

                                                }else {
                                                    Toast.makeText(context, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });


                                    }


                                }else {
                                    Toast.makeText(context, "Query failed. Chek logs.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });




                    }
                });


                alertDialog.setNegativeButton("non", null);
                alertDialog.show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return (adminMenuList == null) ? 0 : adminMenuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView adminNameArticle, adminPrixArticle;
        Button deleteArticle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adminNameArticle = itemView.findViewById(R.id.menu_article_name);
            adminPrixArticle = itemView.findViewById(R.id.menu_article_prix);
            deleteArticle = itemView.findViewById(R.id.menu_delete);

        }
    }
}
