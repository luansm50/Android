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
    private String id;
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private String caminhoFoto;

    @Exclude
    public String getSenha() {
        return senha;
    }
}

