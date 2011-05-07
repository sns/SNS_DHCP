/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sns_dhcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sina
 */
public class Utility {

    public static byte[] readNByte(int n, byte[] in, int start) {
        byte[] result = new byte[n];
        for (int i = 0; i < n; i++) {
            result[i] = in[start + i];
        }
        return result;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        } catch (IOException ex) {
            //TODO: Handle the exception
        }
        return bytes;
    }
    public static byte[] DHCPPacketToByte(DHCPPacket packet)
    {
        int optionSize = getoptionSize(packet.getOptions());
        int size = 240 + optionSize;
        int fillerSize = optionSize % 64;
        byte[] result = new byte[size];
        result [0] = packet.getOp();
        result [1] = packet.getHtype();
        result [2] = packet.getHlen();
        result [3] = packet.getHops();
        copyByte(result, packet.getXid(), 4, 0, 4);
        copyByte(result, packet.getSecs(), 8, 0, 2);
        copyByte(result, packet.getFlags(), 10, 0, 2);
        copyByte(result, packet.getCiaddr(), 12, 0, 4);
        copyByte(result, packet.getYiaddr(), 16, 0, 4);
        copyByte(result, packet.getSiaddr(), 20, 0, 4);
        copyByte(result, packet.getGiaddr(), 24, 0, 4);
        copyByte(result, packet.getChaddr(), 28, 0, 16);
        copyByte(result, packet.getSname(), 44, 0, 64);
        copyByte(result, packet.getFile(), 108, 0, 128);
        copyByte(result, packet.getMagicCookie(), 236, 0, 4);
        copyByte(result, getOptionsInBytes(packet.getOptions()), 240, 0, optionSize);
        byte[] filler = new byte[fillerSize];
        filler[0] = (byte)255;
        return result;
    }
