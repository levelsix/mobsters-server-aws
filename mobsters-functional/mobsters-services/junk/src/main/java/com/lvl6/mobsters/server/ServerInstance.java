package com.lvl6.mobsters.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServerInstance  {

	
	private static final Logger log = LoggerFactory.getLogger(ServerInstance.class);

	
	public ServerInstance() {};
	
	
	private String hostName = "";

	public String serverId(){
		if (hostName.equals("")) {
			setServerId();
		}
		return hostName;
	}
	
	protected void setServerId() {
		File hostn = new File("/etc/hostname");
		if(hostn.exists() && hostn.canRead()) {
			try {
				hostName = new Scanner(hostn).useDelimiter("\\Z").next();
				return;
			} catch (FileNotFoundException e) {
				log.error("Could not read /etc/hostname", e);
			}
		}else {
			log.error("Setting serverId to random UUID");
            hostName = UUID.randomUUID().toString();
		}
	}

	
}
