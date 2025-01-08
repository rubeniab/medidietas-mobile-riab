package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication4.databinding.ActivityRegistrarObjetivosBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registrarUsuarioActivity extends AppCompatActivity {

    //Constante para identificar la solicitud de selección de imagen
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imageView;
    private boolean isPhotoSelected = false; // Bandera para verificar si la foto fue seleccionada
    private Button btnRegistrar, btnCancelar;
    private TextInputEditText nombre, apellido_paterno, apellido_materno, nombre_usuario, correo, contrasena, estatura, peso, fecha_nacimiento;
    private Spinner spinnerSexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        //Referencias a los elementos del diseño
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
        btnRegistrar = findViewById(R.id.btnRegistrarUsuario);
        btnCancelar = findViewById(R.id.btnCancelarRegistroUsuario);
        Button openGalleryButton = findViewById(R.id.btnGaleria);

        String[] options = {"Masculino", "Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        //Acción al hacer clic en el botón de registro
        btnRegistrar.setOnClickListener(v -> {
            finish();
        });

        //Acción al hacer clic en el campo de fecha de nacimiento para mostrar un DatePicker
        fecha_nacimiento.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    registrarUsuarioActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        fecha_nacimiento.setText(formattedDate);
                    },
                    year,
                    month,
                    day
            );

            //Configuración de límites para la selección de fecha
            long maxDate = calendar.getTimeInMillis();
            calendar.set(year - 100, Calendar.JANUARY, 1);
            long minDate = calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.show();
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
                Toast.makeText(registrarUsuarioActivity.this, "Por favor, selecciona una foto", Toast.LENGTH_SHORT).show();
                return; // Detener la ejecución si no se seleccionó una foto
            }

            //Validaciones
            if (nombreText.isEmpty() || nombreUsuarioText.isEmpty() || apellidoPaternoText.isEmpty() || apellidoMaternoText.isEmpty() || correoText.isEmpty() ||
                    contrasenaText.isEmpty() || estaturaText.isEmpty() || pesoText.isEmpty() ||
                    fechaNacimientoText.isEmpty() || selectedPosition == AdapterView.INVALID_POSITION) {

                setErrorIfEmpty(nombre, "Por favor, ingresa tu(s) nombre(s)");
                setErrorIfEmpty(apellido_paterno, "Por favor, ingresa tu apellido paterno");
                setErrorIfEmpty(apellido_materno, "Por favor, ingresa tu apellido materno");
                setErrorIfEmpty(nombre_usuario, "Por favor, ingresa tu nombre de usuario");
                if (!isValidEmail(correoText)) {
                    correo.setError("Por favor, ingresa un correo electrónico válido");
                }
                setErrorIfEmpty(contrasena, "Por favor, ingresa tu contraseña");
                setErrorIfEmpty(estatura, "Por favor, ingresa tu estatura");
                setErrorIfEmpty(peso, "Por favor, ingresa tu peso");
                setErrorIfEmpty(fecha_nacimiento, "Por favor, ingresa tu fecha de nacimiento");
                if (selectedPosition == AdapterView.INVALID_POSITION) {
                    Toast.makeText(registrarUsuarioActivity.this, "Por favor, selecciona un valor del Spinner", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("RegistrarUsuario", "Campos validados correctamente, redirigiendo a RegistrarObjetivos...");
                Intent intent = new Intent(registrarUsuarioActivity.this, RegistrarObjetivos.class);
                intent.putExtra("nombre", nombreText);
                intent.putExtra("apellido_paterno", apellidoPaternoText);
                intent.putExtra("apellido_materno", apellidoMaternoText);
                intent.putExtra("nombre_usuario", nombreUsuarioText);
                intent.putExtra("correo", correoText);
                intent.putExtra("contrasena", contrasenaText);
                intent.putExtra("estatura", estaturaText);
                intent.putExtra("peso", pesoText);
                intent.putExtra("fecha_nacimiento", fechaNacimientoText);
                intent.putExtra("sexo", selectedPosition);
                startActivity(intent);
            }
        });

        openGalleryButton.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
        });
    }

    private String getTextFromField(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }

    private void setErrorIfEmpty(TextInputEditText field, String errorMessage) {
        if (getTextFromField(field).isEmpty()) {
            field.setError(errorMessage);
        }
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

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
