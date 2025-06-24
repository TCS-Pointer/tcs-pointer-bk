package br.com.pointer.pointer_back.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pointer.pointer_back.dto.FeedbackStatsDTO;
import br.com.pointer.pointer_back.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Buscar feedbacks recebidos por um usuário
    Page<Feedback> findByIdUsuarioDestinatario(Long idUsuarioDestinatario, Pageable pageable);

    // Buscar feedbacks enviados por um usuário
    Page<Feedback> findByIdUsuarioRemetente(Long idUsuarioRemetente, Pageable pageable);

    // Buscar feedbacks recebidos com filtro por palavra-chave
    @Query("SELECT f FROM Feedback f WHERE f.idUsuarioDestinatario = :userId AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "(LOWER(f.assunto) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosFortes) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosMelhoria) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.acoesRecomendadas) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<Feedback> findRecebidosByKeyword(@Param("userId") Long userId, 
                                         @Param("keyword") String keyword, 
                                         Pageable pageable);

    // Buscar feedbacks enviados com filtro por palavra-chave
    @Query("SELECT f FROM Feedback f WHERE f.idUsuarioRemetente = :userId AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "(LOWER(f.assunto) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosFortes) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosMelhoria) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.acoesRecomendadas) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<Feedback> findEnviadosByKeyword(@Param("userId") Long userId, 
                                        @Param("keyword") String keyword, 
                                        Pageable pageable);

    // Buscar todos os feedbacks com filtro por palavra-chave (para admins)
    @Query("SELECT f FROM Feedback f WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "(LOWER(f.assunto) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosFortes) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.pontosMelhoria) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.acoesRecomendadas) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<Feedback> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new br.com.pointer.pointer_back.dto.FeedbackStatsDTO(f.tipoFeedback, COUNT(f)) FROM Feedback f GROUP BY f.tipoFeedback")
    List<FeedbackStatsDTO> findFeedbackStats();

} 