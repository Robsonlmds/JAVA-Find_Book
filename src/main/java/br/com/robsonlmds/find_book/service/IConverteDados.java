package br.com.robsonlmds.find_book.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
