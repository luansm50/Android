package com.lm.elderlycaregiver.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(Activity activity, String[] permissoes, int requestCode){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();
            /*Percorre as permissoes passadas,
             * verificando uma uma
             * se já tem a permissao liberada*/
            for(String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity.getApplicationContext(), permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao) listaPermissoes.add(permissao);
            }
            if(listaPermissoes.isEmpty())return true;
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }
}
