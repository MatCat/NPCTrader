package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcManagerCommand extends npcCommand {

	String Permission = "";

	public npcManagerCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
		// TODO Auto-generated constructor stub
	}

	public Boolean Run(String Permission) {
		if (!super.Run()) {
			return false;
		}
		if (plugin.UsePermissions) {
			// System.out.println("Checking Permissions");
			if (plugin.Permissions.has(player, "npc.admin")) {
				return true;
			}
		}
		if (NPCDB.CheckOwner(NPCID, player.getName())) {
			return true;
		}
		if (!NPCDB.CheckManager(NPCID, player.getName())) {
			sender.sendMessage("You must be a manager or owner to do that.");
			return false;
		} else if (Permission != "") {
			if (!NPCDB.CheckManagerPermission(NPCID, player.getName(),
					Permission)) {
				sender.sendMessage("You do not have sufficent permissions to do that.");
				return false;
			} else {
				return true;
			}

		}
		return true;

	}
}
