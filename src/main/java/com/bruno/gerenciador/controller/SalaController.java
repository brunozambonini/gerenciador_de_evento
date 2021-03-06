package com.bruno.gerenciador.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bruno.gerenciador.model.Pessoa;
import com.bruno.gerenciador.model.Sala;
import com.bruno.gerenciador.service.PessoaService;
import com.bruno.gerenciador.service.SalaService;

@Controller
public class SalaController {

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private SalaService salaService;

	//
	@GetMapping("/listaSalas")
	public String listaSalas(Model model) {
		model.addAttribute("listaSalas", salaService.getAllSalas());
		return "Sala/lista_sala"; // nome do arquivo html
	}

	//
	@GetMapping("/novaSalaForm")
	public String novaSala(Model model) {
		Sala sala = new Sala();
		model.addAttribute("sala", sala);

		return "Sala/nova_sala";
	}

	//
	@PostMapping("/salvarSala")
	public String salvarSala(@ModelAttribute("sala") @Valid Sala sala, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique se os campos foram preenchidos corretamente!");
			return "redirect:/novaSalaForm";
		}
		salaService.saveSala(sala);
		return "redirect:/listaSalas";
	}

	// Mostrar dados da sala para serem editados ou não
	@GetMapping("/sala/{id}")
	public String detalhesSala(@PathVariable(value = "id") long id, Model model) {
		// pega sala do banco de dados
		Sala sala = salaService.getSalaById(id);
		List<Pessoa> listaPessoas1 = pessoaService.getPessoasNaSala1(sala);
		List<Pessoa> listaPessoas2 = pessoaService.getPessoasNaSala2(sala);

		int quantidadeSala1 = listaPessoas1.size();
		int quantidadeSala2 = listaPessoas2.size();
		// popular o form com os dados da sala
		model.addAttribute("sala", sala);
		model.addAttribute("listaPessoas1", listaPessoas1);
		model.addAttribute("listaPessoas2", listaPessoas2);

		model.addAttribute("quantidadeSala1", quantidadeSala1);
		model.addAttribute("quantidadeSala2", quantidadeSala2);

		return "Sala/detalhes_sala";
	}

	@GetMapping("/deleteSala/{id}")
	public String deleteSala(@PathVariable("id") Long id) {
		this.salaService.deleteSalaById(id);
		return "redirect:/listaSalas";
	}
}
