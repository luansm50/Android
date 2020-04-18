package com.example.luanmelo.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String text) {
        String textoCodificado = Base64.encodeToString(text.getBytes(), Base64.DEFAULT)
                .replaceAll("(\\n|\\r)", "");
        return textoCodificado;
    }

    public static String descondificarBase64(String texto) {
        return new String(Base64.decode(texto, Base64.DEFAULT));
    }
}
