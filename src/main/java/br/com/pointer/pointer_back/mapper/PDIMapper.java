package br.com.pointer.pointer_back.mapper;

import org.springframework.stereotype.Component;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.enums.StatusPDI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import br.com.pointer.pointer_back.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Component
public class PDIMapper {
    private static final Logger logger = LoggerFactory.getLogger(PDIMapper.class);

    @Autowired
    private MarcoPDIMapper marcoPDIMapper;

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
            pdi.setStatus(dto.getStatus());

            // Mapeando o usuário que criou o PDI
            if (dto.getIdUsuario() != null) {
                Usuario usuario = new Usuario();
                usuario.setId(dto.getIdUsuario());
                pdi.setUsuario(usuario);
            }

            // Mapeando o destinatário
            if (dto.getIdDestinatario() != null) {
                Usuario destinatario = new Usuario();
                destinatario.setId(dto.getIdDestinatario());
                pdi.setDestinatario(destinatario);
            }

            // Mapeando os marcos
            if (dto.getMarcos() != null && !dto.getMarcos().isEmpty()) {
                pdi.setMarcos(dto.getMarcos().stream()
                        .map(marcoDTO -> {
                            MarcoPDI marco = new MarcoPDI();
                            marco.setId(marcoDTO.getId());
                            marco.setTitulo(marcoDTO.getTitulo());
                            marco.setDescricao(marcoDTO.getDescricao());
                            marco.setDtFinal(marcoDTO.getDtFinal());
                            marco.setStatus(marcoDTO.getStatus());
                            marco.setPdi(pdi);
                            return marco;
                        })
                        .collect(Collectors.toList()));
            }

            return pdi;
        } catch (Exception e) {
            logger.error("Erro ao converter DTO para entidade: ", e);
            throw new RuntimeException("Erro ao converter DTO para entidade: " + e.getMessage(), e);
        }
    }

    public pdiDTO toDTO(PDI pdi) {
        try {
            if (pdi == null) {
                logger.error("Entidade PDI é nula");
                throw new IllegalArgumentException("Entidade PDI não pode ser nula");
            }

            pdiDTO dto = new pdiDTO();
            dto.setId(pdi.getId());
            dto.setTitulo(pdi.getTitulo());
            dto.setDescricao(pdi.getDescricao());
            dto.setDataInicio(pdi.getDtInicio());
            dto.setDataFim(pdi.getDtFim());
            dto.setIdUsuario(pdi.getIdUsuario());
            dto.setStatus(pdi.getStatus());

            // Mapeando o usuário que criou o PDI
            if (pdi.getUsuario() != null) {
                UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO();
                usuarioDTO.setId(pdi.getUsuario().getId());
                usuarioDTO.setNome(pdi.getUsuario().getNome());
                usuarioDTO.setEmail(pdi.getUsuario().getEmail());
                usuarioDTO.setSetor(pdi.getUsuario().getSetor());
                usuarioDTO.setCargo(pdi.getUsuario().getCargo());
                usuarioDTO.setTipoUsuario(pdi.getUsuario().getTipoUsuario());
                usuarioDTO.setStatus(pdi.getUsuario().getStatus());
                dto.setUsuario(usuarioDTO);
            }

            // Mapeando o destinatário
            if (pdi.getDestinatario() != null) {
                dto.setIdDestinatario(pdi.getDestinatario().getId());
                UsuarioResponseDTO destinatarioDTO = new UsuarioResponseDTO();
                destinatarioDTO.setId(pdi.getDestinatario().getId());
                destinatarioDTO.setNome(pdi.getDestinatario().getNome());
                destinatarioDTO.setEmail(pdi.getDestinatario().getEmail());
                destinatarioDTO.setSetor(pdi.getDestinatario().getSetor());
                destinatarioDTO.setCargo(pdi.getDestinatario().getCargo());
                destinatarioDTO.setTipoUsuario(pdi.getDestinatario().getTipoUsuario());
                destinatarioDTO.setStatus(pdi.getDestinatario().getStatus());
                dto.setDestinatario(destinatarioDTO);
            }

            // Convertendo os marcos do PDI
            if (pdi.getMarcos() != null) {
                dto.setMarcos(new ArrayList<>());
                pdi.getMarcos().forEach(marco -> {
                    MarcoPDIDTO marcoDTO = new MarcoPDIDTO();
                    marcoDTO.setId(marco.getId());
                    marcoDTO.setTitulo(marco.getTitulo());
                    marcoDTO.setDescricao(marco.getDescricao());
                    marcoDTO.setDtFinal(marco.getDtFinal());
                    marcoDTO.setStatus(marco.getStatus());
                    marcoDTO.setPdiId(pdi.getId());
                    dto.getMarcos().add(marcoDTO);
                });
                logger.info("Marcos mapeados: {}", dto.getMarcos());
            } else {
                logger.info("PDI sem marcos");
            }

            return dto;
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
            if (dto.getIdUsuario() != null) {
                Usuario usuario = new Usuario();
                usuario.setId(dto.getIdUsuario());
                pdi.setUsuario(usuario);
            }
            if (dto.getStatus() != null) {
                logger.info("Atualizando status para: {}", dto.getStatus());
                pdi.setStatus(dto.getStatus());
            }

            // Atualizando o destinatário
            if (dto.getIdDestinatario() != null) {
                Usuario destinatario = new Usuario();
                destinatario.setId(dto.getIdDestinatario());
                pdi.setDestinatario(destinatario);
            }

            // Atualizando os marcos
            if (dto.getMarcos() != null && !dto.getMarcos().isEmpty()) {
                pdi.setMarcos(dto.getMarcos().stream()
                        .map(marcoDTO -> {
                            MarcoPDI marco = new MarcoPDI();
                            marco.setId(marcoDTO.getId());
                            marco.setTitulo(marcoDTO.getTitulo());
                            marco.setDescricao(marcoDTO.getDescricao());
                            marco.setDtFinal(marcoDTO.getDtFinal());
                            marco.setStatus(marcoDTO.getStatus());
                            marco.setPdi(pdi);
                            return marco;
                        })
                        .collect(Collectors.toList()));
                logger.info("Marcos atualizados: {}", pdi.getMarcos());
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar entidade a partir do DTO: ", e);
            throw new RuntimeException("Erro ao atualizar entidade a partir do DTO: " + e.getMessage(), e);
        }
    }
}
