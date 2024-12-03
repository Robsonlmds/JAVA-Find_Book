package br.com.robsonlmds.find_book.model;

import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true)
    private String titulo;
    private String autor;
    private String idiomas;
    private Integer downloads;

    //CONSTRUTOR PADRÃO
    public Livro () {}

    public Livro (DadosLivro dadosLivro){
        this.titulo =  dadosLivro.titulo();
        this.autor =  pegarAutor(dadosLivro).getNome();
        this.idiomas =  idiomaMod(dadosLivro.idiomas());
        this.downloads =  dadosLivro.downloads();
    }

    private String idiomaMod(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "DESCONHECIDO";
        } else {
            return idiomas.get(0);
        }
    }

    private Autor pegarAutor(DadosLivro dadosLivro) {
        DadosAutor dadosAutor = dadosLivro.autor().get(0);
        return new Autor(dadosAutor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    @Override
    public String toString() {
        return "\n\t**** info livro ****" +
                "\n\tTitulo: " + titulo +
                "\n\tAutor: " + autor +
                "\n\tIdiomas: " + idiomas +
                "\n\tDownloads: " + downloads +
                "\n\t*******************";
    }
    
}
