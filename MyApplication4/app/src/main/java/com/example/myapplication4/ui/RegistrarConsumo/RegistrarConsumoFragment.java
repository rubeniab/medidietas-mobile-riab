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
import com.example.myapplication4.ui.modelos.Categoria;
import com.example.myapplication4.ui.modelos.Comida;
import com.example.myapplication4.ui.modelos.UnidadMedida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistrarConsumoFragment extends Fragment {

    private TableLayout tableLayout;
    private EditText searchBar;
    private List<String[]> datos = new ArrayList<>();
    private HashMap<Integer, String> categoriasMap = new HashMap<>();
    private HashMap<Integer, String> unidadesMedidaMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registrar_consumo, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);
        searchBar = view.findViewById(R.id.search_bar);

        new Thread(() -> {
            // Obtener categorías
            HashMap<String, Object> respuestaCategorias = ConsumoDAO.obtenerCategorias(getContext());
            if (!(boolean) respuestaCategorias.get("error")) {
                List<Categoria> categorias = (List<Categoria>) respuestaCategorias.get("objeto");
                for (Categoria categoria : categorias) {
                    categoriasMap.put(categoria.getId(), categoria.getNombre());
                }
            } else {
                System.out.println("Error: " + respuestaCategorias.get("mensaje"));
            }

            // Obtener unidades de medida
            HashMap<String, Object> respuestaUnidadesMedida = ConsumoDAO.obtenerUnidadesMedida(getContext());
            if (!(boolean) respuestaUnidadesMedida.get("error")) {
                List<UnidadMedida> unidadesMedida = (List<UnidadMedida>) respuestaUnidadesMedida.get("objeto");
                for (UnidadMedida unidadMedida : unidadesMedida) {
                    unidadesMedidaMap.put(unidadMedida.getId(), unidadMedida.getNombre());
                }
            } else {
                System.out.println("Error: " + respuestaUnidadesMedida.get("mensaje"));
            }

            // Obtener alimentos
            HashMap<String, Object> respuestaAlimentos = ConsumoDAO.obtenerAlimentos(getContext());
            if (!(boolean) respuestaAlimentos.get("error")) {
                List<Alimento> alimentos = (List<Alimento>) respuestaAlimentos.get("objeto");
                for (Alimento alimento : alimentos) {
                    String categoriaNombre = categoriasMap.getOrDefault(alimento.getIdCategoria(), "Desconocido");
                    String unidadMedidaNombre = unidadesMedidaMap.getOrDefault(alimento.getIdUnidadMedida(), "Desconocido");
                    String tamanoRacion = alimento.getTamanoRacion() + " " + unidadMedidaNombre;
                    if (alimento.getTamanoRacion() > 1) {
                        tamanoRacion += "s";
                    }
                    datos.add(new String[]{
                            alimento.getNombre(),
                            tamanoRacion,
                            String.valueOf(alimento.getCalorias()),
                            categoriaNombre
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