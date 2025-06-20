package br.com.pointer.pointer_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.pointer.pointer_back.model.Comunicado;
import java.util.List;

@Repository
public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    List<Comunicado> findBySetor(String setor);
    List<Comunicado> findByApenasGestores(boolean apenasGestores);
    List<Comunicado> findBySetorAndApenasGestores(String setor, boolean apenasGestores);
}
