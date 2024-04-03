package me.marcolvr.commons;

import java.util.HashMap;

public enum LogEventType {
    RIGHT_LETTER(0x01),
    WRONG_LETTER(0x02),
    PLAYER_EXIT(0x03),
    WORDMAKER_EXIT(0x04);


    int packetId;

    LogEventType(int i) {
        packetId=i;
    }

    private static HashMap<Integer, LogEventType> packets = new HashMap<>();

    public int getId(){
        return packetId;
    }

    public static LogEventType getFromId(int id){
        for (LogEventType p: LogEventType.values()) {
            packets.put(p.packetId, p);
        }
        return packets.get(id);
    }
}
