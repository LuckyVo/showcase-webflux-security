package ru.yandex.mapper;

import ru.yandex.dto.ItemDto;
import ru.yandex.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ItemMapper {

    @Mapping(target = "count", source = "quantity")
    public abstract ItemDto toDto(Item item, int quantity);
}
