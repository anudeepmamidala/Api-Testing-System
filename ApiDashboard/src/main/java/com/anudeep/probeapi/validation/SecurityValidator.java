package com.anudeep.probeapi.validation;

import com.anudeep.probeapi.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@Component
@Slf4j
public class SecurityValidator {

    @Value("${app.ssrf.allow-localhost:true}")
    private boolean allowLocalhost;

    public void validateSSRF(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            
            if (host == null) {
                throw new CustomException("Invalid host in URL", "INVALID_URL", 400);
            }

            // Allow localhost in development mode
            if (allowLocalhost && (host.equals("localhost") || host.equals("127.0.0.1"))) {
                log.debug("Allowing localhost URL in development: {}", url);
                return;
            }

            // Block private/internal IPs
            if (isPrivateIP(host)) {
                log.warn("Blocked SSRF attempt to host: {}", host);
                throw new CustomException("Access to internal/private IPs is not allowed", "FORBIDDEN", 403);
            }

            // Resolve hostname and check resolved IP
            try {
                InetAddress[] addresses = InetAddress.getAllByName(host);
                for (InetAddress addr : addresses) {
                    if (addr.isLoopbackAddress() || isResolvedIPPrivate(addr) || 
                        addr.isAnyLocalAddress() || addr.isLinkLocalAddress()) {
                        log.warn("Blocked SSRF attempt to resolved IP: {}", addr.getHostAddress());
                        throw new CustomException("Access to internal/private IPs is not allowed", 
                            "FORBIDDEN", 403);
                    }
                }
            } catch (UnknownHostException e) {
                log.warn("Could not resolve host: {}", host);
                // Allow unresolvable hosts - may fail later, which is fine
            }

        } catch (URISyntaxException e) {
            throw new CustomException("Invalid URL format", "INVALID_URL", 400);
        }
    }

    private boolean isResolvedIPPrivate(InetAddress addr) {
        String hostAddress = addr.getHostAddress();
        return hostAddress.equals("127.0.0.1") ||
               hostAddress.equals("0.0.0.0") ||
               hostAddress.equals("::1") ||
               hostAddress.startsWith("192.168.") ||
               hostAddress.startsWith("10.") ||
               hostAddress.startsWith("172.") ||
               hostAddress.startsWith("127.") ||
               hostAddress.startsWith("::ffff:") ||
               hostAddress.startsWith("fc00:") ||
               hostAddress.startsWith("fe80:");
    }

    private boolean isPrivateIP(String host) {
        // Remove port if present
        if (host.contains(":")) {
            host = host.substring(0, host.lastIndexOf(":"));
        }

        // Check common patterns
        if (host.equals("localhost") ||
            host.equals("127.0.0.1") ||
            host.equals("0.0.0.0") ||
            host.equals("::1") ||
            host.startsWith("192.168.") ||
            host.startsWith("10.") ||
            host.startsWith("172.") ||
            host.startsWith("127.") ||
            host.startsWith("::ffff:") ||
            host.startsWith("fc00:") ||
            host.startsWith("fe80:")) {
            return true;
        }

        return false;
    }

}
