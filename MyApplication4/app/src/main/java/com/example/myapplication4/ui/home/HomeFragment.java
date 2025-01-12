package com.example.myapplication4.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.ApiService;
import com.example.myapplication4.R;
import com.example.myapplication4.TokenManager;
import com.example.myapplication4.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Configurar botón de logout
        Button logoutButton = binding.btnLogout; // Asegúrate de que el ID coincide con el XML
        logoutButton.setOnClickListener(v -> performLogOut());

        PieChart pieChart1 = binding.pieChart1;
        int totalCalorias = 5600;
        int caloriasConsumidas = 2370;
        int caloriasRestantes = totalCalorias - caloriasConsumidas;
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(caloriasRestantes, "Calorias restantes"));
        entries.add(new PieEntry(caloriasConsumidas, "Calorias consumidas"));
        PieDataSet set = new PieDataSet(entries, "Calorias");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(set);
        pieChart1.setData(data);
        pieChart1.invalidate();
        return root;

    }

    public void performLogOut() {
        // Obtener el servicio de la API desde ApiService
        ApiService.Service apiService = ApiService.getService(); // Asegúrate de que esta instancia esté bien configurada.

        String token = TokenManager.getToken(getContext());
        Log.d(TAG, "Token recuperado para logout: " + token);

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "El token está vacío o es nulo");
            showToast("Error: No se encontró el token.");
            return;
        }

        // Llamada al método logOut
        Call<ResponseBody> call = apiService.logOut("Bearer " + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Leer el cuerpo de la respuesta
                        String cuerpoRespuesta = response.body().string();
                        JSONObject respuestaJson = new JSONObject(cuerpoRespuesta);
                        String mensaje = respuestaJson.optString("msg", "Operación exitosa");

                        Log.i(TAG, "Respuesta exitosa: " + cuerpoRespuesta);

                        // Limpiar datos de sesión y redirigir al LoginActivity
                        TokenManager.clearToken(getContext());

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        // Mostrar mensaje
                        showToast(mensaje);
                    } catch (Exception e) {
                        Log.e(TAG, "Error al procesar la respuesta: ", e);
                        showToast("Error al procesar la respuesta.");
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta: Código " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            Log.e(TAG, "Respuesta de error: " + errorResponse);
                        } catch (Exception e) {
                            Log.e(TAG, "Error al leer el cuerpo de la respuesta de error", e);
                        }
                    }
                    showToast("Error al cerrar sesión: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error en la conexión: ", t);
                showToast("Error de conexión: " + t.getMessage());
            }
        });

        /*
        if (token != null) {
            // Realizar la solicitud de logout al servidor
            ApiService.Service apiService = RetrofitClient.getInstance().create(ApiService.Service.class);
            apiService.logOut(token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Limpiar los datos de sesión locales
                        clearSessionData();

                        // Redirigir al LoginActivity
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
                        startActivity(intent);
                        getActivity().finish();

                        // Mostrar mensaje de sesión cerrada
                        Toast.makeText(getContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostrar error si la respuesta no fue exitosa
                        Toast.makeText(getContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Manejar errores de red
                    Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no hay token, simplemente redirige al login
            clearSessionData();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }*/




    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void clearSessionData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Eliminar todas las preferencias
        editor.apply();
        Log.i(TAG, "Datos de sesión eliminados correctamente.");
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
