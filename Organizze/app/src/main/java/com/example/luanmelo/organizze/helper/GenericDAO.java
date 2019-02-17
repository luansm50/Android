package com.example.luanmelo.organizze.helper;

import java.util.List;

public interface GenericDAO<T> {


    public void salvar(T t);
    public void deletar(T t);
    public void alterar(T t);
    public List<T> listarTodos();

}
