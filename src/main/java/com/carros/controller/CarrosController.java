package com.carros.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carros.domain.Carro;
import com.carros.domain.CarroService;
import com.carros.domain.dto.CarroDTO;

@RestController
@RequestMapping("/api/v1/carros")
public class CarrosController {
	@Autowired
	private CarroService service;

	@GetMapping()
	public ResponseEntity<Iterable<Carro>> get() {
		return ResponseEntity.ok(service.getCarros());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Carro>> getCarro(@PathVariable("id") Long id) {
		Optional<Carro> carro = service.getCarroById(id);
		return (carro.isPresent()) ? ResponseEntity.ok(service.getCarroById(id)) : ResponseEntity.noContent().build();
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<Iterable<Carro>> getCarrosByTipo(@PathVariable("tipo") String tipo) {
		List<Carro> tipos = service.getCarrosByTipo(tipo);
		return !tipos.isEmpty() ? ResponseEntity.ok(tipos) : ResponseEntity.noContent().build();
	}

	@PostMapping()
	public ResponseEntity post(@RequestBody Carro carro) {
		try {
			Carro c = service.insert(carro);
			URI location = getUri(c.getId());
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Carro> put(@PathVariable("id") Long id, @RequestBody Carro carro) {
		try {
			Carro c = service.update(id, carro);
			getUri(c.getId());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Carro> delete(@PathVariable("id") Long id) {
		try {
			service.delete(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
