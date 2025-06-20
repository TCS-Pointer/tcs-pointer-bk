package br.com.pointer.pointer_back.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.exception.ComunicadoNaoEncontradoException;
import br.com.pointer.pointer_back.mapper.ComunicadoMapper;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;
import br.com.pointer.pointer_back.model.Usuario;

@Service
public class ComunicadoService {
    private static final Logger logger = LoggerFactory.getLogger(ComunicadoService.class);

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    @Autowired
    private ComunicadoMapper comunicadoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String getSetorUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get().getSetor();
        }
        // fallback para evitar null pointer
        return null;
    }

    @Transactional(readOnly = true)
    public List<ComunicadoDTO> listarTodos(String keycloakId) {
        try {
            logger.info("Iniciando listagem de todos os comunicados para o usuário com keycloakId: {}", keycloakId);
            Optional<Usuario> usuarioOpt = usuarioRepository.findByKeycloakId(keycloakId);
            if (usuarioOpt.isEmpty()) {
                throw new SecurityException("Usuário não encontrado");
            }
            Usuario usuario = usuarioOpt.get();
            boolean isAdmin = usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equalsIgnoreCase("admin");
            boolean isGestor = usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equalsIgnoreCase("gestor");
            String setor = usuario.getSetor();
            List<ComunicadoDTO> comunicados;
            if (isAdmin) {
                comunicados = comunicadoRepository.findAll().stream()
                        .map(comunicadoMapper::toDTO)
                        .collect(Collectors.toList());
            } else {
                comunicados = comunicadoRepository.findBySetor(setor).stream()
                        .filter(comunicado -> !comunicado.isApenasGestores() || isGestor)
                        .map(comunicadoMapper::toDTO)
                        .collect(Collectors.toList());
            }
            logger.info("Comunicados listados com sucesso: {}", comunicados.size());
            return comunicados;
        } catch (Exception e) {
            logger.error("Erro ao listar comunicados: ", e);
            throw new RuntimeException("Erro ao listar comunicados: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<ComunicadoDTO> listarPorSetor(String setor) {
        try {
            logger.info("Iniciando listagem de comunicados do setor: {}", setor);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            boolean isGestor = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_gestor"));
            String setorUsuario = getSetorUsuarioAutenticado();

            if (!isAdmin && !setor.equals(setorUsuario)) {
                logger.error("Tentativa de acesso não autorizado ao setor: {}", setor);
                throw new SecurityException("Usuário não tem permissão para acessar comunicados de outro setor");
            }

            List<ComunicadoDTO> comunicados = comunicadoRepository.findBySetor(setor).stream()
                    .filter(comunicado -> !comunicado.isApenasGestores() || isGestor)
                    .map(comunicadoMapper::toDTO)
                    .collect(Collectors.toList());
            
            logger.info("Comunicados do setor {} listados com sucesso: {}", setor, comunicados.size());
            return comunicados;
        } catch (SecurityException e) {
            logger.error("Erro de segurança ao listar comunicados: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar comunicados do setor: ", e);
            throw new RuntimeException("Erro ao listar comunicados do setor: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<ComunicadoDTO> listarApenasGestores(String keycloakId) {
        try {
            logger.info("Iniciando listagem de comunicados de gestores para o usuário com keycloakId: {}", keycloakId);

            Optional<Usuario> usuarioOpt = usuarioRepository.findByKeycloakId(keycloakId);
            if (usuarioOpt.isEmpty()) {
                throw new SecurityException("Usuário não encontrado");
            }

            Usuario usuario = usuarioOpt.get();
            boolean isAdmin = usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equalsIgnoreCase("admin");

            List<Comunicado> comunicados;
            if (isAdmin) {
                comunicados = comunicadoRepository.findByApenasGestores(true);
            } else {
                String setor = usuario.getSetor();
                comunicados = comunicadoRepository.findBySetorAndApenasGestores(setor, true);
            }

            return comunicados.stream()
                    .map(comunicadoMapper::toDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Erro ao listar comunicados de gestores: ", e);
            throw new RuntimeException("Erro ao listar comunicados de gestores: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ComunicadoDTO buscarPorId(Long id, String keycloakId) {
        try {
            logger.info("Buscando comunicado com ID: {} para o usuário com keycloakId: {}", id, keycloakId);
            Optional<Usuario> usuarioOpt = usuarioRepository.findByKeycloakId(keycloakId);
            if (usuarioOpt.isEmpty()) {
                throw new SecurityException("Usuário não encontrado");
            }
            Usuario usuario = usuarioOpt.get();
            boolean isAdmin = usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equalsIgnoreCase("admin");
            boolean isGestor = usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equalsIgnoreCase("gestor");
            String setorUsuario = usuario.getSetor();
            Comunicado comunicado = comunicadoRepository.findById(id)
                    .orElseThrow(() -> new ComunicadoNaoEncontradoException("Comunicado não encontrado com ID: " + id));
            if (!isAdmin && !comunicado.getSetor().equalsIgnoreCase(setorUsuario)) {
                logger.error("Tentativa de acesso não autorizado ao comunicado: {}", id);
                throw new SecurityException("Usuário não tem permissão para acessar comunicados de outro setor");
            }
            if (comunicado.isApenasGestores() && !isGestor && !isAdmin) {
                logger.error("Tentativa de acesso não autorizado ao comunicado restrito: {}", id);
                throw new SecurityException("Apenas gestores podem visualizar este comunicado");
            }
            ComunicadoDTO dto = comunicadoMapper.toDTO(comunicado);
            logger.info("Comunicado encontrado com sucesso: {}", dto);
            return dto;
        } catch (ComunicadoNaoEncontradoException | SecurityException e) {
            logger.error("Erro ao buscar comunicado: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao buscar comunicado: ", e);
            throw new RuntimeException("Erro ao buscar comunicado: " + e.getMessage());
        }
    }

    @Transactional
    public ComunicadoDTO criar(ComunicadoDTO comunicadoDTO) {
        try {
            logger.info("Iniciando criação de comunicado: {}", comunicadoDTO);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            String setorUsuario = getSetorUsuarioAutenticado();

            if (!isAdmin && !comunicadoDTO.getSetor().equals(setorUsuario)) {
                logger.error("Tentativa de criação não autorizada para o setor: {}", comunicadoDTO.getSetor());
                throw new SecurityException("Usuário não tem permissão para criar comunicados para outro setor");
            }

            Comunicado comunicado = comunicadoMapper.toEntity(comunicadoDTO);
            Comunicado salvo = comunicadoRepository.save(comunicado);
            ComunicadoDTO dto = comunicadoMapper.toDTO(salvo);
            
            logger.info("Comunicado criado com sucesso: {}", dto);
            return dto;
        } catch (SecurityException e) {
            logger.error("Erro de segurança ao criar comunicado: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao criar comunicado: ", e);
            throw new RuntimeException("Erro ao criar comunicado: " + e.getMessage());
        }
    }

    @Transactional
    public ComunicadoDTO atualizar(Long id, ComunicadoDTO comunicadoDTO) {
        try {
            logger.info("Iniciando atualização do comunicado {}: {}", id, comunicadoDTO);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            String setorUsuario = getSetorUsuarioAutenticado();

            Comunicado comunicadoExistente = comunicadoRepository.findById(id)
                    .orElseThrow(() -> new ComunicadoNaoEncontradoException("Comunicado não encontrado com ID: " + id));

            if (!isAdmin && !comunicadoExistente.getSetor().equals(setorUsuario)) {
                logger.error("Tentativa de atualização não autorizada do comunicado: {}", id);
                throw new SecurityException("Usuário não tem permissão para atualizar comunicados de outro setor");
            }

            if (!isAdmin && !comunicadoDTO.getSetor().equals(setorUsuario)) {
                logger.error("Tentativa de mover comunicado para outro setor: {}", comunicadoDTO.getSetor());
                throw new SecurityException("Usuário não tem permissão para mover comunicado para outro setor");
            }

            Comunicado comunicadoAtualizado = comunicadoMapper.toEntity(comunicadoDTO);
            comunicadoExistente.setTitulo(comunicadoAtualizado.getTitulo());
            comunicadoExistente.setDescricao(comunicadoAtualizado.getDescricao());
            comunicadoExistente.setSetor(comunicadoAtualizado.getSetor());
            comunicadoExistente.setApenasGestores(comunicadoAtualizado.isApenasGestores());
            
            Comunicado salvo = comunicadoRepository.save(comunicadoExistente);
            ComunicadoDTO dto = comunicadoMapper.toDTO(salvo);
            
            logger.info("Comunicado atualizado com sucesso: {}", dto);
            return dto;
        } catch (ComunicadoNaoEncontradoException | SecurityException e) {
            logger.error("Erro ao atualizar comunicado: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar comunicado: ", e);
            throw new RuntimeException("Erro ao atualizar comunicado: " + e.getMessage());
        }
    }

    @Transactional
    public void deletar(Long id) {
        try {
            logger.info("Iniciando deleção do comunicado: {}", id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
            String setorUsuario = getSetorUsuarioAutenticado();

            Comunicado comunicado = comunicadoRepository.findById(id)
                    .orElseThrow(() -> new ComunicadoNaoEncontradoException("Comunicado não encontrado com ID: " + id));

            if (!isAdmin && !comunicado.getSetor().equals(setorUsuario)) {
                logger.error("Tentativa de deleção não autorizada do comunicado: {}", id);
                throw new SecurityException("Usuário não tem permissão para deletar comunicados de outro setor");
            }

            comunicadoRepository.deleteById(id);
            logger.info("Comunicado deletado com sucesso: {}", id);
        } catch (ComunicadoNaoEncontradoException | SecurityException e) {
            logger.error("Erro ao deletar comunicado: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar comunicado: ", e);
            throw new RuntimeException("Erro ao deletar comunicado: " + e.getMessage());
        }
    }
}
