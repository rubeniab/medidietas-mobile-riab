package com.example.myapplication4.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.example.myapplication4.ui.daos.ConsumoDAO;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.example.myapplication4.ui.modelos.ConsumoDiario;
import com.example.myapplication4.ui.perfil.Usuario;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TableLayout tableLayout;
    private FragmentHomeBinding binding;
    private List<String[]> datos = new ArrayList<>();
    List<ConsumoDiario> consumos = new ArrayList<>();
    double calTotales = 0;
    double calConsumidas = 0;
    double calRestantes = 0;
    double carbsTotales = 0;
    double carbsConsumidos = 0;
    double carbsRestantes = 0;
    double protesTotales = 0;
    double protesConsumidos = 0;
    double protesRestantes = 0;
    double grasasTotales = 0;
    double grasasConsumidas = 0;
    double grasasRestantes = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tableLayout = binding.tableLayout;

        new Thread(() -> {
            obtenerObjetivos();
            obtenerConsumos();
            calcularConsumos();

            getActivity().runOnUiThread(() -> {
                PieChart chartCarbohidratos = binding.carbohidratosChart;
                List<PieEntry> carbohidratosEntries = new ArrayList<>();
                carbohidratosEntries.add(new PieEntry((float) carbsRestantes, "Restante"));
                carbohidratosEntries.add(new PieEntry((float) carbsConsumidos, "Consumido"));

                PieDataSet carbSet = new PieDataSet(carbohidratosEntries, "Carbohidratos");
                carbSet.setColors(ColorTemplate.MATERIAL_COLORS);
                carbSet.setValueTextSize(10f);
                carbSet.setValueTextColor(R.color.black);
                PieData carbData = new PieData(carbSet);
                chartCarbohidratos.setData(carbData);
                chartCarbohidratos.getDescription().setEnabled(false);
                chartCarbohidratos.animateY(500);
                chartCarbohidratos.setCenterText("Carbohidratos (g)");
                chartCarbohidratos.setCenterTextSize(10f);
                chartCarbohidratos.getLegend().setEnabled(false);
                chartCarbohidratos.invalidate();

                PieChart chartProteinas = binding.proteinasChart;
                List<PieEntry> proteinasEntries = new ArrayList<>();
                proteinasEntries.add(new PieEntry((float) protesRestantes, "Restante"));
                proteinasEntries.add(new PieEntry((float)protesConsumidos, "Consumido"));

                PieDataSet protSet = new PieDataSet(proteinasEntries, "Proteínas");
                protSet.setColors(ColorTemplate.MATERIAL_COLORS);
                protSet.setValueTextSize(10f);
                carbSet.setValueTextColor(R.color.black);
                PieData protData = new PieData(protSet);
                chartProteinas.setData(protData);
                chartProteinas.getDescription().setEnabled(false);
                chartProteinas.animateY(500);
                chartProteinas.setCenterText("Proteínas (g)");
                chartProteinas.setCenterTextSize(10f);
                chartProteinas.getLegend().setEnabled(false);
                chartProteinas.invalidate();

                PieChart chartGrasas = binding.grasasChart;
                List<PieEntry> grasasEntries = new ArrayList<>();
                grasasEntries.add(new PieEntry((float) grasasRestantes, "Restante"));
                grasasEntries.add(new PieEntry((float)grasasConsumidas, "Consumido"));

                PieDataSet grasSet = new PieDataSet(grasasEntries, "Grasas");
                grasSet.setColors(ColorTemplate.MATERIAL_COLORS);
                grasSet.setValueTextSize(10f);
                carbSet.setValueTextColor(R.color.black);
                PieData grasData = new PieData(grasSet);
                chartGrasas.setData(grasData);
                chartGrasas.getDescription().setEnabled(false);
                chartGrasas.animateY(500);
                chartGrasas.setCenterText("Grasas (g)");
                chartGrasas.setCenterTextSize(10f);
                chartGrasas.getLegend().setEnabled(false);
                chartGrasas.invalidate();

                PieChart chartCalorias = binding.caloriasChart;
                List<PieEntry> caloriasEntries = new ArrayList<>();
                caloriasEntries.add(new PieEntry((float) calRestantes, "Restante"));
                caloriasEntries.add(new PieEntry((float)calConsumidas, "Consumido"));

                PieDataSet calSet = new PieDataSet(caloriasEntries, "Calorías");
                calSet.setColors(ColorTemplate.MATERIAL_COLORS);
                calSet.setValueTextSize(10f);
                carbSet.setValueTextColor(R.color.black);
                PieData calData = new PieData(calSet);
                chartCalorias.setData(calData);
                chartCalorias.getDescription().setEnabled(false);
                chartCalorias.animateY(500);
                chartCalorias.setCenterText("Calorías (kcal)");
                chartCalorias.setCenterTextSize(10f);
                chartCalorias.getLegend().setEnabled(false);
                chartCalorias.invalidate();

                for(ConsumoDiario consumo : consumos) {
                    datos.add(new String[] {consumo.getNombre(), consumo.getTamano_racion(), String.valueOf(consumo.getCalorias()), String.valueOf(consumo.getCantidad()), consumo.getMomento() });
                }
                cargarTabla();
            });
        }).start();

        return root;
    }

    private void cargarTabla() {
        // Elimina todas las vistas previas de la tabla
        tableLayout.removeAllViews();

        // Crea la fila de encabezados
        TableRow headerRow = new TableRow(getContext());
        for (String encabezado : new String[] {"Nombre", "Racion", "Calorias", "Cantidad", "Horario"}) {
            TextView textView = new TextView(getContext());
            textView.setText(encabezado);
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16); // Ajusta el tamaño de texto si es necesario
            textView.setTypeface(null, Typeface.BOLD); // Resalta el texto de los encabezados
            textView.setGravity(Gravity.CENTER); // Centra el texto en cada celda
            headerRow.addView(textView);
        }
        tableLayout.addView(headerRow);

        // Agrega las filas de datos
        for (String[] fila : datos) {
            TableRow dataRow = new TableRow(getContext());
            for (String celda : fila) {
                TextView textView = new TextView(getContext());
                textView.setText(celda);
                textView.setPadding(16, 16, 16, 16);
                textView.setTextSize(14); // Ajusta el tamaño de texto si es necesario
                textView.setGravity(Gravity.CENTER); // Centra el texto en cada celda
                dataRow.addView(textView);
            }
            tableLayout.addView(dataRow);
        }
    }


    private void obtenerObjetivos(){
        HashMap<String, Object> respuestaObjetivos = UsuarioDAO.consultarUsuario("skywhite");
        Usuario usuario = (Usuario) respuestaObjetivos.get("objeto");
        calTotales = usuario.getCalorias();
        carbsTotales = usuario.getCarbohidratos();
        protesTotales = usuario.getProteinas();
        grasasTotales = usuario.getGrasas();
    }

    private void obtenerConsumos(){
        HashMap<String, Object> respuesta = ConsumoDAO.consultarConsumosHoy("skywhite", "2024-12-05");
        if(respuesta.get("error").equals(true)){
            System.out.println(respuesta.get("mensaje"));
        }
        consumos = (List<ConsumoDiario>) respuesta.get("objeto");
    }

    private void calcularConsumos() {
        for(ConsumoDiario consumo : consumos) {
            calConsumidas += consumo.getCalorias() * consumo.getCantidad();
            carbsConsumidos += consumo.getCarbohidratos() * consumo.getCantidad();
            protesConsumidos += consumo.getProteinas() * consumo.getCantidad();
            grasasConsumidas += consumo.getGrasas() * consumo.getCantidad();
        }

        calRestantes = calTotales - calConsumidas;
        carbsRestantes = carbsTotales - carbsConsumidos;
        protesRestantes = protesTotales - protesConsumidos;
        grasasRestantes = grasasTotales - grasasConsumidas;
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
