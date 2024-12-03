package br.com.robsonlmds.find_book.principal;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import br.com.robsonlmds.find_book.model.Autor;
import br.com.robsonlmds.find_book.model.DadosLivro;
import br.com.robsonlmds.find_book.model.Livro;
import br.com.robsonlmds.find_book.model.Results;
import br.com.robsonlmds.find_book.repository.iAutorRepository;
import br.com.robsonlmds.find_book.repository.iLivrosRepository;
import br.com.robsonlmds.find_book.service.ConsumoApi;
import br.com.robsonlmds.find_book.service.ConverteDados;

public class Principal {
    
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private iLivrosRepository livrosRepositorio;
    private iAutorRepository autorRepositorio;
    private static String API_URL = "https://gutendex.com/books/?search=";

    List<Livro> livros;
    List<Autor> autor;

    public Principal(iLivrosRepository livrosRepositorio, iAutorRepository autorRepositorio) {
        this.livrosRepositorio = livrosRepositorio;
        this.autorRepositorio = autorRepositorio;
    }
    
    public void exibirMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menuEscrito = """
                    |***************************************************|
                    |*****                BEM-VINDO               ******|
                    |***************************************************|
                    
                    1 - Buscar livro por nome
                    2 - Listar livros salvos
                    3 - Listar autores salvos
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros por idioma
                    
                    0 - sair
                    
                    |***************************************************|
                    |*****            ESCOLHA UMA OPÇÂO           ******|
                    |***************************************************|
                    
                    """;
            try{
                System.out.println(menuEscrito);
                opcao = leitura.nextInt();
                leitura.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("|***************************************************|");
                System.out.println("|*****         insira um número válido        ******|");
                System.out.println("|***************************************************|");
                leitura.nextLine();
                continue;
            }

            switch (opcao) {
                case 1 -> buscarLivro();
                case 2 -> listarAutoreSalvos();
                case 3 -> listarLivrosSalvos();
                case 4 -> listarAutoresVivosEmCertoAno();
                case 5 -> listarLivrosPorIdiomas();
            }
        }
    }

    private void listarLivrosPorIdiomas() {
        System.out.println("lista de livros por idioma\n------------");
        System.out.println("""
                \n\t---- escolha o idioma ----
                \ten - inglês
                \tes - espanhol
                \tfr - francês
                \tpt - português
                """);
        String idioma = leitura.nextLine();
        livros = livrosRepositorio.findByIdiomasContains(idioma);
        if (livros.isEmpty()) {
            System.out.println("LIVROS NÃO ENCONTRADO PELO IDIOMA");
            listarLivrosPorIdiomas();
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresVivosEmCertoAno() {
        System.out.println("LISTE OS AUTORES VIVOS A PARTIR DESTA DATA, DIGA UM ANO");
        Integer ano = Integer.valueOf(leitura.nextLine());
        autor = autorRepositorio
                    .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autor.isEmpty()) {
            System.out.println("AUTORES VIVOS NÃO ENCONTRADOS");
        } else {
            autor.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoreSalvos() {
        System.out.println("LISTA DE AUTORES DO BANCO DE DADOS\n");
        autor = autorRepositorio.findAll();
        autor.stream() 
                .sorted(Comparator.comparing(Autor::getNome))   
                .forEach(System.out::println);
    }

    private void listarLivrosSalvos() {
        System.out.println("LISTA DE LIVROS DO BANCO DE DADOS\n");
        livros = livrosRepositorio.findAll();
        livros.stream() 
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void buscarLivro() {
        System.out.println("QUAL LIVRO VOCÊ DESEJA BUSCAR?");
        var nomeLivro = leitura.nextLine().toLowerCase();
        var json = consumo.obterDados(API_URL + nomeLivro.replace("", "%20").trim());
        var dados = conversor.obterDados(json, Results.class);
        if (dados.results().isEmpty()) {
            System.out.println("LIVRO NÃO ENCONTRADO");
        }  else {
            DadosLivro dadosLivro = dados.results().get(0);
            Livro livro = new Livro(dadosLivro);
            Autor autor = new Autor().pegaAutor(dadosLivro);
            salvarDados(livro, autor);
        }
    }

    private void salvarDados(Livro livro, Autor autor) {
        Optional<Livro> livroEncontrado = livrosRepositorio.findByTituloContains(livro.getTitulo());
        if (livroEncontrado.isEmpty()) {
            System.out.println("ESTE LIVRO JÁ ESTA NO BANCO DE DADOS");
            System.out.println(livro.toString());
        } else {
            try {
                livrosRepositorio.save(livro);
                System.out.println("LIVRO SALVO COM SUCESSO");
                System.out.println(livro);
            } catch (Exception e) {
                System.out.println("ERRO " + e.getMessage());
            }
        }

        Optional<Autor> autorEncontrado = autorRepositorio.findByNomeContains(autor.getNome());
        if (autorEncontrado.isPresent()) {
            System.out.println("ESTÉ AUTOR JÁ ESTA NO BANCO DE DADOS");
            System.out.println(autor.toString());
        } else {
            try {
                autorRepositorio.save(autor);
                System.out.println("AUTOR SALVO COM SUCESSO");
                System.out.println(autor);
            } catch (Exception e) {
                System.out.println("ERRO " + e.getMessage());
            }
        }
    }
}
