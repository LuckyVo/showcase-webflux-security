package ru.yandex.dto;

public record PagingDto(int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {
}
