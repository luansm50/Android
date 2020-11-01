package com.example.requisicoeshttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.requisicoeshttp.api.CEPService;
import com.example.requisicoeshttp.api.DataService;
import com.example.requisicoeshttp.model.CEP;
import com.example.requisicoeshttp.model.Foto;
import com.example.requisicoeshttp.model.Postagem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;

    private Retrofit retrofit;
    private List<Foto> listaFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);

        retrofit = new Retrofit
                .Builder()
//                .baseUrl("https://viacep.com.br/ws")
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//               recuperarCepRetrofit();
//                 recuperarListaRetrofit();
                salvarPostagem();

//                MyTask task = new MyTask();
//                String urlApi = "https://blockchain.info/ticker";
//                task.execute(urlApi);
            }
        });
    }

    private void salvarPostagem()
    {
        Postagem postagem = new Postagem("1234", "ola mundo", "Hello world");

        DataService dataService = retrofit.create( DataService.class);
        Call<Postagem> call = dataService.salvarPostagens(postagem);

        call.enqueue(new Callback<Postagem>() {
            @Override
            public void onResponse(Call<Postagem> call, Response<Postagem> response) {
                if (response.isSuccessful())
                {
                    Postagem postagemResposta = response.body();

                    textoResultado.setText(
                            "Codigo: " + response.code() +
                            " id: " + postagemResposta.getId() +
                            " titulo: " + postagemResposta.getTitle()
                    );

                }
            }

            @Override
            public void onFailure(Call<Postagem> call, Throwable t) {

            }
        });

    }




    private void recuperarListaRetrofit()
    {
        DataService dataService = retrofit.create( DataService.class);
        Call<List<Foto>> call = dataService.recuperarFotos();

        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if(response.isSuccessful())
                {
                    listaFotos = response.body();

                    for(int i=0; i<listaFotos.size(); i++)
                    {
                        Foto foto = listaFotos.get(i);
                        Log.d("resultado", "resultado: " + foto.getId() + " / " + foto.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {

            }
        });

    }

    private void recuperarCepRetrofit()
    {
        CEPService cepService = retrofit.create( CEPService.class);
        Call<CEP> call = cepService.recuperarCEP("82900370");
        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if(response.isSuccessful())
                {
                    CEP cep = response.body();
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });
    }

















    class MyTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;

            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //inputStreamReader le os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader( inputStream );

                //Objeto utilizado para leitura dos caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();
                String linha = "";

                while((linha = reader.readLine()) != null)
                {
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            try {
                JSONObject jsonObject = new JSONObject(resultado);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            textoResultado.setText(resultado);
        }

    }



}