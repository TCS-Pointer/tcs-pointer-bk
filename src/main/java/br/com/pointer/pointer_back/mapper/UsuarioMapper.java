package br.com.pointer.pointer_back.mapper;

import br.com.pointer.pointer_back.dto.UsuarioDTO;
import br.com.pointer.pointer_back.dto.UsuarioResponseDTO;
import br.com.pointer.pointer_back.model.Usuario;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UsuarioMapper extends GenericMapperImpl<UsuarioDTO, Usuario, UsuarioResponseDTO> {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioMapper.class);

    @Override
    public Usuario toEntity(UsuarioDTO dto) {
        try {
            if (dto == null) {
                logger.error("DTO é nulo");
                return null;
            }
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setSetor(dto.getSetor());
            usuario.setCargo(dto.getCargo());
            usuario.setTipoUsuario(dto.getTipoUsuario());
            usuario.setKeycloakId(dto.getKeycloakId());
            return usuario;
        } catch (Exception e) {
            logger.error("Erro ao converter DTO para entidade: ", e);
            throw new RuntimeException("Erro ao converter DTO para entidade: " + e.getMessage());
        }
    }

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        try {
            if (usuario == null) {
                logger.error("Usuário é nulo");
                return null;
            }
            UsuarioResponseDTO dto = new UsuarioResponseDTO();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setSetor(usuario.getSetor());
            dto.setCargo(usuario.getCargo());
            dto.setTipoUsuario(usuario.getTipoUsuario());
            dto.setDataCriacao(usuario.getDataCriacao());
            dto.setKeycloakId(usuario.getKeycloakId());
            return dto;
        } catch (Exception e) {
            logger.error("Erro ao converter entidade para DTO: ", e);
            throw new RuntimeException("Erro ao converter entidade para DTO: " + e.getMessage());
        }
    }

    @Override
    public void updateEntityFromDTO(UsuarioDTO dto, Usuario usuario) {
        try {
            if (dto == null || usuario == null) {
                logger.error("DTO ou Usuário é nulo");
                return;
            }
            usuario.setNome(dto.getNome());
            usuario.setSetor(dto.getSetor());
            usuario.setCargo(dto.getCargo());
            usuario.setTipoUsuario(dto.getTipoUsuario());
            usuario.setKeycloakId(dto.getKeycloakId());
        } catch (Exception e) {
            logger.error("Erro ao atualizar entidade com DTO: ", e);
            throw new RuntimeException("Erro ao atualizar entidade com DTO: " + e.getMessage());
        }
    }
}
