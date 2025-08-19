package com.literalura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Serviço para conversão de dados JSON utilizando Jackson
 */
@Service
public class ConversaoService {

    private final ObjectMapper objectMapper;

    public ConversaoService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Converte JSON string para objeto da classe especificada
     * @param json String JSON
     * @param classe Classe de destino
     * @param <T> Tipo do objeto
     * @return Objeto convertido
     * @throws RuntimeException se ocorrer erro na conversão
     */
    public <T> T jsonParaObjeto(String json, Class<T> classe) {
        try {
            return objectMapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao processar JSON: " + e.getMessage());
            System.err.println("JSON recebido: " + json);
            throw new RuntimeException("Falha na conversão do JSON", e);
        }
    }

    /**
     * Converte objeto para JSON string
     * @param objeto Objeto a ser convertido
     * @return String JSON
     * @throws RuntimeException se ocorrer erro na conversão
     */
    public String objetoParaJson(Object objeto) {
        try {
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao converter objeto para JSON: " + e.getMessage());
            throw new RuntimeException("Falha na conversão para JSON", e);
        }
    }

    /**
     * Converte JSON string para objeto da classe especificada com tratamento de erro suave
     * @param json String JSON
     * @param classe Classe de destino
     * @param <T> Tipo do objeto
     * @return Objeto convertido ou null se houver erro
     */
    public <T> T jsonParaObjetoSeguro(String json, Class<T> classe) {
        try {
            return jsonParaObjeto(json, classe);
        } catch (RuntimeException e) {
            System.err.println("Erro na conversão (modo seguro): " + e.getMessage());
            return null;
        }
    }
}