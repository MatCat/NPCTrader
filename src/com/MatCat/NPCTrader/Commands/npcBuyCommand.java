package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.NPCTrader;
import com.nijiko.coelho.iConomy.iConomy;

public class npcBuyCommand extends npcCommand {

	public npcBuyCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc buy <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Item Slot to purchase");
			sender.sendMessage("[<Quantity>] Number of lot's to purchase");

			return true;
		}
		int itemId;
		byte itemData;
		double PlayerMoney;
		double Total;
		try {
			args[2] = Integer.toString(Math.abs(Integer.parseInt(args[2])));
		} catch (Exception c) {

		}
		try {
			String[] itemArgs = NPCDB.GetSlotItem(NPCID,
					Integer.parseInt(args[1])).split("[^a-zA-Z0-9]");
			try {
				itemId = ItemDB.get(itemArgs[0]);
			} catch (Exception e) {
				sender.sendMessage("Invalid slot number.");
				return true;
			}
			itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;
			PlayerMoney = plugin.GetPlayerBalance(player.getName());
			Total = NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
					Integer.parseInt(args[2]), true);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			sender.sendMessage("Invalid slot number.");
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
		if (NPCDB.GetStock(NPCID, Integer.parseInt(args[1])) >= (Integer
				.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
				Integer.parseInt(args[1])))) {
			if (plugin.GetPlayerBalance(player.getName()) >= NPCDB
					.GetTotalCost(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]), true)) {
				if (NPCDB.IsBS(NPCID, Integer.parseInt(args[1]), true)) {

					player.getInventory().addItem(
							new ItemStack[] { new ItemStack(itemId, Integer
									.parseInt(args[2])
									* NPCDB.GetSlotLot(NPCID,
											Integer.parseInt(args[1])),
									(short) 0, (byte) itemData) });
					NPCDB.RemoveStock(
							NPCID,
							Integer.parseInt(args[1]),
							Integer.parseInt(args[2])
									* NPCDB.GetSlotLot(NPCID,
											Integer.parseInt(args[1])));
					plugin.SubtractBalance(player.getName(), Total);
					plugin.AddBalance(NPCDB.GetBanker(NPCID), Total);
					Player pB = plugin.getServer().getPlayer(
							plugin.expandName(NPCDB.GetBanker(NPCID)));
					try {
						NPCDB.AddTransaction(NPCID, player.getName(), pB
								.getName(), itemId, itemData, (Integer
								.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
								Integer.parseInt(args[1]))), NPCDB
								.GetTotalCost(NPCID, Integer.parseInt(args[1]),
										1, true), Total, true);
					} catch (Exception e) {

					}

					try {
						pB.sendMessage("You just recieved "
								+ iConomy.getBank().format(Total)
								+ " from NPC "
								+ npc.displayName
								+ " for the sale of "
								+ (Integer.parseInt(args[2]) * NPCDB
										.GetSlotLot(NPCID,
												Integer.parseInt(args[1])))
								+ " "
								+ plugin.GetMaterialName(itemId, itemData));
					} catch (Exception e) {
					}

					sender.sendMessage(NPCDB.NPCMessage(NPCID, "BuyMSG",
							(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(
									NPCID, Integer.parseInt(args[1]))), itemId,
							itemData, Total, plugin));

					return true;
				} else {
					player.sendMessage("We are not currently selling that.");
					return true;
				}

			} else {
				sender.sendMessage(NPCDB.NPCMessage(NPCID, "InsFundsBuyMsg",
						(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
								Integer.parseInt(args[1]))), itemId, itemData,
						NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
								Integer.parseInt(args[2]), true), plugin));
				return true;
			}
		} else {

			sender.sendMessage(NPCDB.NPCMessage(NPCID, "NotStockingMsg",
					(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
							Integer.parseInt(args[1]))), itemId, itemData,
					NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]), true), plugin));
			return true;
		}

	}
}
