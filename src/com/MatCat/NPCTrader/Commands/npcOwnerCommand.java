package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcOwnerCommand extends npcCommand {

	public npcOwnerCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
		// TODO Auto-generated constructor stub
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (plugin.UsePermissions) {
			// System.out.println("Checking Permissions");
			if (plugin.Permissions.has(player, "npc.admin")) {
				return true;
			}
		}

		if (!NPCDB.CheckOwner(NPCID, player.getName())) {
			sender.sendMessage("You must be an owner to do that.");
			return false;
		} else {
			return true;
		}
	}

}
