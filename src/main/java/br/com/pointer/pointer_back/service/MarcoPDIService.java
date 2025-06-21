package br.com.pointer.pointer_back.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.exception.MarcoPDINaoEncontradoException;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.MarcoPDIRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;

@Service
public class MarcoPDIService {

    private static final Logger logger = LoggerFactory.getLogger(MarcoPDIService.class);

    private final MarcoPDIRepository marcoPDIRepository;
    private final PDIRepository pdiRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MarcoPDIService(MarcoPDIRepository marcoPDIRepository, PDIRepository pdiRepository,
            ModelMapper modelMapper) {
        this.marcoPDIRepository = marcoPDIRepository;
        this.pdiRepository = pdiRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ApiResponse<MarcoPDIDTO> criar(MarcoPDIDTO dto) {
        try {
            logger.info("Iniciando criação de Marco PDI com dados: {}", dto);

            if (dto.getPdiId() == null) {
                return ApiResponse.badRequest("ID do PDI é obrigatório");
            }

            PDI pdi = pdiRepository.findById(dto.getPdiId())
                    .orElseThrow(() -> new IllegalArgumentException("PDI não encontrado com ID: " + dto.getPdiId()));

            MarcoPDI marcoPDI = modelMapper.map(dto, MarcoPDI.class);
            marcoPDI.setPdi(pdi);
            logger.info("Marco PDI convertido para entidade: {}", marcoPDI);

            MarcoPDI salvo = marcoPDIRepository.save(marcoPDI);
            logger.info("Marco PDI salvo com sucesso: {}", salvo);

            return ApiResponse.success(modelMapper.map(salvo, MarcoPDIDTO.class), "Marco PDI criado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao criar Marco PDI: ", e);
            return ApiResponse.badRequest("Erro ao criar Marco PDI: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<MarcoPDIDTO>> listarPorPDI(Long pdiId) {
        try {
            List<MarcoPDI> marcos = marcoPDIRepository.findByPdiId(pdiId);
            List<MarcoPDIDTO> dtos = marcos.stream()
                    .map(marco -> modelMapper.map(marco, MarcoPDIDTO.class))
                    .collect(Collectors.toList());
            return ApiResponse.success(dtos, "Marcos PDI listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar Marcos PDI: ", e);
            return ApiResponse.badRequest("Erro ao listar Marcos PDI: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<MarcoPDIDTO> buscarPorId(Long id) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));
            return ApiResponse.success(modelMapper.map(marcoPDI, MarcoPDIDTO.class),
                    "Marco PDI encontrado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar Marco PDI: ", e);
            return ApiResponse.badRequest("Erro ao buscar Marco PDI: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<MarcoPDIDTO> atualizar(Long id, MarcoPDIDTO dto) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));

            modelMapper.map(dto, marcoPDI);
            MarcoPDI atualizado = marcoPDIRepository.save(marcoPDI);
            return ApiResponse.success(modelMapper.map(atualizado, MarcoPDIDTO.class),
                    "Marco PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar Marco PDI: ", e);
            return ApiResponse.badRequest("Erro ao atualizar Marco PDI: " + e.getMessage());
        }
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id) {
        try {
            if (!marcoPDIRepository.existsById(id)) {
                return ApiResponse.badRequest("Marco PDI não encontrado com ID: " + id);
            }
            marcoPDIRepository.deleteById(id);
            return ApiResponse.success(null, "Marco PDI deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar Marco PDI: ", e);
            return ApiResponse.badRequest("Erro ao deletar Marco PDI: " + e.getMessage());
        }
    }
}