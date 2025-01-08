package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarObjetivos extends AppCompatActivity {
    private boolean isPhotoSelected = false;
    private EditText nombre_usuario, nombre, apellido_paterno, apellido_materno, correo, contrasena, estatura, peso, fecha_nacimiento;
    private EditText calorias, carbohidratos, grasas, proteinas;
    private Spinner spinnerSexo;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imageView;
    private Button btnRegistrarObjetivos, btnCancelarRegistroObjetivos, btnRegistrar;
    private ProgressDialog progressDialog; // Agregamos ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_objetivos);

        // Referencias a los elementos del diseño
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
        Button openGalleryButton = findViewById(R.id.btnGaleria);

        String[] options = {"Masculino", "Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        // Acción al hacer clic en el campo de fecha de nacimiento para mostrar un DatePicker
        fecha_nacimiento.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
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

            long maxDate = calendar.getTimeInMillis();
            calendar.set(year - 100, Calendar.JANUARY, 1);
            long minDate = calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.show();
        });

        openGalleryButton.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
        });

        btnRegistrar.setOnClickListener(v -> {
            String nombreText = getTextFromField(nombre);
            String nombreUsuarioText = getTextFromField(nombre_usuario);
            String apellidoPaternoText = getTextFromField(apellido_paterno);
            String apellidoMaternoText = getTextFromField(apellido_materno);
            String correoText = getTextFromField(correo);
            String contrasenaText = getTextFromField(contrasena);
            String estaturaText = getTextFromField(estatura);
            String pesoText = getTextFromField(peso);
            String fechaNacimientoText = getTextFromField(fecha_nacimiento);
            int selectedPosition = spinnerSexo.getSelectedItemPosition();

            if (!isPhotoSelected) {
                Toast.makeText(RegistrarObjetivos.this, "Por favor, selecciona una foto", Toast.LENGTH_SHORT).show();
                return;
            }


            // Validación de campos
            if (TextUtils.isEmpty(nombre.getText().toString()) || TextUtils.isEmpty(nombre_usuario.getText().toString()) ||
                    TextUtils.isEmpty(apellido_paterno.getText().toString()) || TextUtils.isEmpty(apellido_materno.getText().toString()) ||
                    TextUtils.isEmpty(correo.getText().toString()) || TextUtils.isEmpty(contrasena.getText().toString()) ||
                    TextUtils.isEmpty(estatura.getText().toString()) || TextUtils.isEmpty(peso.getText().toString()) ||
                    TextUtils.isEmpty(fecha_nacimiento.getText().toString())) {
                Toast.makeText(RegistrarObjetivos.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(correo.getText().toString()).matches()) {
                Toast.makeText(RegistrarObjetivos.this, "Por favor, ingresa un correo válido", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Obtiene los valores de los campos
                String nombreUsuarioStr = nombre_usuario.getText().toString();
                String nombreStr = nombre.getText().toString();
                String apellidoPaternoStr = apellido_paterno.getText().toString();
                String apellidoMaternoStr = apellido_materno.getText().toString();
                String correoStr = correo.getText().toString();
                String contrasenaStr = contrasena.getText().toString();
                String fechaNacimientoStr = fecha_nacimiento.getText().toString();
                String carbohidratosStr = fecha_nacimiento.getText().toString();
                String grasasStr = fecha_nacimiento.getText().toString();
                String proteinastr = fecha_nacimiento.getText().toString();


                double estaturaVal = Double.parseDouble(estatura.getText().toString());
                double pesoVal = Double.parseDouble(peso.getText().toString());
                double caloriasVal = Double.parseDouble(calorias.getText().toString());
                double carbohidratosVal = Double.parseDouble(carbohidratos.getText().toString());
                double grasasVal = Double.parseDouble(grasas.getText().toString());
                double proteinasVal = Double.parseDouble(proteinas.getText().toString());
                boolean sexoVal = spinnerSexo.getSelectedItemPosition() == 0; // 0 para hombre, 1 para mujer

                // Crear objeto UsuarioMovil
                UsuarioMovil usuarioMovil = new UsuarioMovil();
                usuarioMovil.setNombre_usuario(nombreUsuarioStr);
                usuarioMovil.setNombre(nombreStr);
                usuarioMovil.setApellido_paterno(apellidoPaternoStr);
                usuarioMovil.setApellido_materno(apellidoMaternoStr);
                usuarioMovil.setContrasena(contrasenaStr);
                usuarioMovil.setCorreo(correoStr);
                usuarioMovil.setFecha_nacimiento(fechaNacimientoStr);
                usuarioMovil.setEstatura(estaturaVal);
                usuarioMovil.setPeso(pesoVal);
                usuarioMovil.setSexo(sexoVal);

                // Crear objeto Objetivo
                Objetivo objetivo = new Objetivo();
                objetivo.setCalorias(caloriasVal);
                objetivo.setCarbohidratos(carbohidratosVal);
                objetivo.setGrasas(grasasVal);
                objetivo.setProteinas(proteinasVal);

                // Crear objeto RegistroUsuario
                RegistroUsuario registroUsuario = new RegistroUsuario();
                registroUsuario.setUsuarioMovil(usuarioMovil);
                registroUsuario.setObjetivo(objetivo);

                // Mostrar ProgressDialog mientras se hace la llamada a la API
                progressDialog = new ProgressDialog(RegistrarObjetivos.this);
                progressDialog.setMessage("Registrando usuario...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Llamar a la función para enviar la solicitud
                registrarUsuario(registroUsuario);

            } catch (NumberFormatException e) {
                Toast.makeText(RegistrarObjetivos.this, "Por favor, ingresa valores numéricos válidos para estatura y peso", Toast.LENGTH_SHORT).show();
            }
        });
    }    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void registrarUsuario(RegistroUsuario registroUsuario) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.registrarUsuario(registroUsuario);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss(); // Ocultar ProgressDialog al recibir la respuesta
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrarObjetivos.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarObjetivos.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss(); // Ocultar ProgressDialog al fallar
                Toast.makeText(RegistrarObjetivos.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
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
                isPhotoSelected = true; // Marcar la foto como seleccionada
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error desconocido al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
