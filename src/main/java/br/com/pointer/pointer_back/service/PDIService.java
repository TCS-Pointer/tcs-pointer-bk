package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.util.ApiResponseUtil;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PDIService {

    private static final Logger logger = LoggerFactory.getLogger(PDIService.class);

    @Autowired
    private PDIRepository pdiRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ApiResponseUtil apiResponseUtil;

    public PDIService(PDIRepository pdiRepository, ModelMapper modelMapper, ApiResponseUtil apiResponseUtil) {
        this.pdiRepository = pdiRepository;
        this.modelMapper = modelMapper;
        this.apiResponseUtil = apiResponseUtil;
    }

    // TODO: Verificar se todos os marcos estão CONCLUIDOS, PDI será CONCLUIDO
    // TODO: Get PDI por ID Usuario (criador) (request param)
    // TODO: Get PDI por ID Destinatário (request param)
    // TODO: Trocar destinatario para idDestinatario
    // TODO: Validação data inicial < data final PDI e Marcos
    // TODO: Validar se todos os marcos estão CONCLUIDOS, PDI será CONCLUIDO
    // TODO: Validar duração mínima de 1 mês

    // TODO: Rota get todas os PDI (somente admin)
    // TODO: Rota get PDI por ID usuario (rota do gestor e admin)
    // TODO: Rota get PDI por ID destinatário (rota do usuario)

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> buscarPorUsuario(Long idUsuario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdUsuario(idUsuario);
            return apiResponseUtil.mapList(pdis, pdiDTO.class, "PDIs do usuário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por usuário: ", e);
            return apiResponseUtil.error("Erro ao buscar PDIs por usuário: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> buscarPorDestinatario(Long idDestinatario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdDestinatario(idDestinatario);
            return apiResponseUtil.mapList(pdis, pdiDTO.class, "PDIs do destinatário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por destinatário: ", e);
            return apiResponseUtil.error("Erro ao buscar PDIs por destinatário: " + e.getMessage(), 400);
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
                return apiResponseUtil.error("Status do PDI é obrigatório", 400);
            }

            try {
                StatusPDI.valueOf(dto.getStatus().name());
            } catch (IllegalArgumentException e) {
                logger.error("Status inválido: {}", dto.getStatus());
                return apiResponseUtil.error("Status inválido: " + dto.getStatus(), 400);
            }

            PDI pdi = modelMapper.map(dto, PDI.class);
            logger.info("PDI convertido para entidade: {}", pdi);

            validarStatusPDI(pdi);

            PDI salvo = pdiRepository.save(pdi);
            logger.info("PDI salvo com sucesso: {}", salvo);

            return apiResponseUtil.created(modelMapper.map(salvo, pdiDTO.class), "PDI criado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao criar PDI: ", e);
            return apiResponseUtil.error("Erro ao criar PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> listarTodos() {
        try {
            List<PDI> pdis = pdiRepository.findAll();
            return apiResponseUtil.mapList(pdis, pdiDTO.class, "PDIs listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs: ", e);
            return apiResponseUtil.error("Erro ao listar PDIs: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<pdiDTO> buscarPorId(Long id) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));
            return apiResponseUtil.map(pdi, pdiDTO.class, "PDI encontrado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar PDI: ", e);
            return apiResponseUtil.error("Erro ao buscar PDI: " + e.getMessage(), 400);
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
                    return apiResponseUtil.error("Status inválido: " + dto.getStatus(), 400);
                }
            }

            modelMapper.map(dto, pdi);
            PDI atualizado = pdiRepository.save(pdi);
            return apiResponseUtil.map(atualizado, pdiDTO.class, "PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar PDI: ", e);
            return apiResponseUtil.error("Erro ao atualizar PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id) {
        try {
            if (!pdiRepository.existsById(id)) {
                return apiResponseUtil.error("PDI não encontrado com ID: " + id, 400);
            }
            pdiRepository.deleteById(id);
            return apiResponseUtil.success(null, "PDI deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar PDI: ", e);
            return apiResponseUtil.error("Erro ao deletar PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional
    public ApiResponse<pdiDTO> atualizarStatus(Long id, AtualizarStatusPDIDTO dto) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));

            if (dto.getIdMarco() == null || dto.getStatusMarco() == null) {
                return apiResponseUtil.error("ID do marco e status são obrigatórios", 400);
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
                return apiResponseUtil.error("Marco não encontrado no PDI", 400);
            }

            boolean todosMarcosConcluidos = pdi.getMarcos().stream()
                    .allMatch(marco -> marco.getStatus() == StatusMarcoPDI.CONCLUIDO);

            if (todosMarcosConcluidos) {
                pdi.setStatus(StatusPDI.CONCLUIDO);
            }

            PDI atualizado = pdiRepository.save(pdi);
            return apiResponseUtil.map(atualizado, pdiDTO.class, "Status do PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar status do marco: ", e);
            return apiResponseUtil.error("Erro ao atualizar status do marco: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<pdiDTO>> listarTodosComDestinatario() {
        try {
            List<PDI> pdis = pdiRepository.findAllWithDestinatario();
            return apiResponseUtil.mapList(pdis, pdiDTO.class, "PDIs com destinatário listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs com destinatário: ", e);
            return apiResponseUtil.error("Erro ao listar PDIs com destinatário: " + e.getMessage(), 400);
        }
    }
}