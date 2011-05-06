/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import java.sql.Timestamp;

/**
 *
 * @author sina
 */
public class IPTime {
    private IPAddress ip ;
    private Timestamp time;

    public IPTime(IPAddress ip,Timestamp time){
        setIp(ip);
        setTime(time);
    }

    IPTime(IPAddress offeredIP, long time) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * @return the ip
     */
    public IPAddress getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(IPAddress ip) {
        this.ip = ip;
    }
}
class IPAddress{
        private byte first;
        private byte second;
        private byte third;
        private byte forth;

        public IPAddress(byte first, byte second, byte third, byte forth )
        {
            setFirst(first);
            setSecond(second);
            setThird(third);
            setForth(forth);
        }
        public IPAddress(byte[] ip)
        {
            setFirst(ip[0]);
            setSecond(ip[1]);
            setThird(ip[2]);
            setForth(ip[3]);
        }
        /**
         * @return the first
         */
        public byte getFirst() {
            return first;
        }

        /**
         * @param first the first to set
         */
        public void setFirst(byte first) {
            this.first = first;
        }

        /**
         * @return the second
         */
        public byte getSecond() {
            return second;
        }

        /**
         * @param second the second to set
         */
        public void setSecond(byte second) {
            this.second = second;
        }

        /**
         * @return the third
         */
        public byte getThird() {
            return third;
        }

        /**
         * @param third the third to set
         */
        public void setThird(byte third) {
            this.third = third;
        }

        /**
         * @return the forth
         */
        public byte getForth() {
            return forth;
        }

        /**
         * @param forth the forth to set
         */
        public void setForth(byte forth) {
            this.forth = forth;
        }
        public byte[] IPAddressToByte()
        {
            byte [] result = new byte[4];
            result[0] = this.getFirst();
            result[1] = this.getSecond();
            result[2] = this.getThird();
            result[3] = this.getForth();
            return result;
        }
        

    }
