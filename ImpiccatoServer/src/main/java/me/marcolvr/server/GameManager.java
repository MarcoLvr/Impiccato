package me.marcolvr.server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {

    @Getter
    private static List<Room> rooms = new ArrayList<>();

    @Getter
    private static List<Client> clients = new ArrayList<>();

    public static boolean isUsernameUsed(String username){
        for (Client c : clients){
            if(c.getUsername() != null && c.getUsername().equals(username)) return true;
        }
        return false;
    }

    public static boolean roomExist(String code){
        return rooms.stream().filter(room -> room.code.equalsIgnoreCase(code.toUpperCase())).findFirst().orElse(null) != null;
    }

    public static Room createRoom(String code, Client client){
        Room r = new Room(client, code.toUpperCase());
        rooms.add(r);
        return r;
    }

    public static boolean isStarted(String code){
        return rooms.stream().filter(room -> room.code.equalsIgnoreCase(code.toUpperCase())).findFirst().orElse(null).started;

    }

    public static void deleteRoom(Room room){
        rooms.remove(room);
        System.out.println(String.format("[%s] Room deleted.", room.code));
    }

    public static Room joinRoom(String code, Client client){
        return rooms.stream().filter(room -> room.code.equalsIgnoreCase(code.toUpperCase())).findFirst().orElse(null).addPlayer(client);
    }

    public static void unregister(Client client){
        if(client.getRoom() != null) client.getRoom().removePlayer(client);
        clients.remove(client);
        if(client.username != null){
            System.out.println("[SYSTEM] " + client.username + " disconnected [" + client.getAddress().toString().replace("/", "")+ "]");
        }
    }

}
