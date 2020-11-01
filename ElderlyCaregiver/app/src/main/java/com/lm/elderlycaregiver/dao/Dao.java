package com.lm.elderlycaregiver.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T>
{
    Optional<T> buscar(String id);

    List<T> listarTodos();

    Boolean salvar(T t);

    Boolean alterar(T t);

    Boolean deletar(T t);
}
