package me.marcolvr.commons.exceptions;

import me.marcolvr.commons.net.Packet;

public class InvalidPacketException extends Exception {

    public InvalidPacketException(Packet p, Packet found){
        System.out.printf("Excepted %s but found %s", p.toString(), found==null ? "an invalid value" : found.toString());
    }

    public InvalidPacketException(){
    }
}
