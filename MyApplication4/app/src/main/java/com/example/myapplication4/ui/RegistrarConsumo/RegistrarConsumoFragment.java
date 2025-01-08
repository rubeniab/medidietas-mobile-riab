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

import java.util.Arrays;

public class RegistrarConsumoFragment extends Fragment {

    private TableLayout tableLayout;
    private EditText searchBar;

    private String[][] datos = {
            {"Manzana", "1 unidad", "52", "Fruta"},
            {"Banana", "1 unidad", "89", "Fruta"},
            {"Arroz", "1 taza", "130", "Cereal"},
            {"Pollo", "1 pieza", "165", "Proteína"},
            {"Leche", "1 vaso", "42", "Lácteo"}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registrar_consumo, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);
        searchBar = view.findViewById(R.id.search_bar);


        cargarTabla(datos);


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

    private void cargarTabla(String[][] datos) {
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
        String[][] datosFiltrados = Arrays.stream(datos)
                .filter(fila -> fila[0].toLowerCase().contains(query.toLowerCase()))
                .toArray(String[][]::new);
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