package com.example.myapplication4.ui.home;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication4.R;
import com.example.myapplication4.databinding.FragmentHomeBinding;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.daos.ConsumoDAO;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.example.myapplication4.ui.modelos.ConsumoDiario;
import com.example.myapplication4.ui.modelos.Momento;
import com.example.myapplication4.ui.perfil.Usuario;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {private TableLayout tableLayout;
    private FragmentHomeBinding binding;
    private List<String[]> datos = new ArrayList<>();
    private List<ConsumoDiario> consumos = new ArrayList<>();
    private List<Momento> momentosList = new ArrayList<>();
    private double calTotales = 0;
    private double calConsumidas = 0;
    private double calRestantes = 0;
    private double carbsTotales = 0;
    private double carbsConsumidos = 0;
    private double carbsRestantes = 0;
    private double protesTotales = 0;
    private double protesConsumidos = 0;
    private double protesRestantes = 0;
    private double grasasTotales = 0;
    private double grasasConsumidas = 0;
    private double grasasRestantes = 0;

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
            obtenerMomentos();
            calcularConsumos();

            getActivity().runOnUiThread(() -> {
                cargarTabla();
                actualizarGraficas();
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

        // Verificar que la lista consumos no sea null
        if (consumos == null) {
            consumos = new ArrayList<>();
        }

        // Agrega las filas de datos
        for (int i = 0; i < consumos.size(); i++) {
            ConsumoDiario consumo = consumos.get(i);
            TableRow dataRow = new TableRow(getContext());
            dataRow.setPadding(16, 16, 16, 16);
            dataRow.setGravity(Gravity.CENTER);

            TextView nombreTextView = new TextView(getContext());
            nombreTextView.setText(consumo.getNombre());
            nombreTextView.setPadding(16, 16, 16, 16);
            nombreTextView.setGravity(Gravity.CENTER);
            dataRow.addView(nombreTextView);

            TextView racionTextView = new TextView(getContext());
            racionTextView.setText(consumo.getTamano_racion());
            racionTextView.setPadding(16, 16, 16, 16);
            racionTextView.setGravity(Gravity.CENTER);
            dataRow.addView(racionTextView);

            TextView caloriasTextView = new TextView(getContext());
            caloriasTextView.setText(String.valueOf(consumo.getCalorias()));
            caloriasTextView.setPadding(16, 16, 16, 16);
            caloriasTextView.setGravity(Gravity.CENTER);
            dataRow.addView(caloriasTextView);

            TextView cantidadTextView = new TextView(getContext());
            cantidadTextView.setText(String.valueOf(consumo.getCantidad()));
            cantidadTextView.setPadding(16, 16, 16, 16);
            cantidadTextView.setGravity(Gravity.CENTER);
            dataRow.addView(cantidadTextView);

            TextView momentoTextView = new TextView(getContext());
            momentoTextView.setText(consumo.getMomento());
            momentoTextView.setPadding(16, 16, 16, 16);
            momentoTextView.setGravity(Gravity.CENTER);
            dataRow.addView(momentoTextView);

            int finalI = i;
            dataRow.setOnClickListener(v -> showResumenDialog(consumos.get(finalI)));

            tableLayout.addView(dataRow);
        }
    }

    private void showResumenDialog(ConsumoDiario consumo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Modificar consumo");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resumen_consumo, null);
        builder.setView(dialogView);

        TextView resumenTextView = dialogView.findViewById(R.id.resumen_text_view);
        resumenTextView.setText("Nombre: " + consumo.getNombre() +
                "\nRación: " + consumo.getTamano_racion() +
                "\nCalorías: " + consumo.getCalorias());

        EditText cantidadEditText = dialogView.findViewById(R.id.cantidad_edit_text);
        cantidadEditText.setText(String.valueOf(consumo.getCantidad()));

        Spinner momentoSpinner = dialogView.findViewById(R.id.momento_spinner);
        List<String> momentosNombres = new ArrayList<>();
        for (Momento momento : momentosList) {
            momentosNombres.add(momento.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, momentosNombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        momentoSpinner.setAdapter(adapter);

        int momentoIndex = momentosNombres.indexOf(consumo.getMomento());
        if (momentoIndex != -1) {
            momentoSpinner.setSelection(momentoIndex);
        }

        builder.setPositiveButton("Modificar", (dialog, id) -> {
            double nuevaCantidad = Double.parseDouble(cantidadEditText.getText().toString());
            int nuevoIdMomento = momentosList.get(momentoSpinner.getSelectedItemPosition()).getId();

            new Thread(() -> {
                HashMap<String, Object> respuesta = ConsumoDAO.modificarConsumo(getContext(), consumo.getId(), nuevaCantidad, nuevoIdMomento);
                getActivity().runOnUiThread(() -> {
                    if (!(boolean) respuesta.get("error")) {
                        Toast.makeText(getContext(), "Consumo modificado correctamente", Toast.LENGTH_SHORT).show();
                        new Thread(() -> {
                            obtenerConsumos(); // Actualizar la lista de consumos
                            calcularConsumos(); // Recalcular los consumos
                            getActivity().runOnUiThread(() -> {
                                cargarTabla(); // Recargar la tabla con los datos actualizados
                                actualizarGraficas(); // Actualizar las gráficas
                            });
                        }).start();
                    } else {
                        Toast.makeText(getContext(), "Error al modificar el consumo: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        builder.setNegativeButton("Cerrar", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        Button eliminarButton = dialogView.findViewById(R.id.eliminar_button);
        eliminarButton.setOnClickListener(v -> {
            new Thread(() -> {
                HashMap<String, Object> respuesta = ConsumoDAO.eliminarConsumo(getContext(), consumo.getId());
                getActivity().runOnUiThread(() -> {
                    if (!(boolean) respuesta.get("error")) {
                        Toast.makeText(getContext(), "Consumo eliminado correctamente", Toast.LENGTH_SHORT).show();
                        new Thread(() -> {
                            obtenerConsumos(); // Actualizar la lista de consumos
                            calcularConsumos(); // Recalcular los consumos
                            getActivity().runOnUiThread(() -> {
                                cargarTabla(); // Recargar la tabla con los datos actualizados
                                actualizarGraficas(); // Actualizar las gráficas
                            });
                        }).start();
                        dialog.dismiss(); // Cerrar el diálogo después de eliminar
                    } else {
                        Toast.makeText(getContext(), "Error al eliminar el consumo: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }

    private void obtenerObjetivos() {
        HashMap<String, Object> respuestaObjetivos = UsuarioDAO.consultarUsuario(Constantes.NOMBRE_USUARIO);
        if (respuestaObjetivos.get("error").equals(true)) {
            System.out.println(respuestaObjetivos.get("mensaje"));
            return;
        }

        Usuario usuario = (Usuario) respuestaObjetivos.get("objeto");
        if (usuario == null) {
            System.out.println("Error: Usuario no encontrado");
            return;
        }

        calTotales = usuario.getCalorias();
        carbsTotales = usuario.getCarbohidratos();
        protesTotales = usuario.getProteinas();
        grasasTotales = usuario.getGrasas();
    }

    private void obtenerConsumos() {
        // Obtener la fecha actual
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

        HashMap<String, Object> respuesta = ConsumoDAO.consultarConsumosHoy(Constantes.NOMBRE_USUARIO, fechaActual);
        if (respuesta.get("error").equals(true)) {
            System.out.println(respuesta.get("mensaje"));
            consumos = new ArrayList<>(); // Inicializar la lista vacía en caso de error
        } else {
            consumos = (List<ConsumoDiario>) respuesta.get("objeto");
            if (consumos == null) {
                consumos = new ArrayList<>(); // Inicializar la lista vacía si es null
            }
        }
    }

    private void obtenerMomentos() {
        HashMap<String, Object> respuestaMomentos = ConsumoDAO.obtenerMomentos(getContext());
        if (!(boolean) respuestaMomentos.get("error")) {
            momentosList = (List<Momento>) respuestaMomentos.get("objeto");
        } else {
            System.out.println("Error: " + respuestaMomentos.get("mensaje"));
        }
    }

    private void calcularConsumos() {
        // Reiniciar los valores antes de calcular
        calConsumidas = 0;
        carbsConsumidos = 0;
        protesConsumidos = 0;
        grasasConsumidas = 0;

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

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void actualizarGraficas() {
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
    }
}