package com.carros.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.carros.domain.dto.CarroDTO;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class CarroService {

	@Autowired
	private CarroRepository rep;

	public Iterable<Carro> getCarros() {
		return rep.findAll();
	}

	public Optional<Carro> getCarroById(Long id) {
		return rep.findById(id);
	}

	public List<Carro> getCarrosByTipo(String tipo) {
		return rep.findByTipo(tipo);
	}

	public Carro insert(Carro carro) {
		Assert.isNull(carro.getId(), "Não foi possivel inserir o registro");
		return rep.save(carro);
	}

	public Carro update(Long id, Carro carro) {
		Optional<Carro> optional = getCarroById(id);
		Carro db = null;
		if (optional.isPresent()) {
			db = optional.get();
			db.setNome(carro.getNome());
			db.setTipo(carro.getTipo());
			rep.save(db);
		}
		Assert.isNull(carro.getId(), "Não foi possivel atualizar o registro");
		return db;
	}
	public void delete(Long id) {
		Optional<Carro> optional = getCarroById(id);
		if (optional.isPresent()) {
			rep.deleteById(id);
		}
		Assert.isTrue(optional.isPresent(), "Não foi possivel deletar o registro");
	}

}
