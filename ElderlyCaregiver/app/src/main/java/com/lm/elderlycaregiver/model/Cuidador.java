package com.lm.elderlycaregiver.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cuidador  implements Serializable
{
    private String idUsuario;

    private Usuario usuario;

    @Builder.Default
    private Boolean ativado = false;

    @Builder.Default
    private List<PlanoCuidador> planos = new ArrayList<>();

    @Builder.Default
    private Double classificacao = 5.0;

    public Boolean salvar(Cuidador cuidador) {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("cuidadores").child(cuidador.getIdUsuario());
        usuariosRef.setValue(cuidador);
        return true;
    }




    @Exclude
    public Usuario getUsuario() {
        return usuario;
    }
}
