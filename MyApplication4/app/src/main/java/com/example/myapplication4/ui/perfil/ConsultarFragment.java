package com.example.myapplication4.ui.perfil;

<<<<<<< Updated upstream
import android.app.ProgressDialog;
import android.content.Intent;
=======
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
>>>>>>> Stashed changes
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import androidx.fragment.app.Fragment;

import com.example.myapplication4.R;
import com.example.myapplication4.modificarPerfil;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.daos.UsuarioDAO;

import android.content.Intent;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import imageService.ImageService;
import imageService.ProfileImageServiceGrpc;
import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import imageService.ImageService;
import imageService.ProfileImageServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ConsultarFragment extends Fragment {

    private TextView nombreUsuarioInfoTextView;
    private TextView nombreInfoTextView;
    private TextView correoInfoTextView;
    private TextView apellido_paternoTextView;
    private TextView apellido_maternoTextView;
    private ImageView imageView;
    private Button btnModificarPerfil;

    private TextView caloriasInfoTextView;
    private TextView carbohidratosInfoTextView;
    private TextView grasasInfoTextView;
    private TextView proteinasInfoTextView;
<<<<<<< Updated upstream
    private ProgressDialog progressDialog;
    private Button btnModificarPerfil;
=======

    private ProgressDialog progressDialog;

>>>>>>> Stashed changes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        nombreUsuarioInfoTextView = rootView.findViewById(R.id.nombre_usuarioInfoTextView);
        nombreInfoTextView = rootView.findViewById(R.id.nombreInfoTextView);
        correoInfoTextView = rootView.findViewById(R.id.correoInfoTextView);
        apellido_paternoTextView = rootView.findViewById(R.id.apellido_paternoTextView);
        apellido_maternoTextView = rootView.findViewById(R.id.apellido_maternoTextView);
        imageView = rootView.findViewById(R.id.imageView);
        btnModificarPerfil = rootView.findViewById(R.id.btnModificarPerfil);

        caloriasInfoTextView = rootView.findViewById(R.id.caloriasInfoTextView);
        carbohidratosInfoTextView = rootView.findViewById(R.id.carbohidratosInfoTextView);
        grasasInfoTextView = rootView.findViewById(R.id.grasasInfoTextView);
        proteinasInfoTextView = rootView.findViewById(R.id.proteinasInfoTextView);

        Log.d("OnCreateView", "Vistas inicializadas correctamente");

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Cargando datos...");
        progressDialog.setCancelable(false);
        progressDialog.show();

<<<<<<< Updated upstream
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando..."); // Mensaje de carga
        progressDialog.setCancelable(false);

        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String nombre = nombreInfoTextView.getText().toString();
                String nombreUsuario = nombreUsuarioInfoTextView.getText().toString();
                String correo = correoInfoTextView.getText().toString();
                String apellidoPaterno = apellido_paternoTextView.getText().toString();
                String apellidoMaterno = apellido_maternoTextView.getText().toString();
=======
        new Thread(() -> {
            try {

                HashMap<String, Object> resultado = UsuarioDAO.consultarUsuario(Constantes.NOMBRE_USUARIO);
                boolean error = (boolean) resultado.get("error");
>>>>>>> Stashed changes

                if (!error) {
                    Usuario usuario = (Usuario) resultado.get("objeto");

                    requireActivity().runOnUiThread(() -> {
                        nombreInfoTextView.setText(usuario.getNombre());
                        nombreUsuarioInfoTextView.setText(usuario.getNombre_usuario());
                        apellido_paternoTextView.setText(usuario.getApellido_paterno());
                        apellido_maternoTextView.setText(usuario.getApellido_materno());
                        correoInfoTextView.setText(usuario.getCorreo());
                        caloriasInfoTextView.setText(String.valueOf(usuario.getCalorias()));
                        carbohidratosInfoTextView.setText(String.valueOf(usuario.getCarbohidratos()));
                        grasasInfoTextView.setText(String.valueOf(usuario.getGrasas()));
                        proteinasInfoTextView.setText(String.valueOf(usuario.getProteinas()));
                        Log.d("OnCreateView", "Información de usuario actualizada");

<<<<<<< Updated upstream

                startActivity(intent);
                progressDialog.dismiss();
=======
                        // Cargar foto del usuario
                        cargarFotoDesdeGrpc();
                    });
                } else {
                    String mensaje = (String) resultado.get("mensaje");
                    requireActivity().runOnUiThread(() -> {
                        progressDialog.dismiss(); // Asegúrate de cerrar el ProgressDialog en caso de error
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage("No se pudo conectar con el servidor.\nDetalles: " + mensaje)
                                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                                .show();
                    });
                }
            } catch (Exception e) {
                Log.e("OnCreateView", "Error al consultar información del usuario", e);
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), "Error al cargar información del usuario. Por favor, inténtelo más tarde.", Toast.LENGTH_SHORT).show();
                });
            } finally {
                // Ocultar ProgressDialog en el hilo principal
                requireActivity().runOnUiThread(() -> progressDialog.dismiss());
>>>>>>> Stashed changes
            }
        }).start();


        btnModificarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), modificarPerfil.class);
            startActivity(intent);
        });
        return rootView;
    }

    private void cargarFotoDesdeGrpc() {
        progressDialog.setMessage("Cargando imagen...");
        progressDialog.show();

        new Thread(() -> {
            ManagedChannel channel = null;
            try {
                Log.d("CargarFoto", "Iniciando canal gRPC");
                channel = ManagedChannelBuilder
                        .forAddress(Constantes.IP, Constantes.PUERTO_API_GRPC_DOCKER)
                        .usePlaintext()
                        .build();

                Log.d("CargarFoto", "Canal gRPC iniciado");
                ProfileImageServiceGrpc.ProfileImageServiceBlockingStub stub = ProfileImageServiceGrpc.newBlockingStub(channel);

                Log.d("CargarFoto", "Stub gRPC creado");
                ImageService.DownloadImageRequest solicitud = ImageService.DownloadImageRequest.newBuilder()
                        .setName(Constantes.FOTO)
                        .build();

                Log.d("CargarFoto", "Haciendo solicitud gRPC para " + Constantes.FOTO);
                ImageService.DownloadImageResponse respuesta = stub.downloadProfileImage(solicitud);
                Log.d("CargarFoto", "Respuesta gRPC recibida");

                String imagenBase64 = respuesta.getImageData();
                byte[] decodedString = Base64.decode(imagenBase64, Base64.DEFAULT);
                Bitmap foto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (foto != null) {
                    Log.d("CargarFoto", "Imagen decodificada correctamente");
                    requireActivity().runOnUiThread(() -> {
                        imageView.setImageBitmap(foto);
                        progressDialog.dismiss();
                    });
                } else {
                    throw new Exception("Error al decodificar la imagen");
                }
            } catch (Exception e) {
                Log.e("CargarFoto", "Error al recuperar la foto", e);
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), "Error al recuperar la foto", Toast.LENGTH_SHORT).show();
                });
            } finally {
                if (channel != null) {
                    channel.shutdown();
                }
            }
        }).start();
    }
}
