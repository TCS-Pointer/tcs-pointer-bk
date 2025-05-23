package br.com.pointer.pointer_back.mapper;

import org.springframework.stereotype.Component;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.model.PDI;

@Component
public class PDIMapper {
    //TODO: refactor DTO
    public PDI toEntity(pdiDTO dto) {
        if (dto == null) {
            return null;
        }

        PDI pdi = new PDI();
        pdi.setTitulo(dto.getTitulo());
        pdi.setDescricao(dto.getDescricao());
        pdi.setDtInicio(dto.getDataInicio());
        pdi.setDtFim(dto.getDataFim());
        pdi.setIdUsuario(dto.getIdUsuario());
        pdi.setStatus(dto.getStatus());
        // dataCriacao é gerenciado pelo @PrePersist
        return pdi;
    }

    public void updateEntityFromDTO(pdiDTO dto, PDI pdi) {
        if (dto == null || pdi == null) {
            return;
        }

        pdi.setTitulo(dto.getTitulo());
        pdi.setDescricao(dto.getDescricao());
        pdi.setDtInicio(dto.getDataInicio());
        pdi.setDtFim(dto.getDataFim());
        pdi.setIdUsuario(dto.getIdUsuario());
        pdi.setStatus(dto.getStatus());
    }
}
