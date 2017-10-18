package com.example.martinmelo.uvarobotics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class ChangeNumber extends AppCompatActivity {
    private static EditText ETCurrentNumber, ETNewNumber;
    private static Switch SWShowNumber;
    private static ImageView imageBack;
    private static Button BtnUpdateNumber;
    private static TextView TVCurrentNumber;

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

        SWShowNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               CheckIfIsOn();
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
}
