package net.ncguy.serialui;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Guy on 17/05/2017.
 */
public class Utils {


    private static List<InetAddress> localInterfaceAddresses;
    public static List<InetAddress> LocalInterfaceAddresses() {
        if (localInterfaceAddresses == null) {
            localInterfaceAddresses = new ArrayList<>();
            try {
                Collections.list(NetworkInterface.getNetworkInterfaces()).forEach((net -> localInterfaceAddresses.addAll(Collections.list(net.getInetAddresses()))));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return localInterfaceAddresses;
    }

    private static List<InetAddress> localInterfaceBroadcasts;
    public static List<InetAddress> LocalInterfaceBroadcasts() {
        if (localInterfaceBroadcasts == null) {
            localInterfaceBroadcasts = new ArrayList<>();
            try {
                for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        localInterfaceBroadcasts.add(interfaceAddress.getBroadcast());
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return localInterfaceBroadcasts;
    }

    public static String AssembleBytes(byte[] data) {
        StringBuilder d = new StringBuilder();
        for (byte datum : data)
            d.append((char) datum);
        return d.toString();
    }

}
