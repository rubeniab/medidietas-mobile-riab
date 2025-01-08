package com.example.myapplication4.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication4.ApiService;
import com.example.myapplication4.R;
import com.example.myapplication4.RetrofitClient;
import com.example.myapplication4.databinding.FragmentGalleryBinding;
import com.example.myapplication4.modificarPerfil;
import com.example.myapplication4.ui.perfil.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConsultarFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Botón para modificar perfil
        Button btnModificarPerfil = root.findViewById(R.id.btnModificarPerfil);

        // Recuperar el token guardado en SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MiAppPreferences", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);  // Recupera el token guardado

        if (token == null) {
            Log.e("Token Error", "No se encontró el token. El usuario no está autenticado.");
            // Redirigir a LoginActivity o mostrar un mensaje de error si no hay token
        } else {
            // Configuración de Retrofit desde RetrofitClient
            Retrofit retrofit = RetrofitClient.getInstance();  // Usamos la instancia de Retrofit
            ApiService apiService = retrofit.create(ApiService.class);

            // Suponiendo que tienes el nombre de usuario, se puede obtener de algún lugar o pasarlo
            String nombreUsuario = "Raúl";  // Reemplaza con el nombre de usuario real

            // Realizar la llamada GET para obtener el usuario
            Call<Usuario> call = apiService.obtenerUsuarioPorNombre(nombreUsuario, "Bearer " + token);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        try {
                            // Procesar la respuesta exitosa
                            Usuario usuario = response.body();
                            if (usuario != null) {
                                Log.d("API Response", "Respuesta del usuario: " + usuario.getNombre_usuario());

                                // Mostrar los datos del usuario en la UI
                                TextView nombreTextView = root.findViewById(R.id.nombre);
                                TextView nombreUsuarioTextView = root.findViewById(R.id.nombre_usuario);
                                TextView correoTextView = root.findViewById(R.id.correo);
                                //TextView caloriasTextView = root.findViewById(R.id.calorias);
                                //TextView carbohidratosTextView = root.findViewById(R.id.carbohidratos);
                                //TextView grasasTextView = root.findViewById(R.id.grasas);
                                //TextView proteinasTextView = root.findViewById(R.id.proteinas);

                                // Asignar los valores a los TextViews
                                nombreTextView.setText(usuario.getNombre());
                                nombreUsuarioTextView.setText(usuario.getNombre_usuario());
                                correoTextView.setText(usuario.getCorreo());
                                //caloriasTextView.setText(usuario.getCalorias());
                                //carbohidratosTextView.setText(usuario.getCarbohidratos());
                                //grasasTextView.setText(usuario.getGrasas());
                                //proteinasTextView.setText(usuario.getProteinas());

                            } else {
                                Log.e("API ERROR", "El usuario no existe o la respuesta está vacía");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Manejar error si la respuesta no es exitosa
                        Log.e("API Error", "Error en la respuesta: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    // Manejar el fallo de la solicitud
                    Log.e("API Failure", "Error al hacer la llamada: " + t.getMessage());
                }
            });
        }

        // Acción cuando se hace clic en el botón de modificar perfil
        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), modificarPerfil.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
