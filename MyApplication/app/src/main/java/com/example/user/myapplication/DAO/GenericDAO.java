package com.example.user.myapplication.DAO;

import com.example.user.myapplication.model.Tarefa;

import java.util.List;

public interface GenericDAO<T> {

    public boolean salvar(T t);
    public boolean atualizar(T t);
    public boolean deletar(T t);
    public List<T> listar();
}
