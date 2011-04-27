package com.MatCat.NPCTrader.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.martin.bukkit.npclib.NPCEntity;


import com.MatCat.NPCTrader.NPCTrader;
import com.nijiko.coelho.iConomy.iConomy;

public class npcCreateCommand extends npcCommand {

	public npcCreateCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, commandLabel, args, PermissionNode, bNPCInteract);
		// TODO Auto-generated constructor stub
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}

		if (args.length < 4) {
			sender.sendMessage("Usage: /npc create <Name> <ItemSlots> <Units>");
			sender.sendMessage("");
			sender.sendMessage("[<Name>] Name of NPC");
			sender.sendMessage("[<ItemSlots>] Amount of slots your store has");
			sender.sendMessage("[<Units>] How many units per slot");
			return true;
		}

		double PlayerMoney = 0;
		int ItemSlots = 0;
		int Units = 0;
		Double SlotPrice = plugin.getSettings().Prices("ItemSlot");
		Double UnitPrice = plugin.getSettings().Prices("Unit");
		try {
			if (!plugin.CanCreate(player.getName())) {
				sender.sendMessage("You have reached the limit of NPC's you may own.");
				return true;
			}
			PlayerMoney = plugin.GetPlayerBalance(player.getName());
		} catch (Exception e) {
			System.out.println(e.toString());
			sender.sendMessage("iConomy and NPC Trader out of sync.");
			return true;
		}
		try {
			ItemSlots = Integer.parseInt(args[2].trim());
			Units = Integer.parseInt(args[3].trim());
			if (ItemSlots < 1) {
				sender.sendMessage("You must specify atleast 1 Item Slot");
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			sender.sendMessage(ChatColor.RED + "Inproper arguments");
			sender.sendMessage("Usage: /npc create <Name> <ItemSlots> <Units>");
			sender.sendMessage("");
			sender.sendMessage("[<Name>] Name of NPC");
			sender.sendMessage("[<ItemSlots>] Amount of slots your store has");
			sender.sendMessage("[<Units>] How many units per slot");

			return true;
		}
		Double Total = ((Units * UnitPrice) * ItemSlots)
				+ (ItemSlots * SlotPrice);

		if (Total > PlayerMoney) {
			// TODO: Handle Not enough money
			sender.sendMessage("Player doesnt have enough money " + Total + " "
					+ PlayerMoney);
			return true;
		}

		int NPCID;
		NPCID = NPCDB.CreateNPC(args[1], Integer.parseInt(args[2].trim()),
				Integer.parseInt(args[3].trim()), player.getName(), player
						.getWorld().getName());
		if (NPCID == 0) {
			sender.sendMessage("Database failure trying to add NPC.");
			return true;
		}
		Boolean result = NPCDB.NPCMove(NPCID, (float) l.getX(),
				(float) l.getY(), (float) l.getZ(), l.getPitch(), l.getYaw());
		if (result == false) {
			sender.sendMessage("Database failure trying to move NPC.");
			return true;
		}
		int iresult;
		iresult = NPCDB.NPCAddOwner(NPCID, player.getName());
		if (iresult == 0) {
			sender.sendMessage("Database failure trying to add Owner.");
			return true;
		}

		NPCEntity hnpc = plugin.npcm.spawnNPC(plugin.getSettings().NPC("Prefix")
				+ args[1] + plugin.getSettings().NPC("Suffix"), l, Integer.toString(NPCID));
		plugin.SubtractBalance(player.getName(), Total);
		sender.sendMessage("NPC Created for the cost of "
				+ iConomy.getBank().format(Total));
		return true;
	}
}
