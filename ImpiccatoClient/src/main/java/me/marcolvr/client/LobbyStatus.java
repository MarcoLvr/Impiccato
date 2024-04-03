package me.marcolvr.client;

import java.util.HashMap;

public enum LobbyStatus {
    WAITING_FOR_PLAYERS(0x01),
    STARTING(0x02),
    SHOW_GAME_SCREEN(0x03);


    int packetId;

    LobbyStatus(int i) {
        packetId=i;
    }

    private static HashMap<Integer, LobbyStatus> packets = new HashMap<>();

    public int getId(){
        return packetId;
    }

    public static LobbyStatus getFromId(int id){
        for (LobbyStatus p: LobbyStatus.values()) {
            packets.put(p.packetId, p);
        }
        return packets.get(id);
    }
}
