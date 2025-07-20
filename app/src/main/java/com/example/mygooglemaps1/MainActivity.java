package com.example.mygooglemaps1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap myMap;
    private Map<String, List<String>> productosPorLugar = new HashMap<>();
    private Map<String, LatLng> ubicacionesTiendas = new HashMap<>();
    private Location ubicacionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisosDeUbicacion();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

// Productos por tienda
        productosPorLugar.put("CLINOFOTEC", Arrays.asList("Teclados", "Mouses", "Monitores"));
        productosPorLugar.put("CENTRO DE COMPUTO SEGURA", Arrays.asList("Audífonos", "Memoria RAM", "CPU"));
        productosPorLugar.put("COMPUSOL", Arrays.asList("Impresoras", "Tinta de impresora", "Tarjeta de video"));
        productosPorLugar.put("COMPUTER HOUSE", Arrays.asList("Laptops", "Parlantes", "Teclados Gamer"));
        productosPorLugar.put("COMPUSALLY", Arrays.asList("Monitores", "Sillas Gamer", "Webcams"));
        productosPorLugar.put("COMPUPLUS", Arrays.asList("Discos Duros", "SSD", "Placas Madre"));
        productosPorLugar.put("TECH PERU", Arrays.asList("Memoria RAM", "Teclados Mecánicos", "Cables HDMI"));
        productosPorLugar.put("MICROTECHSERVICE", Arrays.asList("Tarjetas Gráficas", "Fuentes de Poder", "Coolers"));
        productosPorLugar.put("COMPUSOLUCION", Arrays.asList("Tablets", "Pantallas", "Cargadores"));
        productosPorLugar.put("HARWARE Y SOFTWARE", Arrays.asList("Licencias de Software", "Antivirus", "Instalación de SO"));
        productosPorLugar.put("HACKERTECH", Arrays.asList("Componentes para PC", "Herramientas Técnicas", "Cables"));
        productosPorLugar.put("ULTRACOM", Arrays.asList("Routers", "Switches", "Cámaras de seguridad"));

// Coordenadas por tienda
        ubicacionesTiendas.put("CLINOFOTEC", new LatLng(-9.128089, -78.517661));
        ubicacionesTiendas.put("CENTRO DE COMPUTO SEGURA", new LatLng(-9.117447, -78.515306));
        ubicacionesTiendas.put("COMPUSOL", new LatLng(-9.072978, -78.592803));
        ubicacionesTiendas.put("COMPUTER HOUSE", new LatLng(-9.076061, -78.592222));
        ubicacionesTiendas.put("COMPUSALLY", new LatLng(-9.075525, -78.591964));
        ubicacionesTiendas.put("COMPUPLUS", new LatLng(-9.072939, -78.593047));
        ubicacionesTiendas.put("TECH PERU", new LatLng(-9.072900, -78.593031));
        ubicacionesTiendas.put("MICROTECHSERVICE", new LatLng(-9.072875, -78.593319));
        ubicacionesTiendas.put("COMPUSOLUCION", new LatLng(-9.072675, -78.593161));
        ubicacionesTiendas.put("HARWARE Y SOFTWARE", new LatLng(-9.072867, -78.593259));
        ubicacionesTiendas.put("HACKERTECH", new LatLng(-9.121700, -78.517500));
        ubicacionesTiendas.put("ULTRACOM", new LatLng(-9.125036, -78.528056));


        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarProducto(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void solicitarPermisosDeUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        }

        // Mover botón de ubicación del mapa
        View locationButton = ((View) findViewById(R.id.map)
                .getParent())
                .findViewById(Integer.parseInt("2"));

        if (locationButton != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) locationButton.getLayoutParams();
            params.setMargins(0, 180, 30, 0);
            locationButton.setLayoutParams(params);
        }

        for (Map.Entry<String, LatLng> entry : ubicacionesTiendas.entrySet()) {
            myMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey()));
        }

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionesTiendas.get("COMPUSOL"), 15));

        myMap.setOnMyLocationChangeListener(location -> ubicacionActual = location);

        myMap.setOnMarkerClickListener(marker -> {
            String lugar = marker.getTitle();

            if (productosPorLugar.containsKey(lugar)) {
                List<String> productos = productosPorLugar.get(lugar);
                mostrarDialogoConProductosYDistancia(lugar, productos);
            } else {
                Toast.makeText(MainActivity.this, "No hay productos registrados para " + lugar, Toast.LENGTH_SHORT).show();
            }

            return true;
        });
    }

    private void buscarProducto(String query) {
        boolean encontrado = false;

        for (Map.Entry<String, List<String>> entry : productosPorLugar.entrySet()) {
            String tienda = entry.getKey();
            List<String> productos = entry.getValue();

            for (String producto : productos) {
                if (producto.toLowerCase().contains(query.toLowerCase())) {
                    moverCamaraATienda(tienda);
                    mostrarDialogoConProductosYDistancia(tienda, productos);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) break;
        }

        if (!encontrado) {
            Toast.makeText(this, "Producto no encontrado en ninguna tienda", Toast.LENGTH_SHORT).show();
        }
    }

    private void moverCamaraATienda(String tienda) {
        LatLng ubicacion = ubicacionesTiendas.get(tienda);
        if (ubicacion != null && myMap != null) {
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16));
        }
    }

    private void mostrarDialogoConProductosYDistancia(String tienda, List<String> productos) {
        LatLng ubicacionTienda = ubicacionesTiendas.get(tienda);
        float distancia = 0;

        if (ubicacionActual != null && ubicacionTienda != null) {
            Location locTienda = new Location("");
            locTienda.setLatitude(ubicacionTienda.latitude);
            locTienda.setLongitude(ubicacionTienda.longitude);
            distancia = ubicacionActual.distanceTo(locTienda) / 1000f; // km
        }

        String[] productosArray = productos.toArray(new String[0]);

        String mensaje = "Productos en " + tienda + ":\n\n";
        for (String producto : productosArray) {
            mensaje += "- " + producto + "\n";
        }

        if (distancia > 0) {
            mensaje += String.format("\nDistancia aproximada: %.2f km", distancia);
        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Información de tienda")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", (dialog, which) -> {
                    Marker marker = myMap.addMarker(new MarkerOptions()
                            .position(ubicacionesTiendas.get(tienda))
                            .title(tienda));
                    if (marker != null) marker.showInfoWindow();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            } else {
                Toast.makeText(this, "Permisos de ubicación denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}