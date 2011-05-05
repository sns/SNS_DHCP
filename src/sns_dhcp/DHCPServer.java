/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import java.net.DatagramPacket;
import java.util.HashMap;

/**
 *
 * @author sina
 */
public class DHCPServer {

    private DatagramPacket socket;
    private HashMap<Integer,Object> db;

    public DHCPServer() {
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
        byte[] option = new byte[recivedPacket.getLength()-240];
        option = Utility.readNByte(recivedPacket.getLength()-240, validData, 240);
        
    }
}
