package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizaTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDto> lista(String curso) {
		List<Topico> topicos;

		if (curso == null) {
			topicos = topicoRepository.findAll();
		} else {
			topicos = topicoRepository.findByCursoNome(curso);
		}

		return TopicoDto.converter(topicos);
	}

	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm,
			UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public DetalhesTopicoDto detalhar(@PathVariable Long id) {
		return new DetalhesTopicoDto(topicoRepository.getOne(id));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizaTopicoForm topicoForm) {
		Topico topico = topicoForm.atualizar(id, topicoRepository);
		return ResponseEntity.ok(new TopicoDto(topico));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		topicoRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
