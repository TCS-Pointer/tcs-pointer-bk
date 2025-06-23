package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.ComunicadoLeitura;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComunicadoLeituraRepository extends JpaRepository<ComunicadoLeitura, Long> {
    Optional<ComunicadoLeitura> findByComunicadoAndUsuario(Comunicado comunicado, Usuario usuario);
    List<ComunicadoLeitura> findByComunicado(Comunicado comunicado);
} 