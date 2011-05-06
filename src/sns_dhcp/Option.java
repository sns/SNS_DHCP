/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sns_dhcp;

import javax.print.DocFlavor.BYTE_ARRAY;

/**
 *
 * @author sina
 */
public class Option {

   private byte type;
   private byte length;
   private byte[] value;
   public Option(byte type, byte length, byte [] value)
   {
       this.type = type;
       this.length = length;
       this.value = value;
   }
   public Option(byte length)
   {
       type = 0;
       this.length = length ;
       value = new byte[length];
   }
   public Option()
   {
       type = 0;
       length = 0;
       value = new byte [0];

   }

    Option(byte type , byte val) {
        setType(type);
        setLength((byte)1);
        value = new byte[length];
        value[0 ] = val;
    }

    

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the length
     */
    public byte getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(byte length) {
        this.length = length;
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(byte[] value) {
        this.value = value;
    }
}
