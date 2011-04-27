package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcBankerCommand extends npcOwnerCommand {

	public npcBankerCommand(NPCTrader plugin, CommandSender sender, Command c,
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
			sender.sendMessage("Usage: /npc banker <Player Name>");
			sender.sendMessage("");
			sender.sendMessage("[<Player Name>] Name of player.");
			sender.sendMessage("");
			sender.sendMessage("Can be partial if they are online.");
			sender.sendMessage("Player must be an owner.");
			return true;
		}
		if (NPCDB.CheckOwner(NPCID, plugin.expandName(args[1]))) {
			if (args[1].equalsIgnoreCase(player.getName())) {

				Boolean NPCOID = NPCDB.NPCSetBanker(NPCID,
						plugin.expandName(args[1]));
				if (NPCOID) {
					sender.sendMessage("Banker set.");
					return true;
				} else {
					sender.sendMessage("Database error setting banker.");
					return true;
				}
			} else {
				sender.sendMessage("You may only set yourself as banker!");
				return true;
			}
		} else {
			sender.sendMessage("Player is not an owner.");
			return true;
		}

	}
}
