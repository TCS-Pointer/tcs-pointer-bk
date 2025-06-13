package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.dto.AtualizarStatusPDIDTO;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.mapper.PDIMapper;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.enums.StatusPDI;
import br.com.pointer.pointer_back.enums.StatusMarcoPDI;
import br.com.pointer.pointer_back.model.MarcoPDI;

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
    private final PDIMapper pdiMapper;

    public PDIService(PDIRepository pdiRepository, PDIMapper pdiMapper) {
        this.pdiRepository = pdiRepository;
        this.pdiMapper = pdiMapper;
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
    public List<pdiDTO> buscarPorUsuario(Long idUsuario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdUsuario(idUsuario);
            return pdis.stream()
                    .map(pdiMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por usuário: ", e);
            throw new RuntimeException("Erro ao buscar PDIs por usuário: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<pdiDTO> buscarPorDestinatario(Long idDestinatario) {
        try {
            List<PDI> pdis = pdiRepository.findByIdDestinatario(idDestinatario);
            return pdis.stream()
                    .map(pdiMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao buscar PDIs por destinatário: ", e);
            throw new RuntimeException("Erro ao buscar PDIs por destinatário: " + e.getMessage());
        }
    }

    private void validarDatasPDI(pdiDTO dto) {
        if (dto.getDataInicio() != null && dto.getDataFim() != null) {
            if (dto.getDataInicio().isAfter(dto.getDataFim())) {
                throw new IllegalArgumentException("Data inicial não pode ser posterior à data final");
            }

            // Validar duração mínima de 1 mês
            long mesesEntreDatas = ChronoUnit.MONTHS.between(
                    dto.getDataInicio(),
                    dto.getDataFim());

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
    public pdiDTO criar(pdiDTO dto) {
        try {
            logger.info("Iniciando criação de PDI com dados: {}", dto);

            validarDatasPDI(dto);

            if (dto.getStatus() == null) {
                logger.error("Status do PDI é nulo");
                throw new IllegalArgumentException("Status do PDI é obrigatório");
            }

            try {
                StatusPDI.valueOf(dto.getStatus().name());
            } catch (IllegalArgumentException e) {
                logger.error("Status inválido: {}", dto.getStatus());
                throw new IllegalArgumentException("Status inválido: " + dto.getStatus());
            }

            PDI pdi = pdiMapper.toEntity(dto);
            logger.info("PDI convertido para entidade: {}", pdi);

            validarStatusPDI(pdi);

            PDI salvo = pdiRepository.save(pdi);
            logger.info("PDI salvo com sucesso: {}", salvo);

            return pdiMapper.toDTO(salvo);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar PDI: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao criar PDI: ", e);
            throw new RuntimeException("Erro ao criar PDI: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<pdiDTO> listarTodos() {
        try {
            List<PDI> pdis = pdiRepository.findAll();
            return pdis.stream()
                    .map(pdiMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs: ", e);
            throw new RuntimeException("Erro ao listar PDIs: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public pdiDTO buscarPorId(Long id) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));
            return pdiMapper.toDTO(pdi);
        } catch (Exception e) {
            logger.error("Erro ao buscar PDI: ", e);
            throw new RuntimeException("Erro ao buscar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public pdiDTO atualizar(Long id, pdiDTO dto) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));

            if (dto.getStatus() != null) {
                try {
                    StatusPDI.valueOf(dto.getStatus().name());
                } catch (IllegalArgumentException e) {
                    logger.error("Status inválido: {}", dto.getStatus());
                    throw new IllegalArgumentException("Status inválido: " + dto.getStatus());
                }
            }

            pdiMapper.updateEntityFromDTO(dto, pdi);
            PDI atualizado = pdiRepository.save(pdi);
            return pdiMapper.toDTO(atualizado);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar PDI: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar PDI: ", e);
            throw new RuntimeException("Erro ao atualizar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public void deletar(Long id) {
        try {
            if (!pdiRepository.existsById(id)) {
                throw new PDINaoEncontradoException("PDI não encontrado com ID: " + id);
            }
            pdiRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Erro ao deletar PDI: ", e);
            throw new RuntimeException("Erro ao deletar PDI: " + e.getMessage());
        }
    }

    @Transactional
    public pdiDTO atualizarStatus(Long id, AtualizarStatusPDIDTO dto) {
        try {
            PDI pdi = pdiRepository.findById(id)
                    .orElseThrow(() -> new PDINaoEncontradoException("PDI não encontrado com ID: " + id));

            // Atualiza o status do marco
            if (dto.getIdMarco() == null || dto.getStatusMarco() == null) {
                throw new IllegalArgumentException("ID do marco e status são obrigatórios");
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
                throw new IllegalArgumentException("Marco não encontrado no PDI");
            }

            // Verifica se todos os marcos estão concluídos
            boolean todosMarcosConcluidos = pdi.getMarcos().stream()
                    .allMatch(marco -> marco.getStatus() == StatusMarcoPDI.CONCLUIDO);

            // Se todos os marcos estiverem concluídos, atualiza o status do PDI
            if (todosMarcosConcluidos) {
                pdi.setStatus(StatusPDI.CONCLUIDO);
            }

            PDI atualizado = pdiRepository.save(pdi);
            return pdiMapper.toDTO(atualizado);
        } catch (Exception e) {
            logger.error("Erro ao atualizar status do marco: ", e);
            throw new RuntimeException("Erro ao atualizar status do marco: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<pdiDTO> listarTodosComDestinatario() {
        try {
            List<PDI> pdis = pdiRepository.findAllWithDestinatario();
            return pdis.stream()
                    .map(pdiMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar PDIs com destinatário: ", e);
            throw new RuntimeException("Erro ao listar PDIs com destinatário: " + e.getMessage());
        }
    }
}