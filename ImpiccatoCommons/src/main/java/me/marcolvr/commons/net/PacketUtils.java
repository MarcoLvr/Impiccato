package me.marcolvr.commons.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PacketUtils {

    public static byte[] writeInt(int num){
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.putInt(num);
        return buff.array();
    }

    public static byte[] writeString(String str) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = str.length();
        out.write(writeInt(length));
        out.write(str.getBytes(StandardCharsets.US_ASCII));
        return out.toByteArray();
    }

    public static byte[] writeChar(char c){
        return new byte[]{(byte) c};
    }

    public static char readChar(byte[] msg, int offset){
        return (char) msg[offset];
    }

    public static byte writeBoolean(boolean bool){
        return (byte) (bool ? 0 : 1);
    }

    public static int readInt(byte[] msg, int offset){
        byte[] fromOffset = new byte[msg.length-offset];
        for (int i = offset; i<offset+4; i++){
            fromOffset[i-offset]=msg[i];
        }
        ByteBuffer buff = ByteBuffer.wrap(fromOffset);
        return buff.getInt();
    }

    public static String readString(byte[] msg, int offset){
        int length = readInt(msg, offset);
        return new String(msg, offset+4, length);
    }

    public static boolean readBoolean(byte[] msg, int offset){
        return msg[offset] == 0;
    }

    public static byte[] genMessage(int packet, byte[] ...mess) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(packet);
        if(mess!=null){
            for (byte[] msg : mess) {
                out.write(msg);
            }
        }
        out.write(0xE);
        return out.toByteArray();
    }

}
