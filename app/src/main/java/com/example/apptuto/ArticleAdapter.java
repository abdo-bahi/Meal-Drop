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

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
   private Context context;
   private ArrayList<Article> articleListe;
   private RecyclerViewOnItemClick recyclerViewOnItemClick;

    public ArticleAdapter(Context context, ArrayList<Article> articleListe, RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.context = context;
        this.articleListe = articleListe;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.article, parent, false);
        return new MyViewHolder(v).linkAdapter(this );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameArticle.setText(articleListe.get(position).getNameArticle());
        holder.prixArticle.setText(articleListe.get(position).getPrixArticle()+" da");



    }

    @Override
    public int getItemCount() {
        return articleListe.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameArticle, prixArticle;
        Button articleToCart;
        private ArticleAdapter articleAdapter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameArticle = itemView.findViewById(R.id.textArticleAdminName);
            prixArticle = itemView.findViewById(R.id.textArticelAdminPrix);
            articleToCart = itemView.findViewById(R.id.deletArticleAdmin);
            articleToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //set dialog
                    recyclerViewOnItemClick.addCartWork(getAdapterPosition());
                }
            });
        }

        public MyViewHolder linkAdapter(ArticleAdapter articleAdapter){
            this.articleAdapter = articleAdapter;
            return this;
        }
    }
}
