package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcListOwnersCommand extends npcOwnerCommand {

	public npcListOwnersCommand(NPCTrader plugin, CommandSender sender,
			Command c, String commandLabel, String[] args,
			String PermissionNode, Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		sender.sendMessage("Owners: " + NPCDB.GetOwnerList(NPCID));
		return true;
	}
}
