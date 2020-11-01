package com.lm.elderlycaregiver.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanoCuidador
{
    public static Integer DIARIA     = 100;
    public static Integer SEMANAL    = 200;
    public static Integer MENSAL     = 300;

    public static Integer[] listaPlanos = new Integer[]{DIARIA, SEMANAL, MENSAL};

    private String  idCuidador;
    @Builder.Default

    private Integer tipoPlano = DIARIA;
    @Builder.Default

    private Boolean habilitado = false;

    @Builder.Default
    private Double  valorUnitario = 0.0;

    @Builder.Default
    private Double valorDiferenciado = 0.0;
}
