package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.NPCTrader;

public class npcUnStockCommand extends npcManagerCommand {

	public npcUnStockCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanUnStock")) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");

			return true;
		}

		String[] itemArgs;
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
			itemArgs = NPCDB.GetSlotItem(NPCID, Integer.parseInt(args[1]))
					.split("[^a-zA-Z0-9]");
		} catch (NumberFormatException e2) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");
			return true;
		}
		int itemId = 0;
		try {
			args[2] = Integer.toString(Math.abs(Integer.parseInt(args[2])));
		} catch (Exception c) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");
			return true;

		}
		try {
			itemId = ItemDB.get(itemArgs[0]);
		} catch (Exception e1) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");
			return true;

		}
		byte itemData;
		try {
			itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;
		} catch (NumberFormatException e1) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");
			return true;

		}

		try {
			if (NPCDB.CheckUnitCount(NPCID, Integer.parseInt(args[1])) >= Integer
					.parseInt(args[2])) {

				try {
					player.getInventory().addItem(
							new ItemStack[] { new ItemStack(itemId, Integer
									.parseInt(args[2]), (short) 0,
									(byte) itemData) });
					NPCDB.RemoveStock(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]));
				} catch (Exception e) {
					e.printStackTrace();
				}
				sender.sendMessage("Items pulled from stock.");
				return true;
			} else {
				sender.sendMessage("Not enough in stock.");
				return true;
			}
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "INVALID ARGUMENTS");
			sender.sendMessage("Usage: /npc unstock <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot number to unstock");
			sender.sendMessage("[<Quantity>] Quantity of item to unstock.");
			return true;

		}

	}
}
