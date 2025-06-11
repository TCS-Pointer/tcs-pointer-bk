package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO;
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

    @Query("SELECT u FROM Usuario u WHERE " +
           "(:tipoUsuario IS NULL OR u.tipoUsuario = :tipoUsuario) AND " +
           "(:setor IS NULL OR u.setor = :setor)")
    Page<Usuario> findByFilters(
        @Param("tipoUsuario") String tipoUsuario,
        @Param("setor") String setor,
        Pageable pageable
    );

    @Query("SELECT new br.com.pointer.pointer_back.dto.TipoUsuarioStatsDTO(u.tipoUsuario, COUNT(u)) " +
           "FROM Usuario u " +
           "GROUP BY u.tipoUsuario " +
           "ORDER BY " +
           "CASE u.tipoUsuario " +
           "    WHEN 'ADMIN' THEN 1 " +
           "    WHEN 'GESTOR' THEN 2 " +
           "    WHEN 'COLABORADOR' THEN 3 " +
           "    ELSE 4 " +
           "END")
    List<TipoUsuarioStatsDTO> findTipoUsuarioStats();

    @Query("SELECT DISTINCT u.setor FROM Usuario u ORDER BY u.setor")
    List<String> findDistinctSetores();
}
