package com.MatCat.NPCTrader.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.MatCat.NPCTrader.NPCTrader;
import com.nijiko.coelho.iConomy.iConomy;

public class npcRemoveCommand extends npcOwnerCommand {

	public npcRemoveCommand(NPCTrader plugin, CommandSender sender, Command c,
			String commandLabel, String[] args, String PermissionNode,
			Boolean bNPCInteract) {
		super(plugin, sender, c, commandLabel, args, PermissionNode,
				bNPCInteract);
	}

	public Boolean Run() {
		if (!super.Run()) {
			return true;
		}
		int ItemSlots = NPCDB.GetTotalSlots(NPCID);
		int Units = NPCDB.GetTotalUnits(NPCID);
		Double Refund = (Double) (((Units * NPCTrader.getSettings().Prices(
				"Unit")) * ItemSlots) + (ItemSlots * NPCTrader.getSettings()
				.Prices("ItemSlot"))
				* ((NPCTrader.getSettings().Prices("RefundPercentage") / 100.0)));
		plugin.npcm.despawnById(Integer.toString(NPCID));
		Boolean DBC = NPCDB.NPCClearNPC(NPCID);
		if (DBC) {
			plugin.AddBalance(player.getName(), Refund);

			sender.sendMessage("NPC Removed, you have been refunded "
					+ iConomy.getBank().format(Refund));
			return true;
		} else {
			sender.sendMessage("Database error removing NPC.");
			return true;
		}

	}

}
