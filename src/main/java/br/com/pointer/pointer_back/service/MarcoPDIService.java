package br.com.pointer.pointer_back.service;

import br.com.pointer.pointer_back.dto.MarcoPDIDTO;
import br.com.pointer.pointer_back.exception.MarcoPDINaoEncontradoException;
import br.com.pointer.pointer_back.model.MarcoPDI;
import br.com.pointer.pointer_back.model.PDI;
import br.com.pointer.pointer_back.repository.MarcoPDIRepository;
import br.com.pointer.pointer_back.repository.PDIRepository;
import br.com.pointer.pointer_back.ApiResponse;
import br.com.pointer.pointer_back.util.ApiResponseUtil;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarcoPDIService {

    private static final Logger logger = LoggerFactory.getLogger(MarcoPDIService.class);

    private final MarcoPDIRepository marcoPDIRepository;
    private final PDIRepository pdiRepository;
    private final ModelMapper modelMapper;
    private final ApiResponseUtil apiResponseUtil;

    @Autowired
    public MarcoPDIService(MarcoPDIRepository marcoPDIRepository, PDIRepository pdiRepository,
            ModelMapper modelMapper, ApiResponseUtil apiResponseUtil) {
        this.marcoPDIRepository = marcoPDIRepository;
        this.pdiRepository = pdiRepository;
        this.modelMapper = modelMapper;
        this.apiResponseUtil = apiResponseUtil;
    }

    @Transactional
    public ApiResponse<MarcoPDIDTO> criar(MarcoPDIDTO dto) {
        try {
            logger.info("Iniciando criação de Marco PDI com dados: {}", dto);

            if (dto.getPdiId() == null) {
                return apiResponseUtil.error("ID do PDI é obrigatório", 400);
            }

            PDI pdi = pdiRepository.findById(dto.getPdiId())
                    .orElseThrow(() -> new IllegalArgumentException("PDI não encontrado com ID: " + dto.getPdiId()));

            MarcoPDI marcoPDI = modelMapper.map(dto, MarcoPDI.class);
            marcoPDI.setPdi(pdi);
            logger.info("Marco PDI convertido para entidade: {}", marcoPDI);

            MarcoPDI salvo = marcoPDIRepository.save(marcoPDI);
            logger.info("Marco PDI salvo com sucesso: {}", salvo);

            return apiResponseUtil.created(modelMapper.map(salvo, MarcoPDIDTO.class), "Marco PDI criado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao criar Marco PDI: ", e);
            return apiResponseUtil.error("Erro ao criar Marco PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<MarcoPDIDTO>> listarPorPDI(Long pdiId) {
        try {
            List<MarcoPDI> marcos = marcoPDIRepository.findByPdiId(pdiId);
            return apiResponseUtil.mapList(marcos, MarcoPDIDTO.class, "Marcos PDI listados com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao listar Marcos PDI: ", e);
            return apiResponseUtil.error("Erro ao listar Marcos PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<MarcoPDIDTO> buscarPorId(Long id) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));
            return apiResponseUtil.map(marcoPDI, MarcoPDIDTO.class, "Marco PDI encontrado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao buscar Marco PDI: ", e);
            return apiResponseUtil.error("Erro ao buscar Marco PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional
    public ApiResponse<MarcoPDIDTO> atualizar(Long id, MarcoPDIDTO dto) {
        try {
            MarcoPDI marcoPDI = marcoPDIRepository.findById(id)
                    .orElseThrow(() -> new MarcoPDINaoEncontradoException("Marco PDI não encontrado com ID: " + id));

            modelMapper.map(dto, marcoPDI);
            MarcoPDI atualizado = marcoPDIRepository.save(marcoPDI);
            return apiResponseUtil.map(atualizado, MarcoPDIDTO.class, "Marco PDI atualizado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao atualizar Marco PDI: ", e);
            return apiResponseUtil.error("Erro ao atualizar Marco PDI: " + e.getMessage(), 400);
        }
    }

    @Transactional
    public ApiResponse<Void> deletar(Long id) {
        try {
            if (!marcoPDIRepository.existsById(id)) {
                return apiResponseUtil.error("Marco PDI não encontrado com ID: " + id, 400);
            }
            marcoPDIRepository.deleteById(id);
            return apiResponseUtil.success(null, "Marco PDI deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar Marco PDI: ", e);
            return apiResponseUtil.error("Erro ao deletar Marco PDI: " + e.getMessage(), 400);
        }
    }
}