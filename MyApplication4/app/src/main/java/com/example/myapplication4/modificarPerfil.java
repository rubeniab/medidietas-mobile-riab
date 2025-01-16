package com.example.myapplication4;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication4.ui.Utilidades.ApiService;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.Utilidades.GestorToken;
import com.example.myapplication4.ui.modelos.UsuarioMovil;
import com.example.myapplication4.ui.perfil.ConsultarFragment;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.example.myapplication4.ui.perfil.Usuario;
import com.google.gson.JsonObject;

import java.util.HashMap;

import imageService.ImageService;
import imageService.ProfileImageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modificarPerfil extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editNombre, editApellidoPaterno, editApellidoMaterno, editNombreUsuario, editCorreo, editCalorias, editCarbohidratos, editGrasas, editProteinas;
    private ImageView imageView;
    private Button btnActualizar, btnCancelar, btnSeleccionarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        editNombre = findViewById(R.id.editNombre);
        editApellidoPaterno = findViewById(R.id.editApellidoPaterno);
        editApellidoMaterno = findViewById(R.id.editApellidoMaterno);
        editCorreo = findViewById(R.id.editCorreo);
        editNombreUsuario = findViewById(R.id.editNombreUsuario);
        editCalorias = findViewById(R.id.calorias);
        editCarbohidratos = findViewById(R.id.carbohidratos);
        editGrasas = findViewById(R.id.grasas);
        editProteinas = findViewById(R.id.proteinas);
        imageView = findViewById(R.id.imageView);
        Context context = null;

        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelarModificacion);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        consultarUsuario();

        cargarFotoDesdeGrpc();

        btnActualizar.setOnClickListener(view -> {
            String nombre = editNombre.getText().toString().trim();
            String apellidoPaterno = editApellidoPaterno.getText().toString().trim();
            String apellidoMaterno = editApellidoMaterno.getText().toString().trim();
            String correo = editCorreo.getText().toString().trim();
            String nombreUsuario = editNombreUsuario.getText().toString().trim();

            Log.d("btnActualizar", "Nombre: " + nombre);
            Log.d("btnActualizar", "Apellido Paterno: " + apellidoPaterno);
            Log.d("btnActualizar", "Apellido Materno: " + apellidoMaterno);
            Log.d("btnActualizar", "Correo: " + correo);
            Log.d("btnActualizar", "Nombre Usuario: " + nombreUsuario);

            if (!validarCampos(nombre, apellidoPaterno, apellidoMaterno, correo, nombreUsuario)) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Actualizando perfil...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        new Thread(() -> {
            HashMap<String, Object> respuesta = UsuarioDAO.modificarPerfil(context, nombreUsuario, nombre, apellidoPaterno, apellidoMaterno, correo);
            Log.d("btnActualizar", "Respuesta: " + respuesta.toString());
            runOnUiThread(() -> {
                progressDialog.dismiss();
                if((boolean) respuesta.get("error")){
                    Toast.makeText(this, "Error al modiciar el perfil: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Perfil modificado correctamente: " + respuesta.get("mensaje"), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    });

        btnCancelar.setOnClickListener(view -> {
            Toast.makeText(this, "Cambios cancelados", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnSeleccionarFoto.setOnClickListener(v -> abrirGaleria());
    }

    private void consultarUsuario() {
        new Thread(() -> {
            HashMap<String, Object> resultado = UsuarioDAO.consultarUsuario(Constantes.NOMBRE_USUARIO);
            boolean error = (boolean) resultado.get("error");
            if (!error) {
                Usuario usuario = (Usuario) resultado.get("objeto");

                runOnUiThread(() -> {
                    editNombre.setText(usuario.getNombre());
                    editApellidoPaterno.setText(usuario.getApellido_paterno());
                    editApellidoMaterno.setText(usuario.getApellido_materno());
                    editCorreo.setText(usuario.getCorreo());
                    editNombreUsuario.setText(usuario.getNombre_usuario());
                    editCalorias.setText(String.valueOf(usuario.getCalorias()));
                    editCarbohidratos.setText(String.valueOf(usuario.getCarbohidratos()));
                    editGrasas.setText(String.valueOf(usuario.getGrasas()));
                    editProteinas.setText(String.valueOf(usuario.getProteinas()));
                    Log.d("consultarUsuario", "Información de usuario cargada");

                });
            } else {
                String mensaje = (String) resultado.get("mensaje");
                runOnUiThread(() -> {
                    ProgressDialog progressDialog = new ProgressDialog(modificarPerfil.this);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                });
            } }).start();


    }

    private boolean validarCampos(String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String nombreUsuario) {
        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || correo.isEmpty() || nombreUsuario.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        } return true;
    }

    private void cargarFotoDesdeGrpc( ) {
        new Thread(() -> {
            ManagedChannel channel = null;
            try {
                Log.d("CargarFoto", "Iniciando canal gRPC");
                // Construir el canal de comunicación con el servidor gRPC
                channel = ManagedChannelBuilder
                        .forAddress(Constantes.IP, Constantes.PUERTO_API_GRPC_DOCKER)
                        .usePlaintext()
                        .build();

                Log.d("CargarFoto", "Canal gRPC iniciado");
                ProfileImageServiceGrpc.ProfileImageServiceBlockingStub stub = ProfileImageServiceGrpc.newBlockingStub(channel);

                Log.d("CargarFoto", "Stub gRPC creado");
                // Construir la solicitud para obtener la imagen de perfil
                ImageService.DownloadImageRequest solicitud = ImageService.DownloadImageRequest.newBuilder()
                        .setName(Constantes.FOTO)
                        .build();

                Log.d("CargarFoto", "Haciendo solicitud gRPC para " + Constantes.FOTO);
                // Hacer la solicitud y obtener la respuesta
                ImageService.DownloadImageResponse respuesta = stub.downloadProfileImage(solicitud);
                Log.d("CargarFoto", "Respuesta gRPC recibida");
                String imagenBase64 = respuesta.getImageData();

                Log.d("CargarFoto", "Respuesta recibida. Tamaño de la imagen Base64: " + imagenBase64.length());

                // Decodificar la imagen en formato Base64 a Bitmap
                byte[] decodedString = Base64.decode(imagenBase64, Base64.DEFAULT);
                Bitmap foto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // Actualizar la UI con la imagen obtenida
                if (foto != null) {
                    Log.d("CargarFoto", "Imagen decodificada correctamente");
                    runOnUiThread(() -> imageView.setImageBitmap(foto));
                } else {
                    Log.e("CargarFoto", "Error al decodificar la imagen");
                    Toast.makeText(modificarPerfil.this, "Error al recuperar la foto", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("CargarFoto", "Error al recuperar la foto", e);
                Toast.makeText(modificarPerfil.this, "Error al recuperar la foto", Toast.LENGTH_SHORT).show();
                Log.e("ErrorCargarFoto", e.getMessage(), e);
            } finally {
                if (channel != null) {
                    channel.shutdown();
                }
            }
        }).start();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                // Obtener la URI de la imagen seleccionada
                Uri imagenUri = data.getData();
                // Decodificar y mostrar en el ImageView
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imagenUri));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                Log.e("Galeria", "Error al cargar imagen", e);
            }
        }
    }
}
