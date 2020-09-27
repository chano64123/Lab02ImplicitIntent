package com.example.lab02implicitintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class MainActivity extends AppCompatActivity {

    public static String NOMBRES="NOMBRES";
    public static String DIRECCION="DIRECCION";
    public static String CELULAR="CELULAR";
    public static String CORREO="CORREO";
    public static String GITHUB="GITHUB";
    public static String IMAGEN="IMAGEN";
    public static final int CAMERA_PIC_REQUEST=1;
    final private int REQUEST_CODE_ASK_PERMISSION=111;
    Bitmap imagen;
    ImageView ivFoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Iniciando variables
        Button btnFoto = (Button)findViewById(R.id.btnTomarFoto);
        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        final EditText edtNombres =(EditText)findViewById(R.id.edtNombres);
        final EditText edtDireccion =(EditText)findViewById(R.id.edtDireccion);
        final EditText edtCelular =(EditText)findViewById(R.id.edtCelular);
        final EditText edtCorreo =(EditText)findViewById(R.id.edtCorreo);
        final EditText edtGitHub =(EditText)findViewById(R.id.edtGitHub);
        final AwesomeValidation validacion = new AwesomeValidation(ValidationStyle.COLORATION);

        ivFoto= (ImageView)findViewById(R.id.ivFoto);

        //Validaciones
        validacion.addValidation(this,R.id.edtNombres, RegexTemplate.NOT_EMPTY,R.string.nombreInvalido);
        validacion.addValidation(this,R.id.edtDireccion, RegexTemplate.NOT_EMPTY,R.string.direccionInvalido);
        validacion.addValidation(this,R.id.edtCelular, RegexTemplate.TELEPHONE,R.string.numeroInvalido);
        validacion.addValidation(this,R.id.edtCelular, RegexTemplate.NOT_EMPTY,R.string.numeroInvalido);
        validacion.addValidation(this,R.id.edtCorreo, Patterns.EMAIL_ADDRESS,R.string.correoInvalido);
        validacion.addValidation(this,R.id.edtGitHub, RegexTemplate.NOT_EMPTY,R.string.perfilInvalido);

        //para recargar la imagen
        if (ImagenHelp.imagen!=null){
            imagen=ImagenHelp.imagen;
            ivFoto.setImageBitmap(ImagenHelp.imagen);
        }

        //Boton para guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (validacion.validate()){
                        //limpando variable que guarda la imagen
                        ImagenHelp.imagen = null;
                        // variables
                        String nombres = edtNombres.getText().toString();
                        String direccion = edtDireccion.getText().toString();
                        String celular = edtCelular.getText().toString();
                        String correo = edtCorreo.getText().toString();
                        String gitHub = edtGitHub.getText().toString();
                        //intent
                        Intent intent = new Intent(getApplicationContext(), ActivityResultado.class);
                        intent.putExtra(NOMBRES,nombres);
                        intent.putExtra(DIRECCION,direccion);
                        intent.putExtra(CELULAR,celular);
                        intent.putExtra(CORREO,correo);
                        intent.putExtra(GITHUB,gitHub);
                        intent.putExtra(IMAGEN,imagen);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Boton para tomar fotos
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,CAMERA_PIC_REQUEST);
                }else{
                    Toast.makeText(MainActivity.this,R.string.sinPermisoCamara, Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        requestPermissions(new String[] {Manifest.permission.CAMERA},REQUEST_CODE_ASK_PERMISSION);
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int ResultCode, Intent data){
        super.onActivityResult(requestCode,requestCode,data);
        if (requestCode == CAMERA_PIC_REQUEST){
            if(ResultCode == RESULT_OK){
                imagen = (Bitmap)data.getExtras().get("data");
                ImagenHelp.imagen=imagen;
                ivFoto.setImageBitmap(imagen);
            }
        }
    }
}