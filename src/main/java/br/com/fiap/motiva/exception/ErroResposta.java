package br.com.fiap.motiva.exception;

// Formato padrão do JSON de erro devolvido pela API
public record ErroResposta(int status, String erro, String mensagem) {
}
