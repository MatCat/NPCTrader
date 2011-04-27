package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcSetManagerCommand extends npcOwnerCommand {

	public npcSetManagerCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 4) {
			sender.sendMessage("Usage: /npc setmanager <Player> <Permission> <True/False>");
			sender.sendMessage("");
			sender.sendMessage("[<Player>] Player to change permissions for");
			sender.sendMessage("[<Permission>]  CanStock, CanUnstock, CanMove, CanPrice, CanText");
			sender.sendMessage("[<True/False>] Must be true or false");

			return true;
		}

		if (NPCDB.CheckManager(NPCID, plugin.expandName(args[1]))) {

			if (NPCDB.NPCSetManagerPermission(NPCID,
					plugin.expandName(args[1]), args[2],
					Boolean.parseBoolean(args[3]))) {
				sender.sendMessage("Permission Changed.");
				return true;
			} else {
				sender.sendMessage("Database error setting permission.");
				return true;
			}
		} else {

			sender.sendMessage("Player not on the manager list.");

			return true;
		}

	}
}
