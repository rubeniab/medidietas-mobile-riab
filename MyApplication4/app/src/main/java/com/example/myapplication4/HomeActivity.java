package com.example.myapplication4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication4.databinding.ActivityHomeBinding;
import com.example.myapplication4.ui.Utilidades.Constantes;
import com.example.myapplication4.ui.daos.UsuarioDAO;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_consPerfil, R.id.nav_registrarConsumo, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Actualizar los TextView en el encabezado del menú
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
        TextView navHeaderSubtitle = headerView.findViewById(R.id.nav_header_subtitle);
        ImageView fotoPerfilView = headerView.findViewById(R.id.imageView);
        fotoPerfilView.setImageBitmap(cargarFotoPerfil());
        navHeaderTitle.setText(Constantes.NOMBRE_USUARIO);
        navHeaderSubtitle.setText(Constantes.CORREO);

        // Manejar la selección del menú
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    logout();
                    drawer.closeDrawers(); // Cerrar el DrawerLayout
                    return true;
                }
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawers(); // Cerrar el DrawerLayout
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Método para realizar el logout
    private void logout() {
        // Limpiar los datos de sesión (por ejemplo, usando SharedPreferences)
        clearSessionData();

        // Redirigir al LoginActivity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);

        // Mostrar un mensaje de que la sesión ha sido cerrada
        Toast.makeText(HomeActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

    // Método para limpiar los datos de sesión
    private void clearSessionData() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();
    }

    private Bitmap cargarFotoPerfil() {
        Bitmap bM = null;
        try {
            String imagenString = UsuarioDAO.recuperarFotoPerfil(Constantes.FOTO);
            byte[] decodedString = Base64.decode(imagenString, Base64.DEFAULT);
            bM = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bM;
    }
}