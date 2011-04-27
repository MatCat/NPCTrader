package com.MatCat.NPCTrader;

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.martin.bukkit.npclib.NPCEntity;

public class NPCChunkListener extends WorldListener {
	public static NPCTrader plugin;
	public NPCChunkListener(NPCTrader instance) {
		plugin = instance;
	}
	public void OnChunkLoad(ChunkLoadEvent event) {
		for (String NPCID : plugin.npcm.NPCMap().keySet()) {
			NPCEntity npc = plugin.npcm.getNPC(NPCID);
			if (npc.getBukkitEntity().getWorld().getChunkAt(npc.getBukkitEntity().getLocation()) == event.getChunk()) {
				// NPC Is in chunk being loaded... We need to reload it.
				plugin.npcm.ReDraw(npc.ID);
				System.out.println("NPC " + npc.ID + " being re-drawn at Chunk " + event.getChunk().toString());
			}
		}
	}
	public void OnChunkUnload(ChunkUnloadEvent event) {
		
	}

}
