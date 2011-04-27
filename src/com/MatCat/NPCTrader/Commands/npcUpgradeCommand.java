package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;
import com.nijiko.coelho.iConomy.iConomy;

public class npcUpgradeCommand extends npcOwnerCommand {

	public npcUpgradeCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		if (args.length < 3) {
			sender.sendMessage("Usage: /npc upgrade <Slots> <Units>");
			sender.sendMessage("");
			sender.sendMessage("[<Slots>] Add <slots> to your slots count.");
			sender.sendMessage("[<Units>] Add <Units> to your unit count.");
			sender.sendMessage("Can be 0 to only upgrade one or the other.");
			return true;
		}

		double PlayerMoney = 0;
		int ItemSlots = 0;
		int Units = 0;
		Double SlotPrice = plugin.getSettings().Prices("ItemSlot");
		Double UnitPrice = plugin.getSettings().Prices("Unit");
		try {
			PlayerMoney = plugin.GetPlayerBalance(player.getName());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			sender.sendMessage("iConomy and NPC Trader out of sync.");
			return true;
		}
		try {
			ItemSlots = Math.abs(Integer.parseInt(args[1].trim()));
			Units = Math.abs(Integer.parseInt(args[2].trim()));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
			sender.sendMessage(ChatColor.RED + "Inproper Arguments");
			sender.sendMessage("Usage: /npc upgrade <Slots> <Units>");
			sender.sendMessage("");
			sender.sendMessage("[<Slots>] Add <slots> to your slots count.");
			sender.sendMessage("[<Units>] Add <Units> to your unit count.");
			sender.sendMessage("Can be 0 to only upgrade one or the other.");

			return true;
		}
		double Total = 0.0;
		try {
			Total = ((Units * UnitPrice) * (ItemSlots + NPCDB
					.GetTotalSlots(NPCID))) + (ItemSlots * SlotPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (Total > PlayerMoney) {
			sender.sendMessage("You do not have enough money for that!");
			return true;
		} else {
			for (int Count = NPCDB.GetTotalSlots(NPCID) + 1; Count <= NPCDB
					.GetTotalSlots(NPCID) + ItemSlots; ++Count) {
				NPCDB.AddSlot(NPCID, Count);
			}
		}

		if (NPCDB.Upgrade(NPCID, ItemSlots, Units)) {
			plugin.SubtractBalance(player.getName(), Total);
			sender.sendMessage("Upgraded for "
					+ iConomy.getBank().format(Total));
			return true;
		} else {
			sender.sendMessage("Database error trying to upgrade.");
			return true;
		}

	}
}
