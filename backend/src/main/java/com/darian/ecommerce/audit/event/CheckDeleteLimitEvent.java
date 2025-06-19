package com.darian.ecommerce.audit.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CheckDeleteLimitEvent extends ApplicationEvent {
    private final Integer userId;

    public CheckDeleteLimitEvent(Object source, Integer userId) {
        super(source);
        this.userId = userId;
    }

}