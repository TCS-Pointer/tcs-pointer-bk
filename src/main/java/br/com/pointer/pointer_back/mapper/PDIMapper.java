package br.com.pointer.pointer_back.mapper;

import org.springframework.stereotype.Component;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.enums.StatusPDI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PDIMapper {
    private static final Logger logger = LoggerFactory.getLogger(PDIMapper.class);

    public PDI toEntity(pdiDTO dto) {
        try {
            if (dto == null) {
                logger.error("DTO é nulo");
                throw new IllegalArgumentException("DTO não pode ser nulo");
            }

            PDI pdi = new PDI();
            pdi.setId(dto.getId());
            pdi.setTitulo(dto.getTitulo());
            pdi.setDescricao(dto.getDescricao());
            pdi.setDtInicio(dto.getDataInicio());
            pdi.setDtFim(dto.getDataFim());
            pdi.setIdUsuario(dto.getIdUsuario());
            pdi.setDestinatario(dto.getDestinatario());

            return pdi;
        } catch (Exception e) {
            logger.error("Erro ao converter DTO para entidade: ", e);
            throw new RuntimeException("Erro ao converter DTO para entidade: " + e.getMessage(), e);
        }
    }

    public void toDTO(pdiDTO dto, PDI pdi) {
        try {
            if (pdi == null) {
                logger.error("Entidade PDI é nula");
                throw new IllegalArgumentException("Entidade PDI não pode ser nula");
            }

            dto.setId(pdi.getId());
            dto.setTitulo(pdi.getTitulo());
            dto.setDescricao(pdi.getDescricao());
            dto.setDataInicio(pdi.getDtInicio());
            dto.setDataFim(pdi.getDtFim());
            dto.setIdUsuario(pdi.getIdUsuario());
            dto.setDestinatario(pdi.getDestinatario());
            dto.setStatus(pdi.getStatus());
        } catch (Exception e) {
            logger.error("Erro ao converter entidade para DTO: ", e);
            throw new RuntimeException("Erro ao converter entidade para DTO: " + e.getMessage(), e);
        }
    }

    public void updateEntityFromDTO(pdiDTO dto, PDI pdi) {
        try {
            if (dto == null || pdi == null) {
                logger.error("DTO ou entidade PDI é nulo");
                throw new IllegalArgumentException("DTO e entidade PDI não podem ser nulos");
            }

            if (dto.getTitulo() != null)
                pdi.setTitulo(dto.getTitulo());
            if (dto.getDescricao() != null)
                pdi.setDescricao(dto.getDescricao());
            if (dto.getDataInicio() != null)
                pdi.setDtInicio(dto.getDataInicio());
            if (dto.getDataFim() != null)
                pdi.setDtFim(dto.getDataFim());
            if (dto.getIdUsuario() != null)
                pdi.setIdUsuario(dto.getIdUsuario());
            if (dto.getDestinatario() != null)
                pdi.setDestinatario(dto.getDestinatario());
            if (dto.getStatus() != null) {
                logger.info("Atualizando status para: {}", dto.getStatus());
                pdi.setStatus(dto.getStatus());
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar entidade a partir do DTO: ", e);
            throw new RuntimeException("Erro ao atualizar entidade a partir do DTO: " + e.getMessage(), e);
        }
    }
}
