package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.dto.PdiListagemDTO;
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

    @Query("SELECT new br.com.pointer.pointer_back.dto.PdiListagemDTO(" +
            "p.id, p.titulo, p.descricao, p.status, p.dtInicio, p.dtFim, " +
            "d.id, d.nome, d.cargo, d.setor, d.email, " +
            "COUNT(m), SUM(CASE WHEN m.status = 'CONCLUIDO' OR m.status = 'Concluído' THEN 1 ELSE 0 END)) " +
            "FROM PDI p " +
            "JOIN p.destinatario d " +
            "LEFT JOIN p.marcos m " +
            "GROUP BY p.id, p.titulo, p.descricao, p.status, p.dtInicio, p.dtFim, d.id, d.nome, d.cargo, d.setor, d.email")
    List<PdiListagemDTO> buscarTodosParaListagem();
}