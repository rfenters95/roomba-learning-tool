package net;

import com.maschel.roomba.RoombaJSSCClientSerial;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RoombaJSSCClient extends RoombaJSSCClientSerial {

    private final static Logger LOGGER = Logger.getLogger(RoombaJSSCClient.class);
    private Socket socket;

    @Override
    public String[] portList() {
        String[] ports = new String[0];
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("PORTS");
            Scanner scanner = new Scanner(socket.getInputStream());
            if (scanner.nextLine().equalsIgnoreCase("OK PORTS")) {
                ports = new String[Integer.parseInt(scanner.nextLine())];
                for (int i = 0; i < ports.length; i++) {
                    ports[i] = scanner.nextLine();
                }
                return ports;
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ports;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public boolean connect(String portId) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("CONNECT"); //portId ?
            printWriter.println(portId); //portId ?
            System.out.println("Sent CONNECT " + portId);
            System.out.println("Waiting for feedback");
            Scanner scanner = new Scanner(socket.getInputStream());
            if (scanner.nextLine().equalsIgnoreCase("CONNECTED " + portId)) {
                System.out.println("Server connected!");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("DISCONNECT");
            Scanner scanner = new Scanner(socket.getInputStream());
            if (scanner.nextLine().equalsIgnoreCase("OKAY")) {
                socket.close();
                System.out.println("Server disconnected!");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean send(byte[] bytes) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("SEND BYTES");
            printWriter.println(bytes.length);
            for (byte b : bytes) {
                printWriter.println(b);
            }
            Scanner scanner = new Scanner(socket.getInputStream());
            String nextLine = scanner.nextLine();
            if (nextLine.equalsIgnoreCase("SEND BYTES")) {
                nextLine = scanner.nextLine();
                byte[] sensorData = new byte[Integer.parseInt(nextLine)];
                for (int i = 0; i < sensorData.length; i++) {
                    nextLine = scanner.nextLine();
                    sensorData[i] = Byte.parseByte(nextLine);
                }
                setCurrentSensorData(sensorData);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean send(int b) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("SEND INT");
            printWriter.println(b);
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }
}
