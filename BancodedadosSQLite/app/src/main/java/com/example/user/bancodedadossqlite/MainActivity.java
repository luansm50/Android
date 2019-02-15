package com.example.user.bancodedadossqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            //Criar banco de dados
            SQLiteDatabase bancoDados = openOrCreateDatabase("app", MODE_PRIVATE, null);

            //Criar tabela
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS pessoas (nome VARCHAR, idade INT(3))");

            //inserir dados
            /*bancoDados.execSQL("INSERT INTO pessoas(nome, idade) VALUES('Luan', 23)" );
            bancoDados.execSQL("INSERT INTO pessoas(nome, idade) VALUES('Andy', 26)" );*/

            bancoDados.execSQL("UPDATE pessoas SET idade = 23 WHERE nome = 'Luan'");

            //recuperar dados
            /*String consulta = "SELECT nome, idade " +
                    "FROM pessoas WHERE nome = 'Luan' AND idade = 23 ";*/

            /*String consulta = "SELECT nome, idade " +
                    "FROM pessoas WHERE idade > 23 OR idade =  23 ";*/

            /*String consulta = "SELECT nome, idade " +
                    "FROM pessoas WHERE nome LIKE '%an%'  ";*/

            String consulta = "SELECT nome, idade " +
                    "FROM pessoas WHERE 1=1 ORDER BY nome ASC ";

            Cursor cursor = bancoDados.rawQuery(consulta, null);

            //indices da tabela
            int indiceNome = cursor.getColumnIndex("nome");
            int indiceIdade = cursor.getColumnIndex("idade");

            cursor.moveToFirst();
            while(cursor != null){
                String nome = cursor.getString(indiceNome);
                String idade = cursor.getString(indiceIdade);
                Log.i("RESULTADO", "nome: " + nome + " / idade: " + idade  );
                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
