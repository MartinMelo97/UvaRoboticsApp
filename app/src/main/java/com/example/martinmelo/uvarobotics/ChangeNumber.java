package com.example.martinmelo.uvarobotics;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeNumber extends AppCompatActivity {
    private static EditText ETCurrentNumber, ETNewNumber;
    private static Switch SWShowNumber;
    private static ImageView imageBack;
    private static Button BtnUpdateNumber;
    private static TextView TVCurrentNumber;
    private static String currentNumber, newNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);

        //Seteando por default que no se vea el numero actual

        ETCurrentNumber = (EditText) findViewById(R.id.ETActualNumber);
        TVCurrentNumber = (TextView) findViewById(R.id.TVCurrentNumber);

        ETCurrentNumber.setVisibility(View.INVISIBLE);
        TVCurrentNumber.setVisibility(View.INVISIBLE);
        SWShowNumber = (Switch) findViewById(R.id.SwShowNumber);

        imageBack = (ImageView) findViewById(R.id.BtnBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nte = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nte);
                finish();
            }
        });

        //Calling function and setting the number in the database

        checkIfNumberInDatabase();

        ETCurrentNumber.setText(currentNumber);

        SWShowNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               CheckIfIsOn();
            }
        });

        BtnUpdateNumber = (Button) findViewById(R.id.BtnUpdateNumber);
        BtnUpdateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETNewNumber = (EditText) findViewById(R.id.ETNewNumber);
                newNumber = ETNewNumber.getText().toString();
                if(newNumber.length() < 13 && newNumber.length() > 0)
                {
                    final DataBaseManager manager = new DataBaseManager(getApplicationContext(), "write");
                    DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
                    ContentValues valores = helper.generarContentValuesNumbers(newNumber);
                    manager.updateNumber(valores, "numbers");
                    Toast.makeText(ChangeNumber.this, "Numero actualizado correctamente", Toast.LENGTH_LONG).show();
                    finish();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ingresa un numero correcto", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void CheckIfIsOn()
    {
        if(SWShowNumber.isChecked())
        {
            ETCurrentNumber.setVisibility(View.VISIBLE);
            TVCurrentNumber.setVisibility(View.VISIBLE);
        }
        else
        {
            ETCurrentNumber.setVisibility(View.INVISIBLE);
            TVCurrentNumber.setVisibility(View.INVISIBLE);
        }
    }

    public void checkIfNumberInDatabase()
    {
        final DataBaseManager managerRead = new DataBaseManager(getApplicationContext(), "read");
        Cursor number = managerRead.lookingForNumber();
        if(number.moveToFirst())
        {
            currentNumber = number.getString(1);
        }
        else
        {
            Toast.makeText(this, "No hay numero registrado", Toast.LENGTH_SHORT).show();
            currentNumber = "";
        }

    }
}
