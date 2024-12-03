package br.com.robsonlmds.find_book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.robsonlmds.find_book.model.Livro;

public interface iLivrosRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContains(String titulo);
    List<Livro> findByIdiomasContains(String idiomas);
}
