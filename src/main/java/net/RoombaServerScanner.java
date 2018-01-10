package net;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;


/*
 * Scans the local area network (LAN) for hosts running app server.
 *
 * Note: this classes assumes the network uses
 * the 192.168.1.x format!
 * */
public class RoombaServerScanner {

    private final static Logger LOGGER = Logger.getLogger(RoombaServerScanner.class);

    /*
     * Scans the local area network (LAN) for hosts running app server.
     * @return ArrayList<String> host addresses of servers.
     * */
    public static ArrayList<String> scan() {

        Thread thread = null;
        ArrayList<String> inetAddresses = new ArrayList<>();
        for (int i = 1; i < 255; i++) {
            thread = new ServerScannerThread(inetAddresses, "192.168.1." + i);
            thread.start();
        }

        LOGGER.trace("Threads are running");

        try {
            thread.join();
            LOGGER.trace("Threads joined");
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        Collections.sort(inetAddresses);
        return inetAddresses;
    }


}
