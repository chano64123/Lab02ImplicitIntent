package com.example.lab02implicitintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActivityResultado extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSION=111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        try {
            //variables de datos a recibir
            final String nombres = getIntent().getStringExtra(MainActivity.NOMBRES);
            final String direccion = getIntent().getStringExtra(MainActivity.DIRECCION);
            final String celular = getIntent().getStringExtra(MainActivity.CELULAR);
            final String correo = getIntent().getStringExtra(MainActivity.CORREO);
            final String github = getIntent().getStringExtra(MainActivity.GITHUB);
            Bitmap imagen = getIntent().getParcelableExtra(MainActivity.IMAGEN);
            //objetos donde se visualizaran los datos recibidos
            TextView tvNombres = findViewById(R.id.tvNombres);
            TextView tvDireccion = findViewById(R.id.tvDireccion);
            TextView tvCelular = findViewById(R.id.tvCelular);
            TextView tvCorreo = findViewById(R.id.tvCorreo);
            TextView tvGitHub = findViewById(R.id.tvGitHub);
            ImageView ivFoto = findViewById(R.id.ivFoto) ;
            //estableciendo valores
            tvNombres.setText(getResources().getString(R.string.tvNombre) + " " + nombres);
            tvDireccion.setText(getResources().getString(R.string.tvDireccion) + " " + direccion);
            tvCelular.setText(getResources().getString(R.string.tvCelular) + " " + celular);
            tvCorreo.setText(getResources().getString(R.string.tvCorreo) + " " + correo);
            tvGitHub.setText(getResources().getString(R.string.tvPerfil) + " " + github);
            ivFoto.setImageBitmap(imagen);
            //Botones
            Button btnDireccion = findViewById(R.id.btnDireccion);
            Button btnCelular = findViewById(R.id.btnCelular);
            Button btnCorreo = findViewById(R.id.btnCorreo);
            Button btnPerfil = findViewById(R.id.btnPerfil);
            //Acciones de lo botones
            //para la direccion en maps
            btnDireccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String lugar = direccion.replace(" ","+");
                    Uri location = Uri.parse("geo:0.0?q="+lugar);
                    Intent locationIntent = new Intent(Intent.ACTION_VIEW,location);
                    startActivity(locationIntent);
                }
            });
            //para la llamada
            btnCelular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri number = Uri.parse("tel:"+celular);
                    Intent callIntent = new Intent(Intent.ACTION_CALL,number);
                    if (ActivityCompat.checkSelfPermission(ActivityResultado.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(callIntent);
                    }else{
                        Toast.makeText(ActivityResultado.this,R.string.sinPermisoTelefono, Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            requestPermissions(new String[] {Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_PERMISSION);
                        }
                    }
                }
            });
            //para el correo
            btnCorreo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:"+correo+"?subject="+getResources().getString(R.string.titulo)));
                    Toast.makeText(ActivityResultado.this, getResources().getString(R.string.titulo), Toast.LENGTH_SHORT).show();
                    emailIntent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.mensaje1) + " " + nombres + getResources().getString(R.string.mensaje2) + " " + celular + " " + getResources().getString(R.string.mensaje3) + "\"" + github + "\""+ getResources().getString(R.string.mensaje4));
                    startActivity(emailIntent);
                }
            });
            //para el perfil de github
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri portal = Uri.parse("https://github.com/"+github);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,portal);
                    List<ResolveInfo> activities = getPackageManager().queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    boolean isIntentSafe = activities.size()>0;
                    String titulo = getResources().getString(R.string.escoger);
                    Intent wChooser = Intent.createChooser(webIntent,titulo);
                    if (isIntentSafe){
                        startActivity(wChooser);
                    }
                }
            });
        } catch(Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}