package com.example.martinmelo.uvarobotics;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetNumberActivity extends AppCompatActivity {
    Button BtnSave;
    EditText ETTNumber;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_number);

        BtnSave = (Button) findViewById(R.id.BtnSaveNumber);

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETTNumber = (EditText) findViewById(R.id.TxtNumberAlarm);
                number = ETTNumber.getText().toString();
                if(number.length() < 13 && number.length() > 0)
                {
                    final DataBaseManager manager = new DataBaseManager(getApplicationContext(), "write");
                    DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
                    ContentValues valores = helper.generarContentValuesNumbers(number);
                    manager.insertNumber(valores, "numbers");

                }
                else
                {
                    Toast.makeText(SetNumberActivity.this, "Ingresa un numero correcto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
