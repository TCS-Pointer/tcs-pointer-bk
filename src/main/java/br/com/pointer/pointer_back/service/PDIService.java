package br.com.pointer.pointer_back.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.model.Usuario;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.repository.UsuarioRepository;

@Service
public class PDIService {

    private static final Logger logger = LoggerFactory.getLogger(PDIService.class);

    @Autowired
    private PDIRepository pdiRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PDIService(PDIRepository pdiRepository, ModelMapper modelMapper) {
        this.pdiRepository = pdiRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> buscarPorUsuario(Long idUsuario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdUsuario(idUsuario);
            List<pdiDTO> dtos = pdis.stream()
                    .map(pdi -> modelMapper.map(pdi, pdiDTO.class))
                    .collect(Collectors.toList());
            return ApiResponse.success(dtos, "PDIs do usuário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por usuário: ", e);
            return ApiResponse.badRequest("Erro ao buscar PDIs por usuário: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> buscarPorDestinatario(Long idDestinatario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdDestinatario(idDestinatario);
            List<pdiDTO> dtos = pdis.stream()
                    .map(pdi -> modelMapper.map(pdi, pdiDTO.class))
                    .collect(Collectors.toList());
            return ApiResponse.success(dtos, "PDIs do destinatário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por destinatário: ", e);
            return ApiResponse.badRequest("Erro ao buscar PDIs por destinatário: " + e.getMessage());
        }
    }

    private void validarDatasPDI(pdiDTO dto) {
        if (dto.getDtInicio() != null && dto.getDtFim() != null) {
            if (dto.getDtInicio().isAfter(dto.getDtFim())) {
                throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
            }

            // Validar duração mínima de 1 mês
            long mesesEntreDatas = ChronoUnit.MONTHS.between(
                    dto.getDtInicio(),
                    dto.getDtFim());

            if (mesesEntreDatas < 1) {
                throw new IllegalArgumentException("O PDI deve ter duração mínima de 1 mês");
            }
        }
    }

    private void validarStatusPDI(PDI pdi) {
        if (pdi.getMarcos() != null && !pdi.getMarcos().isEmpty()) {
            boolean todosMarcosConcluidos = pdi.getMarcos().stream()
                    .allMatch(marco -> marco.getStatus() == StatusMarcoPDI.CONCLUIDO);

            if (todosMarcosConcluidos) {
                pdi.setStatus(StatusPDI.CONCLUIDO);
            }
        }
    }

    @Transactional
    public ApiResponse<pdiDTO> criar(pdiDTO dto) {
        try {
            logger.info("Iniciando criação de PDI com dados: {}", dto);

            validarDatasPDI(dto);

            if (dto.getStatus() == null) {
                logger.error("Status do PDI é nulo");
                return ApiResponse.badRequest("Status do PDI é obrigatório");
            }

            try {
                StatusPDI.valueOf(dto.getStatus().name());
            } catch (IllegalArgumentException e) {
                logger.error("Status inválido: {}", dto.getStatus());
                return ApiResponse.badRequest("Status inválido: " + dto.getStatus());
            }

            PDI pdi = modelMapper.map(dto, PDI.class);
            logger.info("PDI convertido para entidade: {}", pdi);

            // Associar destinatário já existente ao PDI
            if (dto.getIdDestinatario() == null) {
                logger.error("ID do destinatário é nulo");
                return ApiResponse.badRequest("ID do destinatário é obrigatório");
            }
            Usuario destinatario = usuarioRepository.findById(dto.getIdDestinatario())
                    .orElseThrow(() -> new RuntimeException(
                            "Destinatário não encontrado com ID: " + dto.getIdDestinatario()));
            pdi.setDestinatario(destinatario);

            // Garantir que cada marco aponte para o PDI correto
            if (pdi.getMarcos() != null) {
                for (MarcoPDI marco : pdi.getMarcos()) {
                    marco.setPdi(pdi);
                }
            }

            validarStatusPDI(pdi);

            PDI salvo = pdiRepository.save(pdi);
            logger.info("PDI salvo com sucesso: {}", salvo);

            return ApiResponse.success(modelMapper.map(salvo, pdiDTO.class), "PDI criado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao criar PDI: ", e);
            return ApiResponse.badRequest("Erro ao criar PDI: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> listarTodos() {
        try {
            List<PDI> pdis = pdiRepository.findAll();
            List<pdiDTO> dtos = pdis.stream()
                    .map(pdi -> modelMapper.map(pdi, pdiDTO.class))
                    .collect(Collectors.toList());
            return ApiResponse.success(dtos, "PDIs listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs: ", e);
            return ApiResponse.badRequest("Erro ao listar PDIs: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<pdiDTO> buscarPorId(Long id) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));
            return ApiResponse.success(modelMapper.map(pdi, pdiDTO.class), "PDI encontrado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDI: ", e);
            return ApiResponse.badRequest("Erro ao buscar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<pdiDTO> atualizar(Long id, pdiDTO dto) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));

            if (dto.getStatus() != null) {
                try {
                    StatusPDI.valueOf(dto.getStatus().name());
                } catch (IllegalArgumentException e) {
                    logger.error("Status inválido: {}", dto.getStatus());
                    return ApiResponse.badRequest("Status inválido: " + dto.getStatus());
                }
            }

            modelMapper.map(dto, pdi);
            PDI atualizado = pdiRepository.save(pdi);
            return ApiResponse.success(modelMapper.map(atualizado, pdiDTO.class), "PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar PDI: ", e);
            return ApiResponse.badRequest("Erro ao atualizar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id) {
        try {
            if (!pdiRepository.existsById(id)) {
                return ApiResponse.badRequest("PDI não encontrado com ID: " + id);
            }
            pdiRepository.deleteById(id);
            return ApiResponse.success(null, "PDI deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar PDI: ", e);
            return ApiResponse.badRequest("Erro ao deletar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<pdiDTO> atualizarStatus(Long id, AtualizarStatusPDIDTO dto) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));

            if (dto.getIdMarco() == null || dto.getStatusMarco() == null) {
                return ApiResponse.badRequest("ID do marco e status são obrigatórios");
            }

            boolean marcoEncontrado = false;
            for (MarcoPDI marco : pdi.getMarcos()) {
                if (marco.getId().equals(dto.getIdMarco())) {
                    marco.setStatus(dto.getStatusMarco());
                    marcoEncontrado = true;
                    break;
                }
            }

            if (!marcoEncontrado) {
                return ApiResponse.badRequest("Marco não encontrado no PDI");
            }

            boolean todosMarcosConcluidos = pdi.getMarcos().stream()
                    .allMatch(marco -> marco.getStatus() == StatusMarcoPDI.CONCLUIDO);

            if (todosMarcosConcluidos) {
                pdi.setStatus(StatusPDI.CONCLUIDO);
            }

            PDI atualizado = pdiRepository.save(pdi);
            return ApiResponse.success(modelMapper.map(atualizado, pdiDTO.class),
                    "Status do PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar status do marco: ", e);
            return ApiResponse.badRequest("Erro ao atualizar status do marco: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> listarTodosComDestinatario() {
        try {
            List<PDI> pdis = pdiRepository.findAllWithDestinatario();
            List<pdiDTO> dtos = pdis.stream()
                    .map(pdi -> modelMapper.map(pdi, pdiDTO.class))
                    .collect(Collectors.toList());
            return ApiResponse.success(dtos, "PDIs com destinatário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs com destinatário: ", e);
            return ApiResponse.badRequest("Erro ao listar PDIs com destinatário: " + e.getMessage());
        }
    }
}