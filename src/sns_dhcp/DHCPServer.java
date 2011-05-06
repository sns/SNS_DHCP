/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import com.sun.swing.internal.plaf.basic.resources.basic;
import java.net.DatagramPacket;
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
    private HashMap<byte[],Object> db;
    private HashMap<byte[], Object> reserved;
    private Random randomIp;

    public DHCPServer(IPAddress subnetmask, IPAddress gateway, IPAddress DNS, Timestamp renewal, Timestamp rebinding, Timestamp lease) {
        byte[] validData;
        DatagramPacket recivedPacket;
        DHCPPacket temp = null;
        {
            byte[] recievedData = new byte[2000];
            recivedPacket = new DatagramPacket(recievedData, 2000);
            validData = Utility.readNByte(recivedPacket.getLength(), recievedData, 0);
            recivedPacket.setData(validData);
        }
        temp = new DHCPPacket(recivedPacket);
        //option 240 start
        int optionSize = recivedPacket.getLength()-240;
        byte[] option = new byte[optionSize];
        option = Utility.readNByte(optionSize, validData, 240);
        byte[] MessageOption = new byte[6];
        Utility.optionTraverse(option, 53, MessageOption);
        if (MessageOption[1] == 1) // we recieved Discover message , we must create an offer message
        {
            DHCPPacket offer = temp;  // optopn 53 must have the value of 2
            //ip = first.second.third.forth
            byte first = (byte) randomIp.nextInt((256 - subnetmask.getFirst()) + subnetmask.getFirst());
            byte second = (byte) randomIp.nextInt((256 - subnetmask.getSecond()) + subnetmask.getSecond());
            byte third = (byte) randomIp.nextInt((256 - subnetmask.getThird()) + subnetmask.getThird());
            byte forth = (byte) randomIp.nextInt((256 - subnetmask.getForth()) + subnetmask.getForth());
            IPAddress offeredIP = new IPAddress(first, second, third, forth);
            byte[] mac = Utility.readNByte(6, temp.getChaddr(), 0);
            // get current timestamp
            Calendar today = Calendar.getInstance();
            Timestamp now = new Timestamp(today.getTimeInMillis());
            //creat IPTime object and put into reserve HashMap
            IPTime reservedIPTime = new IPTime(offeredIP, now.getTime());
            reserved.put(mac, reservedIPTime);
            //set client offer ip
            temp.setYiaddr(offeredIP.IPAddressToByte());
        }

    }
}
