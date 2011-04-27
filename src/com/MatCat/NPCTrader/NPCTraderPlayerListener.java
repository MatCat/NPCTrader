package com.MatCat.NPCTrader;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.martin.bukkit.npclib.NPCEntity;


public class NPCTraderPlayerListener extends PlayerListener {
	public static NPCTrader plugin;

	public NPCTraderPlayerListener(NPCTrader instance) {
		plugin = instance;
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		Location pl = event.getTo();
		for (String NPCID : plugin.npcm.NPCMap().keySet()) {
			NPCEntity npc = plugin.npcm.getNPC(NPCID);
			Player ep = event.getPlayer();
			// Player[] pl = plugin.getServer().getOnlinePlayers();

			// for (Player player : pl) {
			// X = player.getLocation().getX();
			// Y = player.getLocation().getY();
			// Z = player.getLocation().getZ();
			//
			// }
			double X = ep.getLocation().getX();
			double Y = ep.getLocation().getY();
			double Z = ep.getLocation().getZ();

			if (distance(X, Y, Z, npc.locX, npc.locY, npc.locZ) < 25) {
				 //System.out.println("NPC " + npc.displayName
				//		 + " within 25 blocks... Logic to be determained...");
				if (npc.ReDraw) {
					 System.out.println("NPC " + npc.displayName
					 + " within 25 blocks... revisualizing");
					plugin.npcm.ReDraw(npc.ID);
					// NPCTraderMySQL NPCDB = new NPCTraderMySQL();
/*					boolean dbc = plugin.NPCDB.dbConnect();
					plugin.NPCDB.SetupNPC(Integer.parseInt(NPCID), plugin,
							plugin.getServer().getWorlds());
					plugin.NPCDB.dbClose();*/
					npc.ReDraw = false;
				}

			}
		}
	}

	public double distance(double X1, double Y1, double Z1, double X2,
			double Y2, double Z2) {
		double dx = X1 - X2; // horizontal difference
		double dy = Y1 - Y2; // vertical difference
		double dz = Z1 - Z2; // vertical difference
		double dist = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz)); // distance
																	// using
																	// Pythagoras
																	// theorem
		return dist;
	}

}
