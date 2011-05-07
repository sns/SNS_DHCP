/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sns_dhcp;

/**
 *
 * @author root
 */
public class ByteArray {

    byte[] data;

    ByteArray(byte[] d) {
        data = d;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ByteArray)) {
            return false;
        }
        ByteArray other = (ByteArray) obj;
        for (int i = 0; i < other.data.length; i++) {
            if (data[i] != other.data[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = data[0];
        for (int i = 1; i < data.length; i++) {
            hash = Integer.rotateLeft(hash, 8);
            hash ^= data[i];
        }
        return hash;
    }
}
