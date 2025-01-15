package com.example.myapplication4.ui.Utilidades;

public class ConversorUrlYoutube {

    public static String convertirUrl(String url) {

        // Convierte URL larga a su forma embebida
        if (url.contains("youtube.com/watch?v=")) {
            url = url.replace("youtube.com/watch?v=", "youtube.com/embed/");
            url = url.split("&")[0];
        }

        // Convierte URL corta a su forma embebida
        if (url.contains("youtu.be/")) {
            url = url.replace("youtu.be/", "youtube.com/embed/");
        }

        return url;
    }
}
