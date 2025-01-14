package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.ApiService;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.example.myapplication4.ui.modelos.RegistroUsuario;
import com.example.myapplication4.ui.modelos.Objetivo;
import com.example.myapplication4.ui.daos.UsuarioDAO;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarObjetivos extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 2;

    private boolean isPhotoSelected = false;
    private EditText nombre_usuario, nombre, apellido_paterno, apellido_materno, correo, contrasena, estatura, peso, fecha_nacimiento;
    private EditText calorias, carbohidratos, grasas, proteinas;
    private Spinner spinnerSexo;
    private ImageView imageView;
    private Button btnRegistrar, btnSalir;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_objetivos);

        initializeViewElements();
        setupSpinnerSexo();
        setupDatePicker();
        setupButtonListeners();
    }

    private void initializeViewElements() {
        nombre = findViewById(R.id.nombre);
        apellido_paterno = findViewById(R.id.apellido_paterno);
        apellido_materno = findViewById(R.id.apellido_materno);
        nombre_usuario = findViewById(R.id.nombre_usuario);
        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        estatura = findViewById(R.id.estatura);
        peso = findViewById(R.id.peso);
        fecha_nacimiento = findViewById(R.id.fecha_nacimiento);
        spinnerSexo = findViewById(R.id.sexo);
        imageView = findViewById(R.id.imageView);
        calorias = findViewById(R.id.calorias);
        carbohidratos = findViewById(R.id.carbohidratos);
        grasas = findViewById(R.id.grasas);
        proteinas = findViewById(R.id.proteinas);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnSalir = findViewById(R.id.btnCancelar);
    }


    private void setupSpinnerSexo() {
        String[] options = {"Masculino", "Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);
    }


    private void setupDatePicker() {
        fecha_nacimiento.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -18);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegistrarObjetivos.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        fecha_nacimiento.setText(formattedDate);
                    },
                    year,
                    month,
                    day
            );

            // Calcular la fecha máxima (hace 15 años)
            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.add(Calendar.YEAR, -15);
            long maxDate = maxCalendar.getTimeInMillis();

            // Calcular la fecha mínima (hace 100 años)
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.add(Calendar.YEAR, -100);
            long minDate = minCalendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.show();
        });
    }


    private void setupButtonListeners() {
        findViewById(R.id.btnGaleria).setOnClickListener(v -> openGallery());
        btnSalir.setOnClickListener(v -> finish());
        btnRegistrar.setOnClickListener(v -> handleRegistration());
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    private String convertirIvAString() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void handleRegistration() {
        if (validateInputs()) {

            String foto = convertirIvAString();
            String nombreFoto = "";
            try {
                nombreFoto = UsuarioDAO.guardarFotoPerfil(nombre_usuario.getText().toString(), "jpeg", foto);
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressDialog = ProgressDialog.show(this, "Registrando", "Por favor espera...", true, false);


            UsuarioMovil usuarioMovil = new UsuarioMovil();
            usuarioMovil.setNombre(nombre.getText().toString());
            usuarioMovil.setFoto(nombreFoto);
            usuarioMovil.setApellido_paterno(apellido_paterno.getText().toString());
            usuarioMovil.setApellido_materno(apellido_materno.getText().toString());
            usuarioMovil.setNombre_usuario(nombre_usuario.getText().toString());
            usuarioMovil.setCorreo(correo.getText().toString());
            usuarioMovil.setContrasena(contrasena.getText().toString());
            usuarioMovil.setEstatura(Double.parseDouble(estatura.getText().toString()));
            usuarioMovil.setPeso(Double.parseDouble(peso.getText().toString()));
            usuarioMovil.setSexo(spinnerSexo.getSelectedItem().toString().equals("Masculino"));
            String fechaFormateada = formatearFecha();
            usuarioMovil.setFecha_nacimiento(fechaFormateada);
            System.out.println(usuarioMovil.getFecha_nacimiento());

            Objetivo objetivo = new Objetivo();
            objetivo.setCalorias(Double.parseDouble(calorias.getText().toString()));
            objetivo.setCarbohidratos(Double.parseDouble(carbohidratos.getText().toString()));
            objetivo.setGrasas(Double.parseDouble(grasas.getText().toString()));
            objetivo.setProteinas(Double.parseDouble(proteinas.getText().toString()));

            RegistroUsuario registroUsuario = new RegistroUsuario();
            registroUsuario.setUsuarioMovil(usuarioMovil);
            registroUsuario.setObjetivo(objetivo);

            UsuarioDAO.registrarUsuario(registroUsuario, this);
        }
    }

    private String formatearFecha() {
        String fechaFormateada = "";
        try {
            String fechaTexto = fecha_nacimiento.getText().toString(); // Ejemplo: "12/01/2025"

            // Define el formato actual (de entrada) de la fecha
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Define el formato deseado (de salida)
            SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Convierte la fecha del texto a un objeto Date
            Date fecha = formatoEntrada.parse(fechaTexto);

            // Convierte el objeto Date al nuevo formato
            fechaFormateada = formatoSalida.format(fecha);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fechaFormateada;
    }

    private boolean validateInputs() {
        if (!isPhotoSelected) {
            Toast.makeText(this, "Por favor, selecciona una foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(nombre.getText()) || TextUtils.isEmpty(correo.getText()) || !Patterns.EMAIL_ADDRESS.matcher(correo.getText()).matches()) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private UsuarioMovil buildUsuarioMovil() {
        UsuarioMovil usuarioMovil = new UsuarioMovil();
        usuarioMovil.setNombre_usuario(getTextFromField(nombre_usuario));
        usuarioMovil.setNombre(getTextFromField(nombre));
        usuarioMovil.setApellido_paterno(getTextFromField(apellido_paterno));
        usuarioMovil.setApellido_materno(getTextFromField(apellido_materno));
        usuarioMovil.setCorreo(getTextFromField(correo));
        usuarioMovil.setContrasena(getTextFromField(contrasena));
        usuarioMovil.setEstatura(Double.parseDouble(getTextFromField(estatura)));
        usuarioMovil.setPeso(Double.parseDouble(getTextFromField(peso)));
        usuarioMovil.setSexo(spinnerSexo.getSelectedItemPosition() == 0); // 0: Masculino, 1: Femenino
        usuarioMovil.setFecha_nacimiento(getTextFromField(fecha_nacimiento));
        return usuarioMovil;
    }

    private Objetivo buildObjetivo() {
        Objetivo objetivo = new Objetivo();
        objetivo.setCalorias(Double.parseDouble(getTextFromField(calorias)));
        objetivo.setCarbohidratos(Double.parseDouble(getTextFromField(carbohidratos)));
        objetivo.setGrasas(Double.parseDouble(getTextFromField(grasas)));
        objetivo.setProteinas(Double.parseDouble(getTextFromField(proteinas)));
        return objetivo;
    }

    private String getTextFromField(EditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try (InputStream imageStream = getContentResolver().openInputStream(selectedImage)) {
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(bitmap);
                isPhotoSelected = true;
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error desconocido al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
