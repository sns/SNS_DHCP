/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sns_dhcp;

import com.sun.swing.internal.plaf.basic.resources.basic;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import sns_dhcp.IPTime;
import sun.security.jca.GetInstance;

/**
 *
 * @author sina
 */
public class DHCPServer {

    private DatagramSocket socket;
    private HashMap<ByteArray, IPTime> db;
    private HashMap<ByteArray, IPTime> reserved;
    IPAddress dhcpServerIP;
    IPAddress DNS;
    IPAddress gateway;
    IPAddress subnetmask;
    int renewal;
    int rebinding;
    int lease;
    private Random randomIp = new Random();
    private int MaxLength = 2048;

    public DHCPServer() {
        try {
            socket = new DatagramSocket(67);
        } catch (SocketException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        db = new HashMap<ByteArray, IPTime>(255);
        reserved = new HashMap<ByteArray, IPTime>(255);
        dhcpServerIP = new IPAddress(Utility.getIPAddress());
        gateway = dhcpServerIP;
        DNS = dhcpServerIP;
        renewal = 18000; // 5 hour
        rebinding = 86400;  // 1day
        lease = 259200; //3day
        subnetmask = new IPAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 0});
    }

    public DHCPServer(IPAddress subnetmask, IPAddress gateway, IPAddress DNS, int renewal, int rebinding, int lease) {
        db = new HashMap<ByteArray, IPTime>(1024);
        reserved = new HashMap<ByteArray, IPTime>(1024);
        dhcpServerIP = new IPAddress(Utility.getIPAddress());
        this.gateway = gateway;
        this.DNS = DNS;
        this.renewal = renewal;
        this.rebinding = rebinding;
        this.lease = lease;
        this.subnetmask = subnetmask;
    }

    public void start() {
//        leaseCheckerThread leaseChecker = new leaseCheckerThread();
//        leaseChecker.run();
        while (true) {
            byte[] validData;
            DatagramPacket recivedPacket;
            try {
                socket = new DatagramSocket(67);
            } catch (SocketException ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            DHCPPacket temp = null;
            {
                byte[] recievedData = new byte[MaxLength];
                recivedPacket = new DatagramPacket(recievedData, MaxLength);
                try {
                    socket.receive(recivedPacket);
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                validData = Utility.readNByte(recivedPacket.getLength(), recievedData, 0);
                recivedPacket.setData(validData);
            }
            temp = new DHCPPacket(recivedPacket);
            Option gatewayOption = new Option((byte) 3, (byte) 4, gateway.IPAddressToByte());
            temp.getOptions().add(gatewayOption);
            Option DNSOption = new Option((byte) 6, (byte) 4, DNS.IPAddressToByte());
            temp.getOptions().add(DNSOption);
            Option subnetmaskOption = new Option((byte) 1, (byte) 4, subnetmask.IPAddressToByte());
            temp.getOptions().add(subnetmaskOption);
            Option serverIPOption = new Option((byte) 54, (byte) 4, Utility.getIPAddress());
            temp.getOptions().add(serverIPOption);
            Option renewalTimeOption = new Option((byte) 58, (byte) 4, Utility.intToByteArray(renewal));
            temp.getOptions().add(renewalTimeOption);
            Option rebindingTimeOption = new Option((byte) 59, (byte) 4, Utility.intToByteArray(rebinding));
            temp.getOptions().add(rebindingTimeOption);
            Option leaseTimeOption = new Option((byte) 51, (byte) 4, Utility.intToByteArray(lease));
            temp.getOptions().add(leaseTimeOption);
            //option 240 start
            int optionSize = recivedPacket.getLength() - 240;
            byte[] option = new byte[optionSize];
            option = Utility.readNByte(optionSize, validData, 240);
            byte[] messageOption = new byte[3];
            byte[] option50 = new byte[6];
            Utility.optionTraverse(option, 53, messageOption);
            Utility.optionTraverse(option, 50, option50);
            if (messageOption[2] == (byte) 1) // we recieved Discover message , we must create an offer message
            {
                DHCPPacket offer = Utility.getDiscover(temp, db, reserved, randomIp, subnetmask);
                Utility.sendReply(offer, socket);
            } else if (messageOption[2] == (byte) 3) // DHCP Request
            {
                DHCPPacket AckOrNack = Utility.getRequest(temp, reserved, db, option50);
                Utility.sendReply(AckOrNack, socket);
            }

        }

    }

//    class leaseCheckerThread extends Thread {
//
//        IPTime tempValue;
//        byte[] tempKey;
//
//        public void run() {
//
//            while (true) {
//                try {
//                    leaseCheckerThread.sleep(900000);
//                    Iterator itr = db.entrySet().iterator();
//                    while (itr.hasNext()) {
//                        Map.Entry pairs = (Map.Entry) itr.next();
//                        tempValue = (IPTime) pairs.getValue();
//                        Timestamp leaseTime = new Timestamp(lease * 1000);
//                        if (Utility.getCurrentTimeStamp().getTime() - tempValue.getTime().getTime() >= leaseTime.getTime()) {
//                            db.remove(pairs.getKey());
//                        }
//                    }
//
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }
}
