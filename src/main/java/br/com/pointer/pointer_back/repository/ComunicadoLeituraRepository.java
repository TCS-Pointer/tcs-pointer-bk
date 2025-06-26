package br.com.pointer.pointer_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pointer.pointer_back.model.ComunicadoLeitura;

@Repository
public interface ComunicadoLeituraRepository extends JpaRepository<ComunicadoLeitura, Long> {
    Optional<ComunicadoLeitura> findByComunicadoIdAndUsuarioId(Long comunicadoId, Long usuarioId);
    List<ComunicadoLeitura> findByComunicadoId(Long comunicadoId);
    int countByComunicadoId(Long comunicadoId);
    boolean existsByComunicadoIdAndUsuarioId(Long comunicadoId, Long usuarioId);
}