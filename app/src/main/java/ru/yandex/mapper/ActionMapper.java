package ru.yandex.mapper;

import ru.yandex.dto.ActionDto;
import ru.yandex.utils.Action;
import org.springframework.stereotype.Component;

@Component
public class ActionMapper {

    public Action to(ActionDto actionDto) {
        return Action.valueOf(actionDto.action.toUpperCase());
    }
}
