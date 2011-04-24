/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author sina
 */
public class Utility {

    public static byte[] readNByte (int n, byte[] in, int start)
    {
        byte [] result = new byte[n];
        for(int i = 0 ; i < n ; i++)
        {
           result[i] = in[start+i];
        }
        return result;
    }

    public static byte[] getIPAddress() {

        try {
            Enumeration<NetworkInterface> nis =
                    NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (!ia.isLoopbackAddress() && ia.isSiteLocalAddress() ) {
                        return ia.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
