package com.lvl6.mobsters.domain.svcreg

import com.lvl6.mobsters.domain.config.ConfigExtensions;
import com.lvl6.mobsters.domain.config.IConfigurationRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("mobstersDomainModelRegistry")
public class ServiceRegistry 
implements IServiceRegistry 
{
	@Property
	@Autowired
	var ConfigExtensions configExtensions
	
	@Property
	@Autowired
	var IConfigurationRegistry configurationRegistry
}