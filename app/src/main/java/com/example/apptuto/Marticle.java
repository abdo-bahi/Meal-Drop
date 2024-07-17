package com.example.apptuto;

public class Marticle {
    private String nameArticle,prixArticle,Categorie;

    public Marticle() {
    }

    public Marticle(String nameArticle, String prixArticle, String Categorie) {
        this.nameArticle = nameArticle;
        this.prixArticle = prixArticle;
        this.Categorie = Categorie;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public String getPrixArticle() {
        return prixArticle;
    }

    public void setPrixArticle(String prixArticle) {
        this.prixArticle = prixArticle;
    }
}
