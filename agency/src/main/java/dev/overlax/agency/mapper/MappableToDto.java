package dev.overlax.agency.mapper;

import java.util.List;

public interface MappableToDto<E, D> {

    D toDto(E entity);

    List<D> toDtoList(List<E> emtityList);
}
