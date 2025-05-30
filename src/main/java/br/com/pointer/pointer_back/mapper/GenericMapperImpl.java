package br.com.pointer.pointer_back.mapper;

public abstract class GenericMapperImpl<DTO, ENTITY, RESPONSE> implements GenericMapper<DTO, ENTITY, RESPONSE> {
    @Override
    public abstract ENTITY toEntity(DTO dto);

    @Override
    public abstract RESPONSE toResponseDTO(ENTITY entity);

    @Override
    public abstract void updateEntityFromDTO(DTO dto, ENTITY entity);
}