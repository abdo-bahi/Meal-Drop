package com.example.apptuto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    ArrayList<CartItem> cartListe;
    CartRecyclerViewOnClick cartRecyclerViewOnClick;

    public CartAdapter(Context context, ArrayList<CartItem> cartListe, CartRecyclerViewOnClick cartRecyclerViewOnClick) {
        this.context = context;
        this.cartListe = cartListe;
        this.cartRecyclerViewOnClick = cartRecyclerViewOnClick;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cartNameArticle.setText(cartListe.get(position).getCartname());
        holder.cartPrixArticle.setText(cartListe.get(position).getCartPrix() + " da");
        holder.cartQtnTxt.setText(String.valueOf(cartListe.get(position).getCartQtn()));


    }

    @Override
    public int getItemCount() {
        return cartListe.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartNameArticle, cartPrixArticle, cartQtnTxt,txttest;
        Button addCart, remCart, deletItem;
        private CartAdapter cartAdapter;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartNameArticle = itemView.findViewById(R.id.menu_article_name);
            cartPrixArticle = itemView.findViewById(R.id.cart_article_prix);
            cartQtnTxt = itemView.findViewById(R.id.cart_qtn_txt);

            addCart = itemView.findViewById(R.id.cart_qtn_add);
            remCart = itemView.findViewById(R.id.cart_qtn_rem);
            deletItem = itemView.findViewById(R.id.menu_delete);



            deletItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //set dialoge
                    cartRecyclerViewOnClick.deletCartItem(getAdapterPosition());

                }
            });

            remCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cartQtnTxt.setText(String.valueOf(cartRecyclerViewOnClick.remItemQnt(getAdapterPosition())));
                }
            });

            addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartQtnTxt.setText(String.valueOf(cartRecyclerViewOnClick.addItemQnt(getAdapterPosition())));

                }

            });


        }


    }
}







