package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

  @Autowired private PacienteRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(
      @RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
    var paciente = new Paciente(dados);
    repository.save(paciente);
    var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
    return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
  }

  @GetMapping
  public ResponseEntity<Page<DadosListagemPaciente>> listar(
      @PageableDefault(
              size = 10,
              sort = {"nome"})
          Pageable paginacao) {
    var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    return ResponseEntity.ok(page);
  }

  @PutMapping
  @Transactional
  public ResponseEntity<DadosDetalhamentoPaciente> atualizar(
      @RequestBody @Valid DadosAtualizacaoPaciente dados) {
    var paciente =
        repository
            .findById(dados.id())
            .orElseThrow(() -> new RuntimeException("Paciente not found"));
    paciente.atualizarInformacoes(dados);

    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Void> excluir(@PathVariable Long id) {
    var paciente =
        repository.findById(id).orElseThrow(() -> new RuntimeException("Paciente not found"));
    paciente.excluir();

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  @Transactional
  public ResponseEntity<DadosDetalhamentoPaciente> detalhar(@PathVariable Long id) {
    var paciente = repository.getReferenceById(id);

    return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
  }
}
