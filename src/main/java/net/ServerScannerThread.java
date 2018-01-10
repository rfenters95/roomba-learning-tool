package net;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerScannerThread extends Thread {

    private final static Logger LOGGER = Logger.getLogger(ServerScannerThread.class);

    private String hostAddress;
    private ArrayList<String> inetAddresses;

    ServerScannerThread(ArrayList<String> inetAddresses, String hostAddress) {
        this.inetAddresses = inetAddresses;
        this.hostAddress = hostAddress;
    }

    @Override
    public void run() {
        try {
            InetAddress inetAddress = InetAddress.getByName(hostAddress);
            if (inetAddress.isReachable(1000)) {
                try {
                    Socket socket = new Socket(inetAddress.getHostAddress(), 13950);
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println("PORTS");
                    Scanner scanner = new Scanner(socket.getInputStream());
                    if (scanner.nextLine().equalsIgnoreCase("OK PORTS")) {
                        int length = Integer.parseInt(scanner.nextLine());
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                String device = scanner.nextLine();
                                inetAddresses.add(device + "@" + inetAddress.getHostAddress());
                            }
                        }
                    }
                } catch (ConnectException ignored) {
                    LOGGER.trace(hostAddress + " is unreachable");
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}