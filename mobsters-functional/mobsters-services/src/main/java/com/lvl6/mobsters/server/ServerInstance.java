package com.lvl6.mobsters.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServerInstance  {
	private static final Logger LOG = LoggerFactory.getLogger(ServerInstance.class);

	private final String serverId;
	
	public ServerInstance()
	{
        this.serverId = 
            java.lang.management.ManagementFactory
                .getRuntimeMXBean()
                .getName();
        LOG.info("Using {} for server id", this.serverId);
	}
	
	public String serverId()
	{
	    return this.serverId;
	}
	
// This all became overkill once I dug into getting the PID and found it came along 
// with a host name already.
//
//    private long pid;
//    private String hostname;
//    private InetAddress ip;
//    
//    public void setIPAddress(String ipAddress) throws UnknownHostException {
//        this.ip = InetAddress.getByName(ipAddress);
//    }
//
//    public void setHostname(String hostname) {
//        this.hostname = hostname;
//    }
//
//    private void resolvePID() {
//        final String procName = 
//            java.lang.management.ManagementFactory
//                .getRuntimeMXBean()
//                .getName();
//        this.pid = Long.parseLong(procName.split("@")[0]);
//        LOG.info( "Extracted process ID {} from JMX process name, {}", this.pid, procName);
//    }
//
//    private void resolveIP() throws UnknownHostException {
//        if (ip == null) {
//            ip = InetAddress.getLocalHost();
//        }
//    }
//
//    private void resolveHostname() {
//        if (this.ip == null) {
//            final String uuid = UUID.randomUUID().toString();
//            LOG.warn("No IP address known when resolving node name.  Using a random UUID of {} instead.", uuid);
//            this.hostname = uuid;
//        } else {            
//            // Attempt name resolution in the following order:
//            // -- Retrieve any name that was cached on construction
//            // -- Query the system name service
//            // -- Serialize the InetAddress as a String
//            this.hostname = ip.getHostName();
//            
//            // If the resolution managed to yield a representation of localhost,
//            // an empty string, or null, replace it with a random UUID as a last resort.
//            if (uniqueHostnameNeeded()) {
//                final String uuid = UUID.randomUUID().toString();
//                LOG.warn(
//                    "IP address {} resolved to unusable node name of {}.  Using a random UUID of {} instead.",
//                    new Object[] {this.ip, this.hostname, uuid});
//                this.hostname = uuid;
//            } else {
//                LOG.info("Using {} for node name portion of server id", this.hostname);
//            }
//        }
//    }
//
//    private boolean uniqueHostnameNeeded() {
//        return this.hostname == null 
//            || this.hostname.isEmpty()
//            || this.hostname.equals("localhost")
//            || this.hostname.equals("127.0.0.1");
//    }
//
//    // @PostConstruct
//    public void initV1() throws UnknownHostException {
//        resolvePID();
//        if (uniqueHostnameNeeded()) {
//            resolveIP();
//            resolveHostname();
//        }
//
//        this.serverId = String.format("%d:%s", this.pid, this.hostname);
//        LOG.info("Using {} for server id", this.serverId);
//    }
}
