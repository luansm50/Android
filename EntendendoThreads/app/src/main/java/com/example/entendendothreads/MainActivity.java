package com.example.entendendothreads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button botaoIniciar;

    private int numero;

    private boolean pararExecucao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botaoIniciar = findViewById(R.id.buttonIniciar);
    }

    public void iniciarThread(View view)
    {
      /* MyThread thread = new MyThread();
       thread.start();*/

      MyRunnable runnable = new MyRunnable();
      new Thread(runnable).start();

      pararExecucao = false;
    }

    public void pararThread(View view)
    {
        pararExecucao = true;
    }

    class MyRunnable implements Runnable
    {

        @Override
        public void run() {
            for(int i=0; i<=15; i++)
            {
                numero = i;
                Log.d("Thread", "contador: " + i);

                if(pararExecucao)
                    return;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        botaoIniciar.setText("contador: " + numero);
                    }
                });
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        botaoIniciar.setText("contador: " + numero);
//                    }
//                });


                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyThread extends Thread
    {
        @Override
        public void run() {
            for(int i=0; i<=15; i++)
            {
                Log.d("Thread", "contador: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}