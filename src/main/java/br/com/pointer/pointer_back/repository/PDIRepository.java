package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.PDI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDIRepository extends JpaRepository<PDI, Long> {
    List<PDI> findByIdUsuario(Long idUsuario);

    List<PDI> findByIdDestinatario(Long idDestinatario);
}