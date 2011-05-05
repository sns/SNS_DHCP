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
    public byte op ;           // for server = 2 , for client = 1
    public byte htype;         // hardware kind ,for example 10 Mb Ethernet
    public byte hlen;          // length of device address which is connected , usually 6 (byte) for MAC
    public byte hops;          // 0 in end systems , inc for each router
    public byte [] xid;        // 4 byte message id (repeat)
    public byte [] secs;       // 2 byte (0)
    public byte [] flags;      // 2 byte (bit flag = 0)
    public byte [] ciaddr;     // 4 byte (0)
    public byte [] yiaddr;     // 4 byte (client ip address)
    public byte [] siaddr;     // 4 byte (server ip address)
    public byte [] giaddr;     // 4 byte (0)
    public byte [] chaddr;     // 16 byte -> first 6 byte = client MAC Address;
    public byte [] sname;      // 44 byte (0)
    public byte [] file;       // 128 byte (0)
    public byte [] magicCookie= new byte[]{99,(byte)130,83,99};
    public byte [] options;    // 64x byte
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
        flags[0]=1;
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
