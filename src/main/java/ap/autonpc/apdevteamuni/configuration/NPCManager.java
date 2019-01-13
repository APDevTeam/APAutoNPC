package ap.autonpc.apdevteamuni.configuration;

import java.util.ArrayList;

public class NPCManager {

    private ArrayList<NPC> npcs = new ArrayList<NPC>();

    public NPCManager() {

    }

    public void registerNPC(NPC npc) {
        npcs.add(npc);
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public NPC getNPC(int id) {
        for(NPC npc : npcs) {
            if(npc.getID() == id) {
                return npc;
            }
        }
        return null;
    }

    public NPC getNPC(String name) {
        for(NPC npc : npcs) {
            if(npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }
}
