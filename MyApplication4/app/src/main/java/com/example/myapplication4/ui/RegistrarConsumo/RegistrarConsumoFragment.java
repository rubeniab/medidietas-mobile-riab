package com.example.myapplication4.ui.RegistrarConsumo;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.myapplication4.R;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.Utilidades.ConversorUrlYoutube;
import com.example.myapplication4.ui.daos.ConsumoDAO;
import com.example.myapplication4.ui.modelos.Alimento;
import com.example.myapplication4.ui.modelos.Categoria;
import com.example.myapplication4.ui.modelos.Comida;
import com.example.myapplication4.ui.modelos.Consumo;
import com.example.myapplication4.ui.modelos.Momento;
import com.example.myapplication4.ui.modelos.UnidadMedida;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class RegistrarConsumoFragment extends Fragment {

    private TableLayout tableLayout;
    private EditText searchBar;
    private List<String[]> datos = new ArrayList<>();
    private List<Alimento> alimentosList = new ArrayList<>();
    private List<Comida> comidasList = new ArrayList<>();
    private List<Momento> momentosList = new ArrayList<>();
    private HashMap<Integer, String> categoriasMap = new HashMap<>();
    private HashMap<Integer, String> unidadesMedidaMap = new HashMap<>();
    private double cantidadConsumo;
    private String momentoSeleccionado;
    private int idMomentoSeleccionado;

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
                    alimentosList.add(alimento);
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
                            String.valueOf(comida.getCalorias()),
                            "Comida"
                    });
                    comidasList.add(comida);
                }
            } else {
                System.out.println("Error: " + respuestaComidas.get("mensaje"));
            }

            // Obtener momentos
            HashMap<String, Object> respuestaMomentos = ConsumoDAO.obtenerMomentos(getContext());
            if (!(boolean) respuestaMomentos.get("error")) {
                momentosList = (List<Momento>) respuestaMomentos.get("objeto");
            } else {
                System.out.println("Error: " + respuestaMomentos.get("mensaje"));
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

        for (int i = 0; i < datos.size(); i++) {
            String[] fila = datos.get(i);
            TableRow row = new TableRow(getContext());
            for (String dato : fila) {
                TextView textView = new TextView(getContext());
                textView.setText(dato);
                textView.setPadding(16, 16, 16, 16);
                row.addView(textView);
            }

            int finalI = i;
            row.setOnClickListener(v -> {
                if (fila[3].equals("Comida")) {
                    showResumenDialogComida(comidasList.get(finalI - alimentosList.size()));
                } else {
                    showResumenDialog(alimentosList.get(finalI));
                }
            });

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

    private void showResumenDialog(Alimento alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Resumen");

        // Crear un layout para el diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resumen, null);
        builder.setView(dialogView);

        // Configurar los TextViews
        TextView resumenTextView = dialogView.findViewById(R.id.resumen_text_view);
        resumenTextView.setText("Nombre: " + alimento.getNombre() +
                "\nRación: " + alimento.getTamanoRacion() + " " + unidadesMedidaMap.getOrDefault(alimento.getIdUnidadMedida(), "Desconocido") +
                "\nCalorías: " + alimento.getCalorias());

        // Configurar el EditText para la cantidad
        EditText cantidadEditText = dialogView.findViewById(R.id.cantidad_edit_text);

        // Configurar el Spinner para los momentos
        Spinner momentoSpinner = dialogView.findViewById(R.id.momento_spinner);
        List<String> momentosNombres = new ArrayList<>();
        for (Momento momento : momentosList) {
            momentosNombres.add(momento.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, momentosNombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        momentoSpinner.setAdapter(adapter);

        // Configurar la gráfica de nutrientes
        PieChart nutrientesChart = dialogView.findViewById(R.id.nutrientesChart);
        List<PieEntry> nutrientesEntries = new ArrayList<>();
        nutrientesEntries.add(new PieEntry((float) alimento.getCarbohidratos(), "Carbohidratos"));
        nutrientesEntries.add(new PieEntry((float) alimento.getGrasas(), "Grasas"));
        nutrientesEntries.add(new PieEntry((float) alimento.getProteinas(), "Proteínas"));

        PieDataSet nutrientesSet = new PieDataSet(nutrientesEntries, "Nutrientes");
        nutrientesSet.setColors(ColorTemplate.MATERIAL_COLORS);
        nutrientesSet.setValueTextSize(10f);
        nutrientesSet.setValueTextColor(Color.BLACK); // Establecer el color del texto a negro
        nutrientesSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        nutrientesSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData nutrientesData = new PieData(nutrientesSet);
        nutrientesChart.setData(nutrientesData);
        nutrientesChart.getDescription().setEnabled(false);
        nutrientesChart.animateY(500);
        nutrientesChart.setCenterText("Nutrientes (g)");
        nutrientesChart.setCenterTextSize(10f);
        nutrientesChart.setCenterTextColor(Color.BLACK);
        nutrientesChart.getLegend().setTextColor(Color.BLACK);
        nutrientesChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        nutrientesChart.invalidate();

        builder.setPositiveButton("Agregar", (dialog, id) -> {
            String cantidadStr = cantidadEditText.getText().toString();
            if (!cantidadStr.isEmpty()) {
                cantidadConsumo = Double.parseDouble(cantidadStr);
                momentoSeleccionado = momentoSpinner.getSelectedItem().toString();
                idMomentoSeleccionado = momentosList.get(momentoSpinner.getSelectedItemPosition()).getId();

                // Obtener la fecha actual del sistema
                String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Crear el objeto Consumo
                Consumo consumo = new Consumo(fechaActual, cantidadConsumo, idMomentoSeleccionado, alimento.getId(), 1);

                // Registrar el consumo
                new Thread(() -> {
                    HashMap<String, Object> respuesta = ConsumoDAO.registrarConsumo(getContext(), consumo, false);
                    getActivity().runOnUiThread(() -> {
                        if (!(boolean) respuesta.get("error")) {
                            Toast.makeText(getContext(), "Consumo registrado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error al registrar el consumo: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            } else {
                Toast.makeText(getContext(), "Por favor, ingrese una cantidad", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cerrar", null);
        builder.create().show();
    }

    private void showResumenDialogComida(Comida comida) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Resumen");

        // Crear un layout para el diálogo
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_resumen, null);
        builder.setView(dialogView);

        // Configurar los TextViews
        TextView resumenTextView = dialogView.findViewById(R.id.resumen_text_view);
        resumenTextView.setText("Nombre: " + comida.getNombre() +
                "\nCalorías: " + comida.getCalorias());

        // Configurar el EditText para la cantidad
        EditText cantidadEditText = dialogView.findViewById(R.id.cantidad_edit_text);

        // Configurar el Spinner para los momentos
        Spinner momentoSpinner = dialogView.findViewById(R.id.momento_spinner);
        List<String> momentosNombres = new ArrayList<>();
        for (Momento momento : momentosList) {
            momentosNombres.add(momento.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, momentosNombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        momentoSpinner.setAdapter(adapter);

        // Configurar la gráfica de nutrientes
        PieChart nutrientesChart = dialogView.findViewById(R.id.nutrientesChart);
        List<PieEntry> nutrientesEntries = new ArrayList<>();
        nutrientesEntries.add(new PieEntry((float) comida.getCarbohidratos(), "Carbohidratos"));
        nutrientesEntries.add(new PieEntry((float) comida.getGrasas(), "Grasas"));
        nutrientesEntries.add(new PieEntry((float) comida.getProteinas(), "Proteínas"));

        PieDataSet nutrientesSet = new PieDataSet(nutrientesEntries, "Nutrientes");
        nutrientesSet.setColors(ColorTemplate.MATERIAL_COLORS);
        nutrientesSet.setValueTextSize(10f);
        nutrientesSet.setValueTextColor(Color.BLACK); // Establecer el color del texto a negro
        nutrientesSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        nutrientesSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData nutrientesData = new PieData(nutrientesSet);
        nutrientesChart.setData(nutrientesData);
        nutrientesChart.getDescription().setEnabled(false);
        nutrientesChart.animateY(500);
        nutrientesChart.setCenterText("Nutrientes (g)");
        nutrientesChart.setCenterTextSize(10f);
        nutrientesChart.setCenterTextColor(Color.BLACK);
        nutrientesChart.getLegend().setTextColor(Color.BLACK);
        nutrientesChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        nutrientesChart.invalidate();

        // Configurar el WebView para el video
        WebView videoWebView = dialogView.findViewById(R.id.video_web_view);
        videoWebView.getSettings().setJavaScriptEnabled(true);
        videoWebView.getSettings().setMediaPlaybackRequiresUserGesture(false); // Habilitar la reproducción de medios
        videoWebView.getSettings().setDomStorageEnabled(true);
        videoWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        videoWebView.setWebChromeClient(new WebChromeClient()); // Habilitar WebChromeClient para la reproducción de video

        String videoUrl = comida.getPreparacionVideo();
        if (videoUrl != null && !videoUrl.isEmpty()) {
            String videoUrlConvertida = ConversorUrlYoutube.convertirUrl(videoUrl);
            videoWebView.loadUrl(videoUrlConvertida);
        }

        builder.setPositiveButton("Agregar", (dialog, id) -> {
            String cantidadStr = cantidadEditText.getText().toString();
            if (!cantidadStr.isEmpty()) {
                cantidadConsumo = Double.parseDouble(cantidadStr);
                momentoSeleccionado = momentoSpinner.getSelectedItem().toString();
                idMomentoSeleccionado = momentosList.get(momentoSpinner.getSelectedItemPosition()).getId();

                // Obtener la fecha actual del sistema
                String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // Crear el objeto Consumo
                Consumo consumo = new Consumo(fechaActual, cantidadConsumo, idMomentoSeleccionado, comida.getId(), Constantes.ID_USUARIO);

                // Registrar el consumo
                new Thread(() -> {
                    HashMap<String, Object> respuesta = ConsumoDAO.registrarConsumo(getContext(), consumo, true);
                    getActivity().runOnUiThread(() -> {
                        if (!(boolean) respuesta.get("error")) {
                            Toast.makeText(getContext(), "Consumo registrado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error al registrar el consumo: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            } else {
                Toast.makeText(getContext(), "Por favor, ingrese una cantidad", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cerrar", null);
        builder.create().show();
    }
}