//////////////////////////////////////////////////////////////////////////////
//////////////////////////DHCPServer Utility//////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    public static byte[] getIPAddress() {

        try {
            Enumeration<NetworkInterface> nis =
                    NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (!ia.isLoopbackAddress() && ia.isSiteLocalAddress()) {
                        return ia.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean optionTraverse(byte[] option, int optionID, byte[] result) {
        boolean found = false;
        int i = 0;
        while (i < option.length) {
            if (option[i] == optionID) {
                result = readNByte(6, option, i);
                found = true;
                break;
            } else {
                i += option[i + 1];
            }
        }
        return found;
    }
    public static byte[] getOptionsInBytes(Vector<Option> options)
    {
        byte[] result = new byte[getoptionSize(options)+2];
        int j = 0;
        for(int i = 0 ; i < options.size() ; i++ )
        {
            result[j] = options.get(i).getType();
            result[j+1] = options.get(i).getLength();
            copyByte(result, options.get(i).getValue(), j+2, 0, options.get(i).getLength());
            j += options.get(i).getLength() + 2;
        }
        return result;
    }

    public static void copyByte(byte[] dest, byte[] src, int fromDest, int fromSrc, int length) {
        int i = fromSrc;
        int j = fromDest;
        for (int k = 0; k < length; k++) {
            dest[j] = src[i];
            i++;
            j++;
        }
    }
    public static int getoptionSize( Vector<Option> options)
    {
        int size = 0;
        for(int i = 0 ; i < options.size() ; i++)
        {
            size += (options.get(i).getLength() + 2);
        }
        return size;
    }
    public static IPAddress RandomIPGenerator(IPAddress subnetmask, Random randomIP)
    {
        //ip = first.second.third.forth
        byte first = (byte) randomIP.nextInt((256 - subnetmask.getFirst()) + subnetmask.getFirst());
        byte second = (byte) randomIP.nextInt((256 - subnetmask.getSecond()) + subnetmask.getSecond());
        byte third = (byte) randomIP.nextInt((256 - subnetmask.getThird()) + subnetmask.getThird());
        byte forth = (byte) randomIP.nextInt((256 - subnetmask.getForth()) + subnetmask.getForth());
        IPAddress offeredIP = new IPAddress(first, second, third, forth);
        return offeredIP;
    }
    public static boolean HasIPAdress(IPAddress ip, HashMap<byte[],IPTime> db)
    {
        boolean contains = false;
        Iterator iter = db.entrySet().iterator();
        IPTime tempValue;
        while(iter.hasNext())
        {
            Map.Entry pairs = (Map.Entry) iter.next();
            tempValue = (IPTime) pairs.getValue();
            if(tempValue.getIp() == ip)
               contains = true;

        }
        return  contains;
    }

    public static DHCPPacket getDiscover(DHCPPacket discover, HashMap<byte[], IPTime> db ,HashMap<byte[], IPTime> reserved, Random randomIP, IPAddress subnetmask)
    {
        DHCPPacket offer = discover;  // optopn 53 must have the value of 2
        Option offerOption = new Option((byte) 53, (byte) 2);
        offer.getOptions().add(offerOption);
        IPAddress offeredIP;

        do{
            offeredIP = Utility.RandomIPGenerator(subnetmask, randomIP);
        }while(Utility.HasIPAdress(offeredIP, reserved) || Utility.HasIPAdress(offeredIP, db));

        byte[] mac = Utility.readNByte(6, offer.getChaddr(), 0);
        Timestamp now = Utility.getCurrentTimeStamp();
        //creat IPTime object and put into reserve HashMap
        IPTime reservedIPTime = new IPTime(offeredIP, now.getTime());
        reserved.put(mac, reservedIPTime);
        //set client offer ip
        offer.setYiaddr(offeredIP.IPAddressToByte());
        return offer;
    }
    public static DHCPPacket getRequest(DHCPPacket request, HashMap<byte[], IPTime> reserved, HashMap<byte[], IPTime> db, byte [] Option50)
    {
        DHCPPacket AckOrDecline = request;
        byte[] mac = Utility.readNByte(6, request.getChaddr(), 0);
        IPAddress requestedIP = new IPAddress(readNByte(4, Option50 , 2));
        IPAddress offeredIP = reserved.get(mac).getIp();
        if(offeredIP == requestedIP)
        {
            Timestamp now = Utility.getCurrentTimeStamp();
            //creat IPTime and insert into db
            IPTime confirmed = new IPTime(offeredIP, now);
            db.put(mac, confirmed);
            Option AckOption = new Option((byte) 53, (byte) 5);
            AckOrDecline.getOptions().add(AckOption);
        }
        else if(offeredIP != requestedIP)
        {
            Option DeclineOption = new Option((byte) 53, (byte) 4);
            AckOrDecline.getOptions().add(DeclineOption);
        }
        return AckOrDecline;
    }

    public static void sendReply(DHCPPacket toBeSend, DatagramSocket socket)
    {
        int replySize = Utility.DHCPPacketToByte(toBeSend).length;
            byte[] reply = Utility.DHCPPacketToByte(toBeSend);
            DatagramPacket replyPacket = new DatagramPacket(reply, replySize);
            try {
                socket = new DatagramSocket();
                replyPacket.setPort(68);
                replyPacket.setAddress(InetAddress.getByAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}));
                socket.setBroadcast(true);

                socket.send(replyPacket);


            } catch (SocketException ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public static boolean hasMagicCookie(byte[] data)
    {
        boolean hasIt = false;
        byte[] magic = readNByte(4, data, 236);
        if(magic [0] == 99 && magic[1] == (byte)130 && magic[2] == 83 && magic[3] == 99)
            hasIt = true;
        return hasIt;
    }
    public static Timestamp getCurrentTimeStamp()
    {
            // get current timestamp
        Calendar today = Calendar.getInstance();
        Timestamp now = new Timestamp(today.getTimeInMillis());
        return now;
    }
    //////////////////////////////////////////////////////////
    /////////////////////////General//////////////////////////
    //////////////////////////////////////////////////////////
    public static IPAddress dottedStringToIP(String input)
    {

        IPAddress ip = new IPAddress((byte)0,(byte)0,(byte)0,(byte)0);
        String[] temp = input.split("\\.");
        int first = Integer.parseInt(temp[0]);
        int second = Integer.parseInt(temp[1]);
        int third = Integer.parseInt(temp[2]);
        int forth = Integer.parseInt(temp[3]);
        ip.setFirst((byte)first);
        ip.setSecond((byte)second);
        ip.setThird((byte)third);
        ip.setForth((byte)forth);
        return ip;
    }

}
