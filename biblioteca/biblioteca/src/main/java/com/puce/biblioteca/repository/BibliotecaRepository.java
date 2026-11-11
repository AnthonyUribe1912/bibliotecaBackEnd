package com.puce.biblioteca.repository;

import com.puce.biblioteca.model.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {
    // Consulta derivada para buscar autores ignorando mayúsculas y minúsculas[cite: 2, 3]
    List<Biblioteca> findByAutorContainingIgnoreCase(String texto);
}