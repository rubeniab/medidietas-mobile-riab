package com.example.myapplication4.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.R;
import com.example.myapplication4.RetrofitClient;
import com.example.myapplication4.ApiService;
import com.example.myapplication4.databinding.FragmentHomeBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Configurar botón de logout
        Button logoutButton = binding.btnLogout; // Asegúrate de que en tu XML tenga el id 'btnLogout'
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return root;
    }

    private void logout() {
        // Obtener el token de SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token != null) {
            // Realizar la solicitud de logout al servidor
            RetrofitClient.getInstance().create(ApiService.class)
                    .logout(token)
                    .enqueue(new Callback<ResponseBody>() {
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
                            Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si no hay token, simplemente redirige al login
            clearSessionData();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    // Método para limpiar los datos de sesión
    private void clearSessionData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Eliminar todas las preferencias
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
