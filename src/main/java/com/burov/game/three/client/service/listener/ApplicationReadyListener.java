package com.burov.game.three.client.service.listener;

import com.burov.game.three.client.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>{

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationReadyListener(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        applicationService.start();
    }
}
