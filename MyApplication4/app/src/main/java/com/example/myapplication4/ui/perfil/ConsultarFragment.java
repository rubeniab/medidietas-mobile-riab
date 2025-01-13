package com.example.myapplication4.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication4.LoginActivity;
import com.example.myapplication4.R;
import com.example.myapplication4.modificarPerfil;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConsultarFragment extends Fragment {

    private TextView nombreUsuarioInfoTextView;
    private TextView nombreInfoTextView;
    private TextView correoInfoTextView;
    private TextView apellido_paternoTextView;
    private TextView apellido_maternoTextView;
    private TextView caloriasInfoTextView;
    private TextView carbohidratosInfoTextView;
    private TextView grasasInfoTextView;
    private TextView proteinasInfoTextView;
    private Button btnModificarPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        nombreUsuarioInfoTextView = rootView.findViewById(R.id.nombre_usuarioInfoTextView);
        nombreInfoTextView = rootView.findViewById(R.id.nombreInfoTextView);
        correoInfoTextView = rootView.findViewById(R.id.correoInfoTextView);
        apellido_paternoTextView = rootView.findViewById(R.id.apellido_paternoTextView);
        apellido_maternoTextView = rootView.findViewById(R.id.apellido_maternoTextView);
        //caloriasInfoTextView = rootView.findViewById(R.id.caloriasInfoTextView);
        //carbohidratosInfoTextView = rootView.findViewById(R.id.carbohidratosInfoTextView);
        //grasasInfoTextView = rootView.findViewById(R.id.grasasInfoTextView);
        //proteinasInfoTextView = rootView.findViewById(R.id.proteinasInfoTextView);
        btnModificarPerfil = rootView.findViewById(R.id.btnModificarPerfil);

        String nombre = Constantes.NOMBRE;
        String nombreUsuario = Constantes.NOMBRE_USUARIO;
        String correo = Constantes.CORREO;
        String apellido_paterno = Constantes.APELLIDO_PATERNO;
        String apellido_materno = Constantes.APELLIDO_MATERNO;

        Double calorias = Constantes.CALORIAS;
        Double carbohidratos = Constantes.CARBOHIDRATOS;
        Double grasas = Constantes.GRASAS;
        Double proteinas = Constantes.PROTEINAS;

        nombreInfoTextView.setText(nombre);
        nombreUsuarioInfoTextView.setText(nombreUsuario);
        apellido_paternoTextView.setText(apellido_paterno);
        apellido_maternoTextView.setText(apellido_materno);
        correoInfoTextView.setText(correo);
        //caloriasInfoTextView.setText(String.valueOf(calorias));
        //carbohidratosInfoTextView.setText(String.valueOf(carbohidratos));
        //grasasInfoTextView.setText(String.valueOf(grasas));
        //proteinasInfoTextView.setText(String.valueOf(proteinas));

        btnModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = nombreInfoTextView.getText().toString();
                String nombreUsuario = nombreUsuarioInfoTextView.getText().toString();
                String correo = correoInfoTextView.getText().toString();
                String apellidoPaterno = apellido_paternoTextView.getText().toString();
                String apellidoMaterno = apellido_maternoTextView.getText().toString();

                Intent intent = new Intent(getActivity(), modificarPerfil.class);

                intent.putExtra("nombre", nombre);
                intent.putExtra("nombreUsuario", nombreUsuario);
                intent.putExtra("correo", correo);
                intent.putExtra("apellido_paterno", apellidoPaterno);
                intent.putExtra("apellido_materno", apellidoMaterno);


                startActivity(intent);
            }
        });

        return rootView;
    }

}
