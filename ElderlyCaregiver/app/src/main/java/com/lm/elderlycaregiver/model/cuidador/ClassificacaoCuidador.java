package com.lm.elderlycaregiver.model.cuidador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificacaoCuidador
{
    private String idCuidador;
    private String idPerfil;

    private Integer qtEstrelas;
}
