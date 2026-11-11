package com.puce.biblioteca.controller;

import com.puce.biblioteca.model.Biblioteca;
import com.puce.biblioteca.service.BibliotecaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libro")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @GetMapping
    public ResponseEntity<List<Biblioteca>> obtenerTodos() {
        return ResponseEntity.ok(bibliotecaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biblioteca> obtenerPorId(@PathVariable Long id) {
        return bibliotecaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Biblioteca> crear(@RequestBody Biblioteca libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bibliotecaService.guardar(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Biblioteca> actualizar(@PathVariable Long id, @RequestBody Biblioteca libro) {
        return bibliotecaService.actualizar(id, libro)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (bibliotecaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- Endpoints para Situaciones Especiales ---

    @GetMapping("/disponibles")
    public ResponseEntity<List<Biblioteca>> obtenerDisponibles() {
        return ResponseEntity.ok(bibliotecaService.obtenerDisponibles());
    }

    @GetMapping("/autor/{texto}")
    public ResponseEntity<List<Biblioteca>> buscarPorAutor(@PathVariable String texto) {
        return ResponseEntity.ok(bibliotecaService.buscarPorAutor(texto));
    }

    @GetMapping("/antiguos")
    public ResponseEntity<List<Biblioteca>> obtenerAntiguos(
            @RequestParam(defaultValue = "1950") int antes) {
        return ResponseEntity.ok(bibliotecaService.obtenerAntiguos(antes));
    }

    @PutMapping("/{id}/prestar")
    public ResponseEntity<Void> prestarLibro(@PathVariable Long id) {
        int resultado = bibliotecaService.prestarLibro(id);
        if (resultado == 0) return ResponseEntity.ok().build();
        if (resultado == 1) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409[cite: 2, 3]
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverLibro(@PathVariable Long id) {
        int resultado = bibliotecaService.devolverLibro(id);
        if (resultado == 0) return ResponseEntity.ok().build();
        if (resultado == 1) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409[cite: 2, 3]
    }
}