package br.com.robsonlmds.find_book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.robsonlmds.find_book.model.Autor;

public interface iAutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContains(String nome);
   java.util.List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(Integer anoNascimento, Integer anoFalecimento);
}


