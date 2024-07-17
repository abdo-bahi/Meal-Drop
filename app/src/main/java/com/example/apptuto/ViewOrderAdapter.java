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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<ViewOrder> orderList;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    public ViewOrderAdapter(Context context, ArrayList<ViewOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.order, parent , false);
        return new MyViewHolder(v).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.ordername.setText(orderList.get(position).getOrderName());
        holder.orderlist.setText(orderList.get(position).getOrderList());
        holder.orderinstruction.setText(orderList.get(position).getOrderInstruction());
        holder.orderadress.setText(orderList.get(position).getOrderAdress());
        holder.ordertotalprice.setText(orderList.get(position).getOrderTotalprice()+ " Da");

        holder.orderdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.orderdone.getContext());
                alertDialog.setTitle("Valider la commande ");
                alertDialog.setMessage("Voulez-vous valider et Supprimer la commande de " +holder.viewOrderAdapter.orderList.get(holder.getAdapterPosition()).getOrderName()+" ?");
                alertDialog.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone = holder.ordername.getText().toString().trim();
                        CollectionReference orderscollectionref = FirebaseFirestore.getInstance().collection("orders");
                        Query ordersquery = orderscollectionref.whereEqualTo("orderName", phone);
                        ordersquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        String id = document.getString("documentid");
                                        FirebaseFirestore.getInstance().collection("orders").document(id).delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            orderList.remove(orderList.get(position));
                                                            notifyDataSetChanged();
                                                            Toast.makeText(context, "envoyée", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(context, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }

                                }else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
        return orderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ordername, orderlist, orderinstruction, orderadress, ordertotalprice;
        Button orderdone;
        private ViewOrderAdapter viewOrderAdapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ordername = itemView.findViewById(R.id.textOrdername);
            orderlist = itemView.findViewById(R.id.textOrderlist);
            orderinstruction = itemView.findViewById(R.id.textorderinstruction);
            orderadress = itemView.findViewById(R.id.textOrderadress);
            ordertotalprice = itemView.findViewById(R.id.textTotalprice);
            orderdone = itemView.findViewById(R.id.orderdonebtn);

        }

        public MyViewHolder linkAdapter(ViewOrderAdapter viewOrderAdapter) {
            this.viewOrderAdapter = viewOrderAdapter;
            return  this;
        }
    }
}
