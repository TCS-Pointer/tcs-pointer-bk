package br.com.pointer.pointer_back.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.ComunicadoDTO;
import br.com.pointer.pointer_back.dto.ComunicadoResponseDTO;
import br.com.pointer.pointer_back.model.Comunicado;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.ComunicadoLeituraRepository;
import br.com.pointer.pointer_back.repository.ComunicadoRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@Service
public class ComunicadoService {
    private static final Logger logger = LoggerFactory.getLogger(ComunicadoService.class);

    @Autowired
    private ComunicadoRepository comunicadoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComunicadoLeituraRepository comunicadoLeituraRepository;

    @Transactional(readOnly = true)
    public ApiResponse<Page<ComunicadoResponseDTO>> listarTodos(
            String keycloakId,
            int page,
            int size,
            String titulo) {
        
        try {
            // Buscar usuário uma única vez
            Usuario usuario = usuarioRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            // Criar PageRequest com ordenação
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dataPublicacao").descending());
            
            Page<Comunicado> comunicados;
            
            // Lógica baseada no tipo de usuário
            switch (usuario.getTipoUsuario()) {
                case "ADMIN":
                    // ADMIN: Todos os comunicados (sem filtro de setor)
                    comunicados = comunicadoRepository.findByFilters(null, titulo, null, pageRequest);
                    break;
                    
                case "GESTOR":
                    // GESTOR: Setor do usuário + apenasGestores = true
                    comunicados = comunicadoRepository.findByFilters(usuario.getSetor(), titulo, true, pageRequest);
                    break;
                    
                default:
                    // COLABORADOR: Setor do usuário + apenasGestores = false
                    comunicados = comunicadoRepository.findByFilters(usuario.getSetor(), titulo, false, pageRequest);
                    break;
            }
            
            // Mapear para DTO com informações adicionais
            Page<ComunicadoResponseDTO> pageDTO = comunicados.map(comunicado -> {
                ComunicadoResponseDTO dto = modelMapper.map(comunicado, ComunicadoResponseDTO.class);
                
                // Adicionar quantidade de leitores
                int leitores = comunicadoLeituraRepository.countByComunicadoId(comunicado.getId());
                dto.setQuantidadeLeitores(leitores);
                
                // Verificar se o usuário já leu
                boolean lido = comunicadoLeituraRepository.existsByComunicadoIdAndUsuarioId(
                    comunicado.getId(), usuario.getId());
                dto.setLido(lido);
                
                return dto;
            });
            
            return ApiResponse.success(pageDTO, "Comunicados listados com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro ao listar comunicados: ", e);
            return ApiResponse.badRequest("Erro ao listar comunicados: " + e.getMessage());
        }
    }
    

    @Transactional
    public ApiResponse<ComunicadoDTO> criar(ComunicadoDTO comunicadoDTO) {
        try {
            Comunicado comunicado = modelMapper.map(comunicadoDTO, Comunicado.class);
            comunicado.setAtivo(true);
            comunicado = comunicadoRepository.save(comunicado);
            
            ComunicadoDTO dto = modelMapper.map(comunicado, ComunicadoDTO.class);
            logger.info("Comunicado criado com sucesso: {}", comunicado.getId());
            
            return ApiResponse.success(dto, "Comunicado criado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao criar comunicado: ", e);
            return ApiResponse.badRequest("Erro ao criar comunicado: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id) {
        try {
            Comunicado comunicado = comunicadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comunicado não encontrado"));
            
            comunicado.setAtivo(false);
            comunicadoRepository.save(comunicado);
            
            return ApiResponse.success(null, "Comunicado deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar comunicado: ", e);
            return ApiResponse.badRequest("Erro ao deletar comunicado: " + e.getMessage());
        }
    }
}
