package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


import com.MatCat.NPCTrader.NPCTrader;

public class npcRenameCommand extends npcOwnerCommand {

	public npcRenameCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage("Usage: /npc rename <NPC Name>");
			sender.sendMessage("");
			sender.sendMessage("[<NPC Name>] Name of NPC.");
			return true;
		}

		try {
			NPCDB.SetName(args[1], NPCID);
			npc.setName(args[1]);
			sender.sendMessage("NPC Renamed.");
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			sender.sendMessage(ChatColor.RED + "Invalid NPC NAME");
			sender.sendMessage("Usage: /npc rename <NPC Name>");
			sender.sendMessage("");
			sender.sendMessage("[<NPC Name>] Name of NPC.");
			return true;
		}

	}
}
