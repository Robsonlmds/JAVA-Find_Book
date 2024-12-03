package br.com.robsonlmds.find_book;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.robsonlmds.find_book.repository.iAutorRepository;
import br.com.robsonlmds.find_book.repository.iLivrosRepository;

@SpringBootApplication
public class FindBookApplication implements CommandLineRunner {
	@Autowired
	private iLivrosRepository livrosRepositorio;

	@Autowired
	private iAutorRepository autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(FindBookApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livrosRepositorio, autorRepositorio);
		principal.exibeMenu();
	}
}
