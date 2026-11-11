package com.puce.biblioteca.service;

import com.puce.biblioteca.model.Biblioteca;
import com.puce.biblioteca.repository.BibliotecaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;

    public BibliotecaService(BibliotecaRepository bibliotecaRepository) {
        this.bibliotecaRepository = bibliotecaRepository;
    }

    // --- Métodos CRUD Básicos ---

    public List<Biblioteca> obtenerTodos() {
        return bibliotecaRepository.findAll();
    }

    public Optional<Biblioteca> obtenerPorId(Long id) {
        return bibliotecaRepository.findById(id);
    }

    public Biblioteca guardar(Biblioteca libro) {
        return bibliotecaRepository.save(libro);
    }

    public Optional<Biblioteca> actualizar(Long id, Biblioteca detalles) {
        return bibliotecaRepository.findById(id).map(libro -> {
            libro.setTitulo(detalles.getTitulo());
            libro.setAutor(detalles.getAutor());
            libro.setAnio(detalles.getAnio());
            libro.setDisponible(detalles.isDisponible());
            return bibliotecaRepository.save(libro);
        });
    }

    public boolean eliminar(Long id) {
        if (bibliotecaRepository.existsById(id)) {
            bibliotecaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Situaciones Especiales ---

    public List<Biblioteca> obtenerDisponibles() {
        return bibliotecaRepository.findAll().stream()
                .filter(Biblioteca::isDisponible)
                .collect(Collectors.toList());
    }

    public List<Biblioteca> buscarPorAutor(String texto) {
        return bibliotecaRepository.findByAutorContainingIgnoreCase(texto);
    }

    public List<Biblioteca> obtenerAntiguos(int antes) {
        return bibliotecaRepository.findAll().stream()
                .filter(libro -> libro.getAnio() < antes)
                .collect(Collectors.toList());
    }

    public int prestarLibro(Long id) {
        // Retorna: 0 -> Éxito (200), 1 -> No encontrado (404), 2 -> Conflicto (409)[cite: 2, 3]
        Optional<Biblioteca> libroOpt = bibliotecaRepository.findById(id);
        if (libroOpt.isEmpty()) {
            return 1;
        }
        Biblioteca libro = libroOpt.get();
        if (!libro.isDisponible()) {
            return 2; // Ya estaba prestado[cite: 2, 3]
        }
        libro.setDisponible(false);
        bibliotecaRepository.save(libro);
        return 0;
    }

    public int devolverLibro(Long id) {
        // Retorna: 0 -> Éxito (200), 1 -> No encontrado (404), 2 -> Conflicto (409)[cite: 2, 3]
        Optional<Biblioteca> libroOpt = bibliotecaRepository.findById(id);
        if (libroOpt.isEmpty()) {
            return 1;
        }
        Biblioteca libro = libroOpt.get();
        if (libro.isDisponible()) {
            return 2; // No estaba prestado[cite: 2, 3]
        }
        libro.setDisponible(true);
        bibliotecaRepository.save(libro);
        return 0;
    }
}