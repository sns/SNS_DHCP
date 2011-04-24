/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 *
 * @author sina
 */
public class DHCPPacket {
    private byte op ;           // for server = 2 , for client = 1
    private byte htype;         // hardware kind ,for example 10 Mb Ethernet
    private byte hlen;          // length of device address which is connected , usually 6 (byte) for MAC
    private byte hops;          // 0 in end systems , inc for each router
    private byte [] xid;        // 4 byte message id (repeat)
    private byte [] secs;       // 2 byte (0)
    private byte [] flags;      // 2 byte (bit flag = 0)
    private byte [] ciaddr;     // 4 byte (0)
    private byte [] yiaddr;     // 4 byte (client ip address)
    private byte [] siaddr;     // 4 byte (server ip address)
    private byte [] giaddr;     // 4 byte (0)
    private byte [] chaddr;     // 16 byte -> first 6 byte = client MAC Address;
    private byte [] sname;      // 44 byte (0)
    private byte [] file;       // 128 byte (0)
    private byte [] options;    // 64x byte
    ////////////////////////////////////////////////////////
    //////////////////////// methods ///////////////////////
    ////////////////////////////////////////////////////////
    public DHCPPacket(DatagramPacket message)
    {
        op = 2;
        htype = message.getData()[1];
        hlen = message.getData()[2];
        hops = 0;
        xid = Utility.readNByte(4, message.getData(), 4);
        secs = Utility.readNByte(2, message.getData(), 8);
        flags = Utility.readNByte(2, message.getData(), 10);
        ciaddr = new byte[4];
        yiaddr = new byte[]{(byte)192,(byte)168,1,4};
        siaddr = Utility.getIPAddress();
        giaddr = new byte[4];
        chaddr = Utility.readNByte(16,message.getData(),28);
        sname = new byte[64];
        file = new byte[128];
        options = new byte [64];

    }
   
}
