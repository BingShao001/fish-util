package com.yb.fish.primarykey;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class IpV4Utils {

    // 可能的虚拟网卡前缀
    private static final List<String> excludedInterfaceNames =
        Arrays.asList("veth", "docker", "vmnet", "tailscale", "Virtual", "VMware", "VirtualBox", "Hyper-V", "Tunnel",
            "vEthernet", "TAP");

    public static String getIpAddr() throws SocketException {

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();

            // 过滤虚拟网卡
            if (isVirtualNetworkInterface(ni)) {
                continue;
            }

            // 获取活动的IPv4地址
            for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                InetAddress inetAddress = interfaceAddress.getAddress();
                if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return null;
    }

    public static long lastNBitsOfIp(String ipAddress, int n) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IP address");
        }

        // 转换为整数
        long firstOctet = Long.parseLong(parts[0]);
        long secondOctet = Long.parseLong(parts[1]);
        long thirdOctet = Long.parseLong(parts[2]);
        long fourthOctet = Long.parseLong(parts[3]);
        return ((firstOctet << 24) | (secondOctet << 16) | (thirdOctet << 8) | fourthOctet) & (0xffffffff >>> (32 - n));
    }

    private static boolean isVirtualNetworkInterface(NetworkInterface ni) {
        String name = ni.getName();
        for (String prefix : excludedInterfaceNames) {
            if (name.contains(prefix)) {
                return true;
            }
        }
        return false;
    }
}
