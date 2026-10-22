package br.com.fiap.motiva.controller;

import br.com.fiap.motiva.model.RelatorioPrioridade;
import br.com.fiap.motiva.service.RelatorioPrioridadeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioPrioridadeController {

    private final RelatorioPrioridadeService service;

    public RelatorioPrioridadeController(RelatorioPrioridadeService service) {
        this.service = service;
    }

    // Gera o relatório a partir dos trechos atuais e grava no histórico
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RelatorioPrioridade gerar() {
        return service.gerarRelatorio();
    }

    @GetMapping
    public List<RelatorioPrioridade> listarHistorico() {
        return service.listarHistorico();
    }

    // GET /api/relatorios/periodo?inicio=2026-10-01&fim=2026-10-31
    @GetMapping("/periodo")
    public List<RelatorioPrioridade> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.listarPorPeriodo(inicio, fim);
    }
}
