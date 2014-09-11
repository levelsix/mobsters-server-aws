package com.lvl6.mobsters.websocket.controllers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Transition-period-scoped websocket controller that is responsible for branching a copy of each
 * coming event to each version of the architecture as appropriate.  
 * 
 * The @SendTo reply dispatching facility cannot be used 
 * 
 * @author jheinnic
 *
 */
@Controller
public class MigrationController {
    @MessageMapping("/app/requests/ArrAchievement")
    @SendTo("amq-queue/clientevents")
    public void foo() {
    	
    }
}
