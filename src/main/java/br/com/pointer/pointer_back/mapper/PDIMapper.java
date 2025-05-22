package br.com.pointer.pointer_back.mapper;

import org.springframework.stereotype.Component;
import br.com.pointer.pointer_back.dto.PDIRequestDTO;
import br.com.pointer.pointer_back.dto.PDIResponseDTO;
import br.com.pointer.pointer_back.model.PDI;

@Component
public class PDIMapper {
    //TODO: refactor DTO
    public PDI toEntity(PDIRequestDTO dto) {
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

    public PDIResponseDTO toResponseDTO(PDI pdi) {
        if (pdi == null) {
            return null;
        }

        PDIResponseDTO dto = new PDIResponseDTO();
        dto.setId(pdi.getId());
        dto.setTitulo(pdi.getTitulo());
        dto.setDescricao(pdi.getDescricao());
        dto.setDataInicio(pdi.getDtInicio());
        dto.setDataFim(pdi.getDtFim());
        dto.setIdUsuario(pdi.getIdUsuario());
        dto.setStatus(pdi.getStatus());
        dto.setDataCriacao(pdi.getDataCriacao() != null ? pdi.getDataCriacao().toLocalDate() : null);

        return dto;
    }

    public void updateEntityFromDTO(PDIRequestDTO dto, PDI pdi) {
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
