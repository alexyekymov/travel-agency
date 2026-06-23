package dev.overlax.agency.mapper;

import java.util.List;

public interface MappableToEntity<D, E> {

    E toEntity(D dto);

    List<E> toEntityList(List<D> dtoList);
}
