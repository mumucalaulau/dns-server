package com.mumuca.dnsresolver.servers.udp;

import com.mumuca.dnsresolver.servers.udp.processors.UDPQueryProcessor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer implements Runnable {

    private final ExecutorService executorService;

    public UDPServer() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(3053)) {
            System.out.println("UDP Server listening on port 3053...");

            while (true) {
                byte[] buffer = new byte[512];

                DatagramPacket queryPacket = new DatagramPacket(buffer, buffer.length);

                socket.receive(queryPacket);

                executorService.submit(new UDPQueryProcessor(socket, queryPacket));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            this.executorService.shutdown();
        }
    }
}
