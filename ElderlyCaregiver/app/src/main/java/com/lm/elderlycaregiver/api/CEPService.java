package com.lm.elderlycaregiver.api;

import com.lm.elderlycaregiver.model.Endereco;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService
{
    @GET("ws/{cep}/json/")
    Call<Endereco> consultarCEP(@Path("cep") String cep);
}
