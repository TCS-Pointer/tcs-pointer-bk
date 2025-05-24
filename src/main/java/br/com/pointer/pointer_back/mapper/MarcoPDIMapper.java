package br.com.pointer.pointer_back.mapper;

import org.springframework.stereotype.Component;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MarcoPDIMapper {
    private static final Logger logger = LoggerFactory.getLogger(MarcoPDIMapper.class);

    public MarcoPDI toEntity(MarcoPDIDTO dto, PDI pdi) {
        try {
            if (dto == null) {
                logger.error("DTO é nulo");
                throw new IllegalArgumentException("DTO não pode ser nulo");
            }

            if (pdi == null) {
                logger.error("PDI é nulo");
                throw new IllegalArgumentException("PDI não pode ser nulo");
            }

            MarcoPDI marcoPDI = new MarcoPDI();
            marcoPDI.setId(dto.getId());
            marcoPDI.setTitulo(dto.getTitulo());
            marcoPDI.setDescricao(dto.getDescricao());
            marcoPDI.setDtFinal(dto.getDtFinal());
            marcoPDI.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusMarcoPDI.AGUARDANDO);
            marcoPDI.setPdi(pdi);

            logger.debug("MarcoPDI convertido para entidade: {}", marcoPDI);
            return marcoPDI;
        } catch (Exception e) {
            logger.error("Erro ao converter DTO para entidade: ", e);
            throw new RuntimeException("Erro ao converter DTO para entidade: " + e.getMessage(), e);
        }
    }

    public void toDTO(MarcoPDIDTO dto, MarcoPDI marcoPDI) {
        try {
            if (marcoPDI == null) {
                logger.error("Entidade MarcoPDI é nula");
                throw new IllegalArgumentException("Entidade MarcoPDI não pode ser nula");
            }

            dto.setId(marcoPDI.getId());
            dto.setTitulo(marcoPDI.getTitulo());
            dto.setDescricao(marcoPDI.getDescricao());
            dto.setDtFinal(marcoPDI.getDtFinal());
            dto.setStatus(marcoPDI.getStatus());
            dto.setPdiId(marcoPDI.getPdi().getId());

            logger.debug("MarcoPDI convertido para DTO: {}", dto);
        } catch (Exception e) {
            logger.error("Erro ao converter entidade para DTO: ", e);
            throw new RuntimeException("Erro ao converter entidade para DTO: " + e.getMessage(), e);
        }
    }

    public void updateEntityFromDTO(MarcoPDIDTO dto, MarcoPDI marcoPDI) {
        try {
            if (dto == null || marcoPDI == null) {
                logger.error("DTO ou entidade MarcoPDI é nulo");
                throw new IllegalArgumentException("DTO e entidade MarcoPDI não podem ser nulos");
            }

            if (dto.getTitulo() != null)
                marcoPDI.setTitulo(dto.getTitulo());
            if (dto.getDescricao() != null)
                marcoPDI.setDescricao(dto.getDescricao());
            if (dto.getDtFinal() != null)
                marcoPDI.setDtFinal(dto.getDtFinal());
            if (dto.getStatus() != null)
                marcoPDI.setStatus(dto.getStatus());

            logger.debug("MarcoPDI atualizado a partir do DTO: {}", marcoPDI);
        } catch (Exception e) {
            logger.error("Erro ao atualizar entidade a partir do DTO: ", e);
            throw new RuntimeException("Erro ao atualizar entidade a partir do DTO: " + e.getMessage(), e);
        }
    }
}