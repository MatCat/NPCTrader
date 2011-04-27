package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcRemoveManagerCommand extends npcManagerCommand {

	public npcRemoveManagerCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("")) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("Usage: /npc removemanager <Player Name>");
			sender.sendMessage("");
			sender.sendMessage("[<Player Name>] Name of player.");
			sender.sendMessage("");
			sender.sendMessage("Can be partial if they are online.");
			return true;
		}
		if (NPCDB.CheckManager(NPCID, plugin.expandName(args[1]))) {
			Boolean NPCOID = NPCDB.NPCRemoveManager(NPCID,
					plugin.expandName(args[1]));
			if (NPCOID) {
				sender.sendMessage("Manager removed.");
				return true;
			} else {
				sender.sendMessage("Database error while trying to remove manager.");
				return true;
			}
		} else {
			sender.sendMessage("Player is not a manager.");
			return true;
		}

	}

}
