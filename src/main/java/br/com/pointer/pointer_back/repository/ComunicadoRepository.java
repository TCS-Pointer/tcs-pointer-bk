package br.com.pointer.pointer_back.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pointer.pointer_back.model.Comunicado;

@Repository
public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {

    @Query("""
        SELECT c FROM Comunicado c
        WHERE c.ativo = true
        AND (:setor IS NULL OR :setor = '' OR :setor MEMBER OF c.setores)
        AND (:titulo IS NULL OR :titulo = '' OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
        AND (:apenasGestores IS NULL OR c.apenasGestores = :apenasGestores)
    """)
    Page<Comunicado> findByFilters(
        @Param("setor") String setor,
        @Param("titulo") String titulo,
        @Param("apenasGestores") Boolean apenasGestores,
        Pageable pageable
    );
}
