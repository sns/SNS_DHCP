/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import com.sun.swing.internal.plaf.basic.resources.basic;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author sina
 */
public class DHCPServer {

    private DatagramPacket socket;
    private HashMap<byte[],IPTime> db;
    private HashMap<byte[], IPTime> reserved;
    private Random randomIp;
    private int MaxLength = 2048;
    public DHCPServer(IPAddress subnetmask, IPAddress gateway, IPAddress DNS, int renewal, int rebinding, int lease) {
        byte[] validData;
        DatagramPacket recivedPacket;
        DHCPPacket temp = null;
        {
            byte[] recievedData = new byte[MaxLength];
            recivedPacket = new DatagramPacket(recievedData, MaxLength);
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
        byte[] messageOption = new byte[6];
        byte[] option50 = new byte[6];
        Utility.optionTraverse(option, 53, messageOption);
        Utility.optionTraverse(option, 50, option50);
        if (messageOption[2] == 1) // we recieved Discover message , we must create an offer message
        {
          DHCPPacket offer = Utility.getDiscover(temp, reserved, randomIp, subnetmask);
        } else if (messageOption[2] == 3) // DHCP Request
        {
          DHCPPacket AckOrDecline = Utility.getRequest(temp, reserved, db, option50);
        }

    }
   
}
