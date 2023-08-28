package br.com.alura.TabelaFipe.services;

import java.util.List;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
