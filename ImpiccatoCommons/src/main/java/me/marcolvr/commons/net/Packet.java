package me.marcolvr.commons.net;

import java.util.HashMap;


public enum Packet {
    HEARTBEAT(0x00, "HEARTBEAT"),
    LOBBY_UPDATE(0x02, "LOBBY_UPDATE"),

    SET_USERNAME(0x10, "SET_USERNAME"),
    SET_ROOM(0x11, "SET_ROOM"),
    SET_WORD(0x12, "SET_WORD"),

    WAIT_WORD(0x20, "WAIT_WORD"),
    UPDATE_WORD(0x21, "UPDATE_WORD"),
    ADD_LETTER(0x22, "ADD_LETTER"),

    END_GAME(0x50, "END_GAME"),

    LOG_EVENT(0x0B,"LOG_EVENT"),

    CREATED(0x0A, "CREATED"),
    OK(0x0D, "OK"),
    INVALID(0x0F, "INVALID"),
    ERROR(0x0E, "ERROR");

    int packetId;
    String name;

    Packet(int i, String name) {
        packetId=i;
        this.name = name;
    }

    private static HashMap<Integer, Packet> packets = new HashMap<>();
    private static HashMap<String, Packet> names = new HashMap<>();

    public int getId(){
        return packetId;
    }
    public String toString(){
        return name;
    }

    public static Packet getFromId(int id){
        for (Packet p: Packet.values()) {
            packets.put(p.packetId, p);
        }
        return packets.getOrDefault(id, null);
    }
}
