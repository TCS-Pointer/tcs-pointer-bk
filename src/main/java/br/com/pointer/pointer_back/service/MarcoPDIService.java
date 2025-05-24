package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.exception.MarcoPDINaoEncontradoException;
import br.com.pointer.pointer_back.mapper.MarcoPDIMapper;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.MarcoPDIRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcoPDIService {

    private static final Logger logger = LoggerFactory.getLogger(MarcoPDIService.class);

    private final MarcoPDIRepository marcoPDIRepository;
    private final PDIRepository pdiRepository;
    private final MarcoPDIMapper marcoPDIMapper;

    @Autowired
    public MarcoPDIService(MarcoPDIRepository marcoPDIRepository, PDIRepository pdiRepository,
            MarcoPDIMapper marcoPDIMapper) {
        this.marcoPDIRepository = marcoPDIRepository;
        this.pdiRepository = pdiRepository;
        this.marcoPDIMapper = marcoPDIMapper;
    }

    @Transactional
    public MarcoPDIDTO criar(MarcoPDIDTO dto) {
        try {
            logger.info("Iniciando criação de Marco PDI com dados: {}", dto);

            if (dto.getPdiId() == null) {
                throw new IllegalArgumentException("ID do PDI é obrigatório");
            }

            PDI pdi = pdiRepository.findById(dto.getPdiId())
                    .orElseThrow(() -> new IllegalArgumentException("PDI não encontrado com ID: " + dto.getPdiId()));

            MarcoPDI marcoPDI = marcoPDIMapper.toEntity(dto, pdi);
            logger.info("Marco PDI convertido para entidade: {}", marcoPDI);

            MarcoPDI salvo = marcoPDIRepository.save(marcoPDI);
            logger.info("Marco PDI salvo com sucesso: {}", salvo);

            marcoPDIMapper.toDTO(dto, salvo);
            logger.info("Marco PDI convertido de volta para DTO: {}", dto);

            return dto;
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao criar Marco PDI: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao criar Marco PDI: ", e);
            throw new RuntimeException("Erro ao criar Marco PDI: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<MarcoPDIDTO> listarPorPDI(Long pdiId) {
        try {
            List<MarcoPDI> marcos = marcoPDIRepository.findByPdiId(pdiId);
            return marcos.stream()
                    .map(marco -> {
                        MarcoPDIDTO dto = new MarcoPDIDTO();
                        marcoPDIMapper.toDTO(dto, marco);
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar Marcos PDI: ", e);
            throw new RuntimeException("Erro ao listar Marcos PDI: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public MarcoPDIDTO buscarPorId(Long id) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));
            MarcoPDIDTO dto = new MarcoPDIDTO();
            marcoPDIMapper.toDTO(dto, marcoPDI);
            return dto;
        } catch (Exception e) {
            logger.error("Erro ao buscar Marco PDI: ", e);
            throw new RuntimeException("Erro ao buscar Marco PDI: " + e.getMessage());
        }
    }

    @Transactional
    public MarcoPDIDTO atualizar(Long id, MarcoPDIDTO dto) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));

            marcoPDIMapper.updateEntityFromDTO(dto, marcoPDI);
            MarcoPDI atualizado = marcoPDIRepository.save(marcoPDI);
            marcoPDIMapper.toDTO(dto, atualizado);
            return dto;
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao atualizar Marco PDI: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar Marco PDI: ", e);
            throw new RuntimeException("Erro ao atualizar Marco PDI: " + e.getMessage());
        }
    }

    @Transactional
    public void deletar(Long id) {
        try {
            if (!marcoPDIRepository.existsById(id)) {
                throw new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id);
            }
            marcoPDIRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Erro ao deletar Marco PDI: ", e);
            throw new RuntimeException("Erro ao deletar Marco PDI: " + e.getMessage());
        }
    }
}