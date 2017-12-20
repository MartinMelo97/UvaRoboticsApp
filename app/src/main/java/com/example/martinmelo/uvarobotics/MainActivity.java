package com.example.martinmelo.uvarobotics;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Button BtnActivar, BtnPanico, BtnUbicacion;
    private static String cellphone_number="";
    private static com.github.clans.fab.FloatingActionMenu menu;
    private static com.github.clans.fab.FloatingActionButton fabChangeNumber;
    private static final int CODE_REQUESTS = 1;
    private static LocationManager locationManager;
    private static String latitud, longitud;
    private static int clickPanico = 0, clicksAlerta = 0, clicksQuien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkIfNumberInDatabase();

        Log.d("A ver",cellphone_number);

        if(cellphone_number.length() > 9)
        {
            Log.d("Frepo","Entro");
            checkPermisos();
        }

        //Instanciamos botones
        BtnActivar = (Button) findViewById(R.id.BtnActivar);
        BtnPanico = (Button) findViewById(R.id.BtnPanico);
        BtnUbicacion = (Button) findViewById(R.id.BtnUbicacion);
        menu = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.menu);
        fabChangeNumber = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item);

        BtnPanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerUbicacion(true, true, "PANICO");
                clickPanico += 1;
                checkPanico();
            }
        });

        BtnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerUbicacion(false, true, "ALERTA");
                clicksAlerta += 1;
               checkAlerta();
            }
        });

        BtnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUbicacion();
                clicksQuien += 1;
                checkQuien();
            }
        });

        fabChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intento = new Intent(getApplicationContext(), ChangeNumber.class);
                startActivity(intento);
            }
        });


    }
    public void checkIfNumberInDatabase()
    {
        final DataBaseManager managerRead = new DataBaseManager(MainActivity.this, "read");
        Cursor number = managerRead.lookingForNumber();
        if(number.moveToFirst())
        {
            Log.d("llego perro","llego perro");
            cellphone_number = number.getString(1);
        }
        else
        {
            Intent intento = new Intent(getApplicationContext(), SetNumberActivity.class);
            startActivity(intento);
        }

    }
    public void checkPermisos()
    {
        int permissionSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        List<String> permissionsRequests = new ArrayList<>();
        if(permissionSMS != PackageManager.PERMISSION_GRANTED)
        {
            permissionsRequests.add(Manifest.permission.SEND_SMS);
        }

        if(permissionCall != PackageManager.PERMISSION_GRANTED)
        {
            permissionsRequests.add(Manifest.permission.CALL_PHONE);
        }

        if(permissionLocation != PackageManager.PERMISSION_GRANTED)
        {
            permissionsRequests.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!permissionsRequests.isEmpty())
        {
            ActivityCompat.requestPermissions(this, permissionsRequests.toArray(new String[permissionsRequests.size()]), CODE_REQUESTS);
        }
    }

    public void mandarUbicacionSMS(String latitud, String longitud, String concat)
    {
        //Toast.makeText(this, " ("+concat+" "+latitud+", "+longitud+")", Toast.LENGTH_SHORT).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(cellphone_number, null," ("+concat+" "+latitud+", "+longitud+")", null, null);
        Toast.makeText(this, "Mensaje enviado exitosamente", Toast.LENGTH_SHORT).show();
    }

    public void obtenerUbicacion(boolean llamar, boolean mensaje, String paraConcatenar)
    {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            alertNoGPSOn();
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
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

            if(mensaje == true)
            {
                mandarUbicacionSMS(latitud, longitud, paraConcatenar);
            }

        }

    }

    protected  void getUbicacion()
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(cellphone_number, null, "(QUIEN)", null, null);
        Toast.makeText(this, "Mensaje enviado exitosamente", Toast.LENGTH_SHORT).show();
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

    public void checkPanico()
    {
        if(clickPanico >= 2)
        {
            BtnPanico.setEnabled(false);
            BtnPanico.setBackgroundColor(getResources().getColor(R.color.disabledd));
            CountDownTimer buttonTimer = new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long l) {
                    BtnPanico.setText(""+l/1000);
                }

                @Override
                public void onFinish() {
                    BtnPanico.setText(R.string.PanicoButtonText);
                    BtnPanico.setEnabled(true);
                    BtnPanico.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }.start();
            clickPanico = 0;
        }
    }

    public void checkAlerta()
    {
        if(clicksAlerta >= 2)
        {
            BtnActivar.setEnabled(false);
            BtnActivar.setBackgroundColor(getResources().getColor(R.color.disabledd));
            CountDownTimer buttonTimer = new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long l) {
                    BtnActivar.setText(""+l/1000);
                }

                @Override
                public void onFinish() {
                    BtnActivar.setText(R.string.AlertaButtonText);
                    BtnActivar.setEnabled(true);
                    BtnActivar.setBackgroundColor(getResources().getColor(R.color.colorActivate));
                }
            }.start();
            clicksAlerta = 0;
        }
    }

    public void checkQuien()
    {
        if(clicksQuien >= 2)
        {
            BtnUbicacion.setEnabled(false);
            BtnUbicacion.setBackgroundColor(getResources().getColor(R.color.disabledd));
            CountDownTimer buttonTimer = new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long l) {
                    BtnUbicacion.setText(""+l/1000);
                }

                @Override
                public void onFinish() {
                    BtnUbicacion.setText(R.string.QuienButtonText);
                    BtnUbicacion.setEnabled(true);
                    BtnUbicacion.setBackgroundColor(getResources().getColor(R.color.colorUbicacion));
                }
            }.start();
            clicksQuien = 0;
        }
    }
}
