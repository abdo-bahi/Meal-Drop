package com.example.apptuto;

public class CartItem {


   private String cartname,cartPrix;
   private int cartQtn;

    public CartItem(String cartname, String cartPrix, int cartQtn) {
        this.cartname = cartname;
        this.cartPrix = cartPrix;
        this.cartQtn = cartQtn;
    }
    public CartItem(){}

    public String getCartname() {
        return cartname;
    }

    public void setCartname(String cartname) {
        this.cartname = cartname;
    }

    public String getCartPrix() {
        return cartPrix;
    }

    public void setCartPrix(String cartPrix) {
        this.cartPrix = cartPrix;
    }

    public int getCartQtn() {
        return cartQtn;
    }

    public void setCartQtn(int cartQtn) {
        this.cartQtn = cartQtn;
    }
}
