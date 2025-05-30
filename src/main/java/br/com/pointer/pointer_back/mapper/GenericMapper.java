package br.com.pointer.pointer_back.mapper;

public interface GenericMapper<DTO, ENTITY, RESPONSE> {
    ENTITY toEntity(DTO dto);
    RESPONSE toResponseDTO(ENTITY entity);
    void updateEntityFromDTO(DTO dto, ENTITY entity);
}