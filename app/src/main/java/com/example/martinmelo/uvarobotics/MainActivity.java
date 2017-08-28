package com.example.martinmelo.uvarobotics;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static Button BtnActivar, BtnPanico, BtnUbicacion;
    private static String cellphone_number="7226034510";

    //Codigo de permiso
    private static final int REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_SEND_SMS = 0;
    private static final int REQUEST_LOCATION = 1;
    private static LocationManager locationManager;
    private static String latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciamos botones
        BtnActivar = (Button) findViewById(R.id.BtnActivar);
        BtnPanico = (Button) findViewById(R.id.BtnPanico);
        BtnUbicacion = (Button) findViewById(R.id.BtnUbicacion);

        requestPermission(Manifest.permission.SEND_SMS, REQUEST_SEND_SMS);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);


        BtnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerUbicacion(true, true, "");
            }
        });

        BtnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerUbicacion(false, true, "(ACTIVAR)");
            }
        });

        BtnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUbicacion();
            }
        });

    }

    public void llamar()
    {
        requestPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PHONE);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+cellphone_number));
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        startActivity(callIntent);
    }

    public void mandarUbicacionSMS(String latitud, String longitud, String concat)
    {
        //Toast.makeText(this, concat+" (UBICACION "+latitud+", "+longitud+")", Toast.LENGTH_SHORT).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(cellphone_number, null, concat+" (UBICACION "+latitud+", "+longitud+")", null, null);
    }

    public void obtenerUbicacion(boolean llamar, boolean mensaje, String paraConcatenar)
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            alertNoGPSOn();
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            }
            else
            {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null)
                {
                    double latti = location.getLatitude();
                    double longi = location.getLongitude();
                    latitud = String.valueOf(latti);
                    longitud = String.valueOf(longi);
                }
                else
                {
                    Toast.makeText(this, "No se puedo trackear tu ubicacion", Toast.LENGTH_SHORT).show();
                }
            }

            if(llamar == true)
            {
                llamar();
            }

            if(mensaje == true)
            {
                mandarUbicacionSMS(latitud, longitud, paraConcatenar);
            }

        }
    }

    protected  void getUbicacion()
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(cellphone_number, null, "(EXTRAER_UBICACION)", null, null);
    }

    protected void alertNoGPSOn()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ppor favor ENCIENDE tu Ubicacion")
                .setCancelable(false)
                .setPositiveButton("Encender", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Ignorar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    protected void requestPermission(String permissionType, int requestCode)
    {
        int permissionint = ContextCompat.checkSelfPermission(this, permissionType);

        if(permissionint != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CALL_PHONE:

                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Se necesita el permiso de llamada", Toast.LENGTH_SHORT).show();
                }

            break;

            case REQUEST_SEND_SMS:

                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Se necesita el permiso de SMS", Toast.LENGTH_SHORT).show();
                }
            break;

        }
    }
}
