/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Vector;

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
    private byte [] magicCookie= new byte[]{99,(byte)130,83,99};
    //private byte [] options;    // 64x byte
    private Vector<Option> options;
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
        //yiaddr = new byte[]{(byte)192,(byte)168,1,4};
        yiaddr = new byte[4];
        siaddr = Utility.getIPAddress();
        giaddr = new byte[4];
        chaddr = Utility.readNByte(16,message.getData(),28);
        sname = new byte[64];
        file = new byte[128];
        //options = new byte [64];
        options = new Vector<Option>();

    }

    /**
     * @return the giaddr
     */
    public byte[] getGiaddr() {
        return giaddr;
    }

    /**
     * @param giaddr the giaddr to set
     */
    public void setGiaddr(byte[] giaddr) {
        this.giaddr = giaddr;
    }

    /**
     * @return the op
     */
    public byte getOp() {
        return op;
    }

    /**
     * @param op the op to set
     */
    public void setOp(byte op) {
        this.op = op;
    }

    /**
     * @return the htype
     */
    public byte getHtype() {
        return htype;
    }

    /**
     * @param htype the htype to set
     */
    public void setHtype(byte htype) {
        this.htype = htype;
    }

    /**
     * @return the hlen
     */
    public byte getHlen() {
        return hlen;
    }

    /**
     * @param hlen the hlen to set
     */
    public void setHlen(byte hlen) {
        this.hlen = hlen;
    }

    /**
     * @return the hops
     */
    public byte getHops() {
        return hops;
    }

    /**
     * @param hops the hops to set
     */
    public void setHops(byte hops) {
        this.hops = hops;
    }

    /**
     * @return the xid
     */
    public byte[] getXid() {
        return xid;
    }

    /**
     * @param xid the xid to set
     */
    public void setXid(byte[] xid) {
        this.setXid(xid);
    }

    /**
     * @return the secs
     */
    public byte[] getSecs() {
        return secs;
    }

    /**
     * @param secs the secs to set
     */
    public void setSecs(byte[] secs) {
        this.secs = secs;
    }

    /**
     * @return the flags
     */
    public byte[] getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(byte[] flags) {
        this.flags = flags;
    }

    /**
     * @return the ciaddr
     */
    public byte[] getCiaddr() {
        return ciaddr;
    }

    /**
     * @param ciaddr the ciaddr to set
     */
    public void setCiaddr(byte[] ciaddr) {
        this.ciaddr = ciaddr;
    }

    /**
     * @return the yiaddr
     */
    public byte[] getYiaddr() {
        return yiaddr;
    }

    /**
     * @param yiaddr the yiaddr to set
     */
    public void setYiaddr(byte[] yiaddr) {
        this.yiaddr = yiaddr;
    }

    /**
     * @return the siaddr
     */
    public byte[] getSiaddr() {
        return siaddr;
    }

    /**
     * @param siaddr the siaddr to set
     */
    public void setSiaddr(byte[] siaddr) {
        this.siaddr = siaddr;
    }

    /**
     * @return the chaddr
     */
    public byte[] getChaddr() {
        return chaddr;
    }

    /**
     * @param chaddr the chaddr to set
     */
    public void setChaddr(byte[] chaddr) {
        this.chaddr = chaddr;
    }

    /**
     * @return the sname
     */
    public byte[] getSname() {
        return sname;
    }

    /**
     * @param sname the sname to set
     */
    public void setSname(byte[] sname) {
        this.sname = sname;
    }

    /**
     * @return the file
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(byte[] file) {
        this.file = file;
    }

    /**
     * @return the options
     */
    public Vector<Option> getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(Vector<Option> options) {
        this.options = options;
    }

    /**
     * @return the magicCookie
     */
    public byte[] getMagicCookie() {
        return magicCookie;
    }

   
   
}
