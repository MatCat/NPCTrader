package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.NPCTrader;

public class npcSetupCommand extends npcManagerCommand {

	public npcSetupCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run("CanPrice")) {
			return true;
		}
		if (args.length < 8) {
			sender.sendMessage("Usage: /npc setup <Slot> <Item> <LotQty> <BuyPrice> <SellPrice> <Buy> <Sell>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Slot to setup");
			sender.sendMessage("[<Item>] Name or ID, use : for wool colors");
			sender.sendMessage("[<LotQty>] Number of Items in lot");
			sender.sendMessage("[<BuyPrice>] Price to sell for");
			sender.sendMessage("[<SellPrice>] Price to buy at");
			sender.sendMessage("[<buy/sell>] true or false to buy or sell item.");

			return true;
		}
		int SlotID;
		try {
			SlotID = Integer.parseInt(args[1]);
		} catch (Exception e) {
			sender.sendMessage("Invalid Slot ID");
			return true;
		}
		String[] itemArgs;
		try {
			itemArgs = args[2].split("[^a-zA-Z0-9]");
		} catch (Exception e) {
			sender.sendMessage("Invalid Item/Data format");
			return true;
		}
		int itemId;
		try {
			itemId = ItemDB.get(itemArgs[0]);
		} catch (Exception e) {
			sender.sendMessage(e.getMessage());
			return true;
		}
		byte itemData = 0;
		try {
			itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;
		} catch (Exception e) {
			sender.sendMessage("Invalid Item Data Value");
			return true;
		}

		int LotQTY;
		try {
			LotQTY = Integer.parseInt(args[3]);
		} catch (Exception e) {
			sender.sendMessage("Invalid Lot Quantity");
			return true;
		}
		int BuyPrice;
		try {
			BuyPrice = Integer.parseInt(args[4]);
		} catch (Exception e) {
			sender.sendMessage("Invalid selling price");
			return true;
		}
		int SellPrice;
		try {
			SellPrice = Integer.parseInt(args[5]);
		} catch (Exception e) {
			sender.sendMessage("Invalid Purchasing Price");
			return true;
		}
		boolean Buy;
		try {
			Buy = Boolean.parseBoolean(args[6]);
		} catch (Exception e) {
			sender.sendMessage("Invalid Selling flag, use true or false");
			return true;
		}
		boolean Sell;
		try {
			Sell = Boolean.parseBoolean(args[7]);
		} catch (Exception e) {
			sender.sendMessage("Invalid Purchasing flag, use true or false");
			return true;
		}
		if (NPCDB.GetStock(NPCID, SlotID) == 0) {
			if (NPCDB.SlotSetup(NPCID, SlotID, itemId, itemData, LotQTY,
					BuyPrice, SellPrice, Buy, Sell)) {
				sender.sendMessage("Slot Setup");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Improper Arguments");
				sender.sendMessage("Usage: /npc setup <Slot> <Item> <LotQty> <BuyPrice> <SellPrice> <Buy> <Sell>");
				sender.sendMessage("");
				sender.sendMessage("[<Slot>] Slot to setup");
				sender.sendMessage("[<Item>] Name or ID, use : for wool colors");
				sender.sendMessage("[<LotQty>] Number of Items in lot");
				sender.sendMessage("[<BuyPrice>] Price to sell for");
				sender.sendMessage("[<SellPrice>] Price to buy at");
				sender.sendMessage("[<buy/sell>] true or false to buy or sell item.");

				return true;
			}
		} else {
			sender.sendMessage("You must first empty the stock before changing item type.");
			return true;
		}

	}
}
