package com.uexcel.spring.security.event;

import com.uexcel.spring.security.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private  String applicationUlr;
    public RegistrationCompleteEvent(User user, String applicationUlr) {
        super(user);
        this.user = user;
        this.applicationUlr = applicationUlr;
    }
}
