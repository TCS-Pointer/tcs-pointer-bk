package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.pdiDTO;
import br.com.pointer.pointer_back.exception.PDINaoEncontradoException;
import br.com.pointer.pointer_back.mapper.PDIMapper;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.enums.StatusPDI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public pdiDTO criar(pdiDTO dto) {
        try {
            logger.info("Iniciando criação de PDI com dados: {}", dto);

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

            PDI salvo = pdiRepository.save(pdi);
            logger.info("PDI salvo com sucesso: {}", salvo);

            pdiMapper.toDTO(dto, salvo);
            logger.info("PDI convertido de volta para DTO: {}", dto);

            return dto;
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
                    .map(pdi -> {
                        pdiDTO dto = new pdiDTO();
                        pdiMapper.toDTO(dto, pdi);
                        return dto;
                    })
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
            pdiDTO dto = new pdiDTO();
            pdiMapper.toDTO(dto, pdi);
            return dto;
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
            pdiMapper.toDTO(dto, atualizado);
            return dto;
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
}