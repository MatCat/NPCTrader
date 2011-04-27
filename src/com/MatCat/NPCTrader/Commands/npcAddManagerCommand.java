package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcAddManagerCommand extends npcOwnerCommand {

	public npcAddManagerCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("Usage: /npc addmanager <Player Name>");
			sender.sendMessage("");
			sender.sendMessage("[<Player Name>] Name of player.");
			sender.sendMessage("");
			sender.sendMessage("Can be partial if they are online.");

			return true;
		}
		if (!NPCDB.CheckOwner(NPCID, plugin.expandName(args[1]))) {
			if (!NPCDB.CheckManager(NPCID, plugin.expandName(args[1]))) {
				Integer NPCOID = NPCDB.NPCAddManager(NPCID,
						plugin.expandName(args[1]));
				if (NPCOID != 0) {
					sender.sendMessage("Manager Added.");
					return true;
				} else {
					sender.sendMessage("Database error adding manager.");
					return true;
				}
			} else {
				sender.sendMessage("That person is already a manager.");
				return true;
			}
		} else {
			sender.sendMessage("That person is already an owner.");
			return true;
		}

	}
}
