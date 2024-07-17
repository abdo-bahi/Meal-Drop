package com.example.apptuto;

import java.util.ArrayList;

public class Application extends android.app.Application {
    private static ArrayList<CartItem> cartArrayListe = new ArrayList<>();
    private static int totalPrix=0;
    private static int isChekedBuv = 0;

    public static int getTotalPrix() {
        return totalPrix;
    }

    public static void setTotalPrix(int totalPrix) {
        Application.totalPrix = totalPrix;
    }

    public Application() {
    }

    public static void setCartArrayListe(ArrayList<CartItem> cartArrayListe) {
        Application.cartArrayListe = cartArrayListe;
    }

    public static void setIsChekedBuv(int isChekedBuv) {
        Application.isChekedBuv = isChekedBuv;
    }

    public static ArrayList<CartItem> getCartArrayListe() {
        return cartArrayListe;
    }

    public static int getIsChekedBuv() {
        return isChekedBuv;
    }
}
