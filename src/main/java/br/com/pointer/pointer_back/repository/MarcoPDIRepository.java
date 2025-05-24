package br.com.pointer.pointer_back.repository;

import br.com.pointer.pointer_back.model.MarcoPDI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarcoPDIRepository extends JpaRepository<MarcoPDI, Long> {
    List<MarcoPDI> findByPdiId(Long pdiId);
}