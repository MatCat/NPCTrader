package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcAddOwnerCommand extends npcOwnerCommand {

	public npcAddOwnerCommand(NPCTrader plugin, CommandSender sender,
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
			sender.sendMessage("Usage: /npc addowner <Player Name>");
			sender.sendMessage("");
			sender.sendMessage("[<Player Name>] Name of player.");
			sender.sendMessage("");
			sender.sendMessage("Can be partial if they are online.");
			return true;
		}
		if (!plugin.CanCreate(player.getName())) {
			sender.sendMessage("You have reached the limit of NPC's you may own.");
			return true;
		}

		if (!NPCDB.CheckOwner(NPCID, plugin.expandName(args[1]))) {
			int NPCOID = NPCDB.NPCAddOwner(NPCID, plugin.expandName(args[1]));
			if (NPCOID != 0) {

				sender.sendMessage("Owner added.");
				return true;
			} else {

				sender.sendMessage("Database error adding owner.");
				return true;
			}
		} else {

			sender.sendMessage("That person is already an owner.");
			return true;
		}

	}

}
