package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.PDI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDIRepository extends JpaRepository<PDI, Long> {
    List<PDI> findByIdUsuario(Long idUsuario);

    @Query("SELECT p FROM PDI p WHERE p.destinatario.id = :idDestinatario")
    List<PDI> findByIdDestinatario(@Param("idDestinatario") Long idDestinatario);

    @Query("SELECT p FROM PDI p JOIN FETCH p.destinatario")
    List<PDI> findAllWithDestinatario();
}