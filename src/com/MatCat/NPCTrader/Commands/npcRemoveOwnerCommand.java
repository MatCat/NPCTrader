package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcRemoveOwnerCommand extends npcOwnerCommand {

	public npcRemoveOwnerCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
		// TODO Auto-generated constructor stub
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("Usage: /npc removeowner <Player Name>");
			sender.sendMessage("");
			sender.sendMessage("[<Player Name>] Name of player.");
			sender.sendMessage("");
			sender.sendMessage("Can be partial if they are online.");
			return true;
		}
		if (NPCDB.CheckOwner(NPCID, plugin.expandName(args[1]))) {
			Boolean NPCOID = NPCDB.NPCRemoveOwner(NPCID,
					plugin.expandName(args[1]));
			if (NPCOID) {

				sender.sendMessage("Owner removed.");
				return true;
			} else {

				sender.sendMessage("Database error removing owner.");
				return true;
			}
		} else {

			sender.sendMessage("That person is not an owner.");
			return true;
		}

	}

}
