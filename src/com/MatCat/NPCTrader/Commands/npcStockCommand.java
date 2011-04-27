package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.NPCTrader;

public class npcStockCommand extends npcManagerCommand {

	public npcStockCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanStock")) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc stock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to stock");
			sender.sendMessage("[<Quantity>] Quantity of item to stock.");

			return true;
		}
		String[] itemArgs;
		try {
			itemArgs = NPCDB.GetSlotItem(NPCID, Integer.parseInt(args[1]))
					.split("[^a-zA-Z0-9]");
		} catch (NumberFormatException e1) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc stock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to stock");
			sender.sendMessage("[<Quantity>] Quantity of item to stock.");
			return true;

		}
		int itemId;
		try {
			args[2] = Integer.toString(Math.abs(Integer.parseInt(args[2])));
		} catch (Exception c) {

		}
		try {
			itemId = ItemDB.get(itemArgs[0]);
		} catch (Exception e) {
			return false;
		}

		byte itemData;
		try {
			itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc stock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to stock");
			sender.sendMessage("[<Quantity>] Quantity of item to stock.");
			return true;
		}
		try {
			if (Integer.parseInt(args[2]) == 0) {
				sender.sendMessage("You must specify a quantity.");
				return true;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (plugin.PlayerItemCount(player, itemId, itemData) >= Integer
					.parseInt(args[2])) {

				if ((NPCDB.GetMaxStock(NPCID, plugin) - NPCDB.GetStock(NPCID,
						Integer.parseInt(args[1]))) < Integer.parseInt(args[2])) {
					sender.sendMessage("Not enough unit space.");
					return true;
				} else {
					plugin.removeItems(player, itemId, itemData,
							Integer.parseInt(args[2]));
					NPCDB.AddStock(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]));
					sender.sendMessage("Stocked.");
					return true;
				}
			} else {
				sender.sendMessage("You do not have enough.");
				return true;
			}
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc stock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to stock");
			sender.sendMessage("[<Quantity>] Quantity of item to stock.");
			return true;

		}

	}
}
