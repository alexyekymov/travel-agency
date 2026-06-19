package dev.overlax.agency.mapper;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(E entity);

    List<D> toDtoList(List<E> emtityList);

    E toEntity(D dto);

    List<E> toEntityList(List<D> dtoList);
}
