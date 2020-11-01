package com.lm.elderlycaregiver.api;

import com.lm.elderlycaregiver.model.Cuidador;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CuidadorService
{
    @Headers({"Accept: application/json"})
    @GET("cuidadores/{idUsuario}.json")
    Call<Cuidador> recuperarCuidador(@Path("idUsuario") String idUsuario);

    @PATCH("cuidadores/{idUsuario}/.json")
    Call<Cuidador> atualizarDadosCuidador(@Path("idUsuario") String idUsuario, @Body Cuidador cuidador);
}
