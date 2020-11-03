package com.lm.elderlycaregiver.api;

import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService
{
    @PUT("usuarios/{idUsuario}/.json")
    Call<Usuario> salvarUsuario(@Path("idUsuario") String idUsuario, @Body Usuario usuario);

    @GET("usuarios/{idUsuario}.json")
    Call<Usuario> recuperarDadosUsuario(@Path("idUsuario") String idUsuario);

    @PATCH("usuarios/{idUsuario}/.json")
    Call<Usuario> atualizarDadosCuidador(@Path("idUsuario") String idUsuario, @Body Usuario usuario);
}
