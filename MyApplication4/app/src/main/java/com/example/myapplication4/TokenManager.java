package com.example.myapplication4;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private static final String SHARED_PREFS_NAME = "TokenPrefs";
    private static final String TOKEN_KEY = "auth_token";

    // Guardar el token en SharedPreferences
    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply(); // Asegura que el cambio se guarde
    }

    // Obtener el token desde SharedPreferences
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        Log.d("TokenManager", "Token recuperado: " + token);
        return token;
    }

    // Eliminar el token (por ejemplo, al cerrar sesión)
    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token"); // O utiliza editor.clear() si quieres eliminar todo
        editor.apply();
        Log.d("TokenManager", "Token eliminado correctamente.");
    }
}
