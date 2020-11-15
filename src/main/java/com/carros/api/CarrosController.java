package com.carros.api;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
	@Secured({ "ROLE_USER" })
	public ResponseEntity<List<CarroDTO>> get() {
		List<CarroDTO> carros = service.getCarros();
		return ResponseEntity.ok(carros);
	}

	@GetMapping("/{id}")
	@Secured({ "ROLE_USER" })
	public ResponseEntity<CarroDTO> get(@PathVariable("id") Long id) {
		CarroDTO carro = service.getCarroById(id);

		return ResponseEntity.ok(carro);
	}

	@GetMapping("/tipo/{tipo}")
	@Secured({ "ROLE_USER" })
	public ResponseEntity<List<CarroDTO>> getCarrosByTipo(@PathVariable("tipo") String tipo) {
		List<CarroDTO> carros = service.getCarrosByTipo(tipo);
		return carros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carros);
	}

	@PostMapping
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<?> post(@RequestBody Carro carro) {

		try {
			CarroDTO c = service.insert(carro);

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
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody Carro carro) {

		CarroDTO c = service.update(carro, id);

		return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {

		service.delete(id);

		return ResponseEntity.ok().build();
	}
}
