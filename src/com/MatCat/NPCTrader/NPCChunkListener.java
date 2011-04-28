package com.MatCat.NPCTrader;
// I dont think this is even getting called.
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.martin.bukkit.npclib.NPCEntity;

public class NPCChunkListener extends WorldListener {
	public static NPCTrader plugin;
	public NPCChunkListener(NPCTrader instance) {
		plugin = instance;
		System.out.println("NPCTrader: Chunk Listener is running...");
	}
	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		System.out.println("Chunk Load Event: " + event.getChunk().toString());
		// We need to see if we have any NPC's in the new chunk.
		Iterator i = plugin.NPCL.keySet().iterator();
		while (i.hasNext()) {
			String NPCID = (String) i.next();
			NPCObj n = (NPCObj) plugin.NPCL.get(NPCID);
			Location l = new Location(plugin.getServer().getWorld(n.world), n.x, n.y, n.z);
			
			if (l.getWorld().getChunkAt(l) == event.getChunk()) {
				// NPC Is in chunk being loaded... We need to reload it.
				plugin.NPCDB.SetupNPC(Integer.valueOf(NPCID), plugin, plugin.getServer().getWorlds());
				System.out.println("NPC " + NPCID + " being re-drawn at Chunk " + event.getChunk().toString());
			}
		}
	}
	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		System.out.println("Chunk UnLoad Event: " + event.getChunk().toString());
		// We need to see if we have any NPC's in the chunk.
		Iterator i = plugin.NPCL.keySet().iterator();
		while (i.hasNext()) {
			String NPCID = (String) i.next();
			NPCObj n = (NPCObj) plugin.NPCL.get(NPCID);
			Location l = new Location(plugin.getServer().getWorld(n.world), n.x, n.y, n.z);
			
			if (l.getWorld().getChunkAt(l) == event.getChunk()) {
				// NPC Is in chunk being unloaded... We need to unload it.
				plugin.npcm.despawnById(NPCID);
				System.out.println("NPC " + NPCID + " being removed at Chunk " + event.getChunk().toString());
			}
		}
		
	}

}
