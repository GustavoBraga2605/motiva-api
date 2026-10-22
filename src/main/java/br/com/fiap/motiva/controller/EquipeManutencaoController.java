package br.com.fiap.motiva.controller;

import br.com.fiap.motiva.model.EquipeManutencao;
import br.com.fiap.motiva.service.EquipeManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipes")
public class EquipeManutencaoController {

    private final EquipeManutencaoService service;

    public EquipeManutencaoController(EquipeManutencaoService service) {
        this.service = service;
    }

    // GET /api/equipes               -> todas
    // GET /api/equipes?regiao=Região Norte -> só as dessa região
    @GetMapping
    public List<EquipeManutencao> listar(@RequestParam(required = false) String regiao) {
        return regiao == null ? service.listar() : service.listarPorRegiao(regiao);
    }

    @GetMapping("/{id}")
    public EquipeManutencao buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipeManutencao criar(@RequestBody EquipeManutencao equipe) {
        return service.criar(equipe);
    }

    @PutMapping("/{id}")
    public EquipeManutencao atualizar(@PathVariable Integer id, @RequestBody EquipeManutencao equipe) {
        return service.atualizar(id, equipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
