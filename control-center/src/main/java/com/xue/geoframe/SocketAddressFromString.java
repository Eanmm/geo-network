package com.xue.geoframe;

import java.net.InetSocketAddress;

/**
 * @author Xue
 * @create 2024-04-22 14:49
 */
public class SocketAddressFromString {
    private final InetSocketAddress address;

    /**
     * SocketAddressFromString constructor.
     *
     * @param addressStr String formatted as host:port
     */
    public SocketAddressFromString(final String addressStr) {
        String[] hostAndPort = addressStr.split(":");
        if (hostAndPort.length != 2) {
            throw new IllegalArgumentException("Expected host:port, got " + addressStr);
        }
        String hostname = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);
        this.address = new InetSocketAddress(hostname, port);
    }

    /**
     * Get the address as an InetSocketAddress.
     *
     * @return InetSocketAddress address.
     */
    public InetSocketAddress asInetSocketAddress() {
        return address;
    }

}
