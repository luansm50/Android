package com.example.requisicoeshttp.api;

import com.example.requisicoeshttp.model.Foto;
import com.example.requisicoeshttp.model.Postagem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService
{
    @GET("/photos")
    Call<List<Foto>> recuperarFotos();

    @GET("/posts")
    Call<List<Postagem>> recuperarPostagens();


    @POST("/posts")
    Call<Postagem> salvarPostagens(@Body Postagem postagem);
}
