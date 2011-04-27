package com.MatCat.NPCTrader;

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
		double X = event.getTo().getX();
		double Y = event.getTo().getY();
		double Z = event.getTo().getZ();
		for (String NPCID : plugin.npcm.NPCMap().keySet()) {
			NPCEntity npc = plugin.npcm.getNPC(NPCID);
			double X2 = npc.getBukkitEntity().getLocation().getX();
			double Y2 = npc.getBukkitEntity().getLocation().getY();
			double Z2 = npc.getBukkitEntity().getLocation().getZ();
			// We need to go through every player on and see if ANYONE is inside
			// 100 block radious
			Player ep = event.getPlayer();
			// Player[] pl = plugin.getServer().getOnlinePlayers();

			// for (Player player : pl) {
			// X = player.getLocation().getX();
			// Y = player.getLocation().getY();
			// Z = player.getLocation().getZ();
			//
			// }
			X = ep.getLocation().getX();
			Y = ep.getLocation().getY();
			Z = ep.getLocation().getZ();

			if (distance(X, Y, Z, X2, Y2, Z2) < 25) {
				if (npc.ReDraw) {
					// System.out.println("NPC " + npc.getName()
					// + " within 100 blocks... revisualizing");
					plugin.npcm.ReDraw(npc.ID);
					// NPCTraderMySQL NPCDB = new NPCTraderMySQL();
/*					boolean dbc = plugin.NPCDB.dbConnect();
					plugin.NPCDB.SetupNPC(Integer.parseInt(NPCID), plugin,
							plugin.getServer().getWorlds());
					plugin.NPCDB.dbClose();*/
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
