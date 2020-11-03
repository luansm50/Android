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
    @Builder.Default
    private Double valorHora = 0.00;

    @Builder.Default
    private Double valorDiurno = 0.00;

    @Builder.Default
    private Double valorNoturno = 0.00;

    @Builder.Default
    private Boolean integral = false;

    @Builder.Default
    private Double valorIntegral = 0.00;

    @Builder.Default
    private Boolean cuidadorAtivado = false;

}
