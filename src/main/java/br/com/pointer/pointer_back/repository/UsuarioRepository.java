package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO;
import br.com.pointer.pointer_back.model.StatusUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByKeycloakId(String keycloakId);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.setor = :setor AND u.keycloakId != :keycloakId")
    List<Usuario> findBySetor(String setor, String keycloakId);

    @Query("SELECT u FROM Usuario u WHERE " +
           "(:tipoUsuario IS NULL OR u.tipoUsuario = :tipoUsuario) AND " +
           "(:setor IS NULL OR u.setor = :setor) AND " +
           "(:status IS NULL OR u.status = :status) " +
           "AND (:nome IS NULL OR :nome = '' OR u.nome LIKE CONCAT('%', :nome, '%')) " +
           "ORDER BY u.dataCriacao DESC")
    Page<Usuario> findByFilters(
        @Param("tipoUsuario") String tipoUsuario,
        @Param("setor") String setor,
        @Param("status") StatusUsuario status,
        @Param("nome") String nome,
        Pageable pageable
    );

    @Query("SELECT new br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO(tipo, " +
           "COALESCE((SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = tipo AND u.status = 'ATIVO'), 0)) " +
           "FROM (SELECT 'ADMIN' as tipo UNION SELECT 'GESTOR' UNION SELECT 'COLABORADOR') tipos " +
           "ORDER BY " +
           "CASE tipo " +
           "    WHEN 'ADMIN' THEN 1 " +
           "    WHEN 'GESTOR' THEN 2 " +
           "    WHEN 'COLABORADOR' THEN 3 " +
           "    ELSE 4 " +
           "END")
    List<TipoUsuarioStatsDTO> findTipoUsuarioStats();

    @Query("SELECT DISTINCT u.setor FROM Usuario u ORDER BY u.setor")
    List<String> findDistinctSetores();
}
