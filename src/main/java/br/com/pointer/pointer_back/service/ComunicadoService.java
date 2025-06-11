package br.com.pointer.pointer_back.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.mapper.ComunicadoMapper;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ComunicadoService {

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    @Autowired
    private ComunicadoMapper comunicadoMapper;

    public List<ComunicadoDTO> listarTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        
        if (isAdmin) {
            return comunicadoRepository.findAll().stream()
                    .map(comunicadoMapper::toDTO)
                    .collect(Collectors.toList());
        }

        String setor = auth.getName(); // Assumindo que o setor está no nome do usuário
        boolean isGestor = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_gestor"));

        return comunicadoRepository.findBySetor(setor).stream()
                .filter(comunicado -> !comunicado.isApenasGestores() || isGestor)
                .map(comunicadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ComunicadoDTO> listarPorSetor(String setor) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        boolean isGestor = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_gestor"));
        String setorUsuario = auth.getName();

        if (!isAdmin && !setor.equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para acessar comunicados de outro setor");
        }

        return comunicadoRepository.findBySetor(setor).stream()
                .filter(comunicado -> !comunicado.isApenasGestores() || isGestor)
                .map(comunicadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ComunicadoDTO buscarPorId(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        boolean isGestor = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_gestor"));
        String setorUsuario = auth.getName();

        Optional<Comunicado> comunicado = comunicadoRepository.findById(id);
        if (comunicado.isEmpty()) {
            throw new EntityNotFoundException("Comunicado não encontrado com ID: " + id);
        }

        Comunicado comunicadoEncontrado = comunicado.get();
        if (!isAdmin && !comunicadoEncontrado.getSetor().equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para acessar comunicados de outro setor");
        }

        if (comunicadoEncontrado.isApenasGestores() && !isGestor && !isAdmin) {
            throw new SecurityException("Apenas gestores podem visualizar este comunicado");
        }

        return comunicadoMapper.toDTO(comunicadoEncontrado);
    }

    public ComunicadoDTO criar(ComunicadoDTO comunicadoDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        String setorUsuario = auth.getName();

        if (!isAdmin && !comunicadoDTO.getSetor().equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para criar comunicados para outro setor");
        }

        Comunicado comunicado = comunicadoMapper.toEntity(comunicadoDTO);
        return comunicadoMapper.toDTO(comunicadoRepository.save(comunicado));
    }

    public ComunicadoDTO atualizar(Long id, ComunicadoDTO comunicadoDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        String setorUsuario = auth.getName();

        Comunicado comunicadoExistente = comunicadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comunicado não encontrado com ID: " + id));

        if (!isAdmin && !comunicadoExistente.getSetor().equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para atualizar comunicados de outro setor");
        }

        if (!isAdmin && !comunicadoDTO.getSetor().equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para mover comunicado para outro setor");
        }

        Comunicado comunicadoAtualizado = comunicadoMapper.toEntity(comunicadoDTO);
        comunicadoExistente.setTitulo(comunicadoAtualizado.getTitulo());
        comunicadoExistente.setDescricao(comunicadoAtualizado.getDescricao());
        comunicadoExistente.setSetor(comunicadoAtualizado.getSetor());
        comunicadoExistente.setApenasGestores(comunicadoAtualizado.isApenasGestores());
        
        return comunicadoMapper.toDTO(comunicadoRepository.save(comunicadoExistente));
    }

    public void deletar(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));
        String setorUsuario = auth.getName();

        Comunicado comunicado = comunicadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comunicado não encontrado com ID: " + id));

        if (!isAdmin && !comunicado.getSetor().equals(setorUsuario)) {
            throw new SecurityException("Usuário não tem permissão para deletar comunicados de outro setor");
        }

        comunicadoRepository.deleteById(id);
    }
}
