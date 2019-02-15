package com.example.user.preferenciasdousuario;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button buttonSalve;
    private TextInputEditText editName;
    private TextView textResult;

    private static final String PREFERENCE_FILE = "PreferenceFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSalve = findViewById(R.id.buttonSalve);
        editName = findViewById(R.id.editName);
        textResult = findViewById(R.id.textResult);

        buttonSalve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, 0);
                SharedPreferences.Editor editor = preferences.edit();

                //validarNome
                if(editName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    String name = editName.getText().toString();
                    editor.putString("name", name);
                    editor.commit();
                    textResult.setText("Olá, " + name);
                }
            }
        });
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_FILE, 0);

        //valida se temos o nome em preferencia
        if(preferences.contains("name")){
            String name = preferences.getString("name", "Olá, usuário não definido");
            textResult.setText(name);
        }else{
            textResult.setText("Olá, usuário não definido");
        }

    }
}
