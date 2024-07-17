package com.example.apptuto;


public class Article
{
    private String nameArticle, prixArticle, categorie;

    public Article(String nameArticle, String prixArticle) {
        this.prixArticle = prixArticle;
        this.nameArticle = nameArticle;

    }

    public Article (String nameArticle, String prixArticle, String categorie) {
        this.nameArticle = nameArticle;
        this.prixArticle = prixArticle;

        this.categorie = categorie;
    }
    public Article(){

    }

    public String getNameArticle() {
        return nameArticle;
    }

    public String getPrixArticle() {
        return prixArticle;
    }



    public String getCategorie() {
        return categorie;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public void setPrixArticle(String prixArticle) {
        this.prixArticle = prixArticle;
    }


}