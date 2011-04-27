package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.MatCat.NPCTrader.ItemDB;
import com.MatCat.NPCTrader.NPCTrader;
import com.nijiko.coelho.iConomy.iConomy;

public class npcSellCommand extends npcCommand {

	public npcSellCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc sell <Slot> <Quantity>");
			sender.sendMessage("");
			sender.sendMessage("[<Slot>] Item Slot to sell");
			sender.sendMessage("[<Quantity>] Number of lot's to sell");

			return true;
		}

		int itemId;
		byte itemData;
		double PlayerMoney;
		Double Total;
		try {
			args[2] = Integer.toString(Math.abs(Integer.parseInt(args[2])));
		} catch (Exception c) {

		}
		try {
			String[] itemArgs = NPCDB.GetSlotItem(NPCID,
					Integer.parseInt(args[1])).split("[^a-zA-Z0-9]");
			itemId = ItemDB.get(itemArgs[0]);
			itemData = itemArgs.length > 1 ? Byte.parseByte(itemArgs[1])
					: (byte) 0;
			PlayerMoney = plugin.GetPlayerBalance(player.getName());
			Total = NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
					Integer.parseInt(args[2]), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sender.sendMessage("Invalid Slot Number");
			return true;
		}

		if ((NPCDB.GetMaxStock(NPCID, plugin) - NPCDB.GetStock(NPCID,
				Integer.parseInt(args[1]))) >= (Integer.parseInt(args[2]) * NPCDB
				.GetSlotLot(NPCID, Integer.parseInt(args[1])))) {
			System.out.println(NPCDB.GetBanker(NPCID));
			if (plugin.GetPlayerBalance(NPCDB.GetBanker(NPCID)) >= NPCDB
					.GetTotalCost(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]), false)) {
				try {
					if (Integer.parseInt(args[2]) == 0) {
						sender.sendMessage("You must specify a quantity.");
						return true;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (NPCDB.IsBS(NPCID, Integer.parseInt(args[1]), false)) {
					if (plugin.PlayerItemCount(player, itemId, itemData) >= (Integer
							.parseInt(args[2]))
							* NPCDB.GetSlotLot(NPCID, Integer.parseInt(args[1]))) {

						plugin.removeItems(
								player,
								itemId,
								itemData,
								Integer.parseInt(args[2])
										* NPCDB.GetSlotLot(NPCID,
												Integer.parseInt(args[1])));

						NPCDB.AddStock(
								NPCID,
								Integer.parseInt(args[1]),
								Integer.parseInt(args[2])
										* NPCDB.GetSlotLot(NPCID,
												Integer.parseInt(args[1])));

						plugin.AddBalance(player.getName(), Total);
						plugin.SubtractBalance(NPCDB.GetBanker(NPCID), Total);
						Player pB = plugin.getServer().getPlayer(
								plugin.expandName(NPCDB.GetBanker(NPCID)));
						try {
							NPCDB.AddTransaction(
									NPCID,
									player.getName(),
									pB.getName(),
									itemId,
									itemData,
									(Integer.parseInt(args[2]) * NPCDB
											.GetSlotLot(NPCID,
													Integer.parseInt(args[1]))),
									NPCDB.GetTotalCost(NPCID,
											Integer.parseInt(args[1]), 1, false),
									Total, false);
						} catch (Exception e) {

						}
						try {
							pB.sendMessage("You just spent "
									+ iConomy.getBank().format(Total)
									+ " from NPC "
									+ npc.displayName
									+ " for the purchase of "
									+ (Integer.parseInt(args[2]) * NPCDB
											.GetSlotLot(NPCID,
													Integer.parseInt(args[1])))
									+ " "
									+ plugin.GetMaterialName(itemId, itemData));
						} catch (Exception e) {
						}

						sender.sendMessage(NPCDB.NPCMessage(NPCID, "SellMSG",
								(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(
										NPCID, Integer.parseInt(args[1]))),
								itemId, itemData, Total, plugin));

						return true;
					} else {
						sender.sendMessage(NPCDB.NPCMessage(NPCID,
								"InsQtySellMsg",
								(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(
										NPCID, Integer.parseInt(args[1]))),
								itemId, itemData, Total, plugin));

						return true;
					}
				} else {

					sender.sendMessage("We are not currently purchasing that.");
					return true;
				}

			} else {
				sender.sendMessage(NPCDB.NPCMessage(NPCID, "InsFundsSellMsg",
						(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
								Integer.parseInt(args[1]))), itemId, itemData,
						NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
								Integer.parseInt(args[2]), false), plugin));
				return true;
			}
		} else {

			sender.sendMessage(NPCDB.NPCMessage(NPCID, "NotStockingMsg",
					(Integer.parseInt(args[2]) * NPCDB.GetSlotLot(NPCID,
							Integer.parseInt(args[1]))), itemId, itemData,
					NPCDB.GetTotalCost(NPCID, Integer.parseInt(args[1]),
							Integer.parseInt(args[2]), false), plugin));
			return true;
		}

	}
}
