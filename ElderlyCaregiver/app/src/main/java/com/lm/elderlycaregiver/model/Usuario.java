package com.lm.elderlycaregiver.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario  implements Serializable
{
    public static final String NOVO         = "NOVO";
    public static final String RESPONSAVEL  = "RESPONSAVEL";
    public static final String CUIDADOR     = "CUIDADOR";

    private String id;
    private String nome;
    private String dataNascimento;
    private String telefone;
    private String cpf;
    private String email;
    private String senha;
    private String caminhoFoto;

    private Endereco endereco;
    private Cuidador cuidador;


    @Builder.Default
    private String tipoUsuario = NOVO;

    @Exclude
    public String getSenha() {
        return senha;
    }
}

