package com.example.myapplication4.ui.RegistrarConsumo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.myapplication4.R;
import com.example.myapplication4.ui.daos.ConsumoDAO;
import com.example.myapplication4.ui.modelos.Alimento;
import com.example.myapplication4.ui.modelos.Comida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistrarConsumoFragment extends Fragment {

    private TableLayout tableLayout;
    private EditText searchBar;
    private List<String[]> datos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registrar_consumo, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);
        searchBar = view.findViewById(R.id.search_bar);

        new Thread(() -> {
            // Obtener alimentos
            HashMap<String, Object> respuestaAlimentos = ConsumoDAO.obtenerAlimentos(getContext());
            if (!(boolean) respuestaAlimentos.get("error")) {
                List<Alimento> alimentos = (List<Alimento>) respuestaAlimentos.get("objeto");
                for (Alimento alimento : alimentos) {
                    datos.add(new String[]{
                            alimento.getNombre(),
                            alimento.getTamanoRacion() + " " + alimento.getIdUnidadMedida(),
                            String.valueOf(alimento.getCalorias()),
                            "Alimento"
                    });
                }
            } else {
                System.out.println("Error: " + respuestaAlimentos.get("mensaje"));
            }

            // Obtener comidas
            HashMap<String, Object> respuestaComidas = ConsumoDAO.obtenerComidas(getContext());
            if (!(boolean) respuestaComidas.get("error")) {
                List<Comida> comidas = (List<Comida>) respuestaComidas.get("objeto");
                for (Comida comida : comidas) {
                    datos.add(new String[]{
                            comida.getNombre(),
                            "1 unidad",
                            "indefinido",
                            "Comida"
                    });
                }
            } else {
                System.out.println("Error: " + respuestaComidas.get("mensaje"));
            }

            // Actualizar la UI en el hilo principal
            getActivity().runOnUiThread(() -> cargarTabla(datos));
        }).start();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String textoBusqueda = charSequence.toString();
                if (!textoBusqueda.isEmpty()) {
                    filtrarTabla(textoBusqueda);
                } else {
                    cargarTabla(datos);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return view;
    }

    private void cargarTabla(List<String[]> datos) {
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(getContext());
        for (String encabezado : new String[]{"Nombre", "Ración", "Calorías", "Categoría"}) {
            TextView textView = new TextView(getContext());
            textView.setText(encabezado);
            textView.setPadding(16, 16, 16, 16);
            headerRow.addView(textView);
        }
        tableLayout.addView(headerRow);

        for (String[] fila : datos) {
            TableRow row = new TableRow(getContext());
            for (String dato : fila) {
                TextView textView = new TextView(getContext());
                textView.setText(dato);
                textView.setPadding(16, 16, 16, 16);
                row.addView(textView);
            }

            row.setOnClickListener(v -> showResumenDialog(fila));

            tableLayout.addView(row);
        }
    }

    private void filtrarTabla(String query) {
        List<String[]> datosFiltrados = new ArrayList<>();
        for (String[] fila : datos) {
            if (fila[0].toLowerCase().contains(query.toLowerCase())) {
                datosFiltrados.add(fila);
            }
        }
        cargarTabla(datosFiltrados);
    }

    private void showResumenDialog(String[] fila) {
        String resumen = "Nombre: " + fila[0] + "\nRación: " + fila[1] + "\nCalorías: " + fila[2] + "\nCategoría: " + fila[3];

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Resumen");
        builder.setMessage(resumen);
        builder.setPositiveButton("Agregar", (dialog, id) -> {
            Toast.makeText(getContext(), "Agregado con éxito", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cerrar", null);
        builder.create().show();
    }
}