package com.MatCat.NPCTrader.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


import com.MatCat.NPCTrader.NPCTrader;

public class npcMoveCommand extends npcManagerCommand {

	public npcMoveCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanMove")) {
			return true;
		}
		Boolean nS = NPCDB.NPCMove(NPCID, (float) l.getX(), (float) l.getY(),
				(float) l.getZ(), l.getPitch(), l.getYaw());
		if (nS) {
			//Location l = new Location();
			plugin.npcm.moveNPC(Integer.toString(NPCID), l);

			sender.sendMessage("NPC Moved.");
			return true;
		} else {

			sender.sendMessage("Error moving NPC.");
			return true;
		}

	}
}
