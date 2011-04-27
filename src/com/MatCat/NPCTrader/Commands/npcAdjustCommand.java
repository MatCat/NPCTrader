package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;

public class npcAdjustCommand extends npcManagerCommand {

	public npcAdjustCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanPrice")) {
			return true;
		}
		if (args.length < 4) {

			sender.sendMessage("Usage: /npc adjust <Slot> <Parameter> <Value>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Item Slot to adjust");
			sender.sendMessage("[<Parameter>] item, buyprice, sellprice, buy, sell");
			sender.sendMessage("[<Value>] Value of parameter.  Use true/false for buy and sell.");

			return true;
		}

		if (args[2].equals("item")) {
			if (NPCDB.CheckUnitCount(NPCID, Integer.parseInt(args[1])) != 0) {

				sender.sendMessage("Cannot change item type while stocked with items.");
				return true;
			}
		}
		if (NPCDB
				.SlotAdjust(NPCID, Integer.parseInt(args[1]), args[2], args[3])) {
			sender.sendMessage("Slot Adjusted.");
			return true;
		} else {

			sender.sendMessage(ChatColor.RED + "Invalid Arguments");
			sender.sendMessage("Usage: /npc adjust <Slot> <Parameter> <Value>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Item Slot to adjust");
			sender.sendMessage("[<Parameter>] item, buyprice, sellprice, buy, sell");
			sender.sendMessage("[<Value>] Value of parameter.  Use true/false for buy and sell.");

			return true;
		}

	}
}
