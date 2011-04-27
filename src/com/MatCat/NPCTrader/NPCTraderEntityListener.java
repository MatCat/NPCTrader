package com.MatCat.NPCTrader;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;
import org.martin.bukkit.npclib.NPCEntity;
import org.martin.bukkit.npclib.NpcEntityTargetEvent;
import org.martin.bukkit.npclib.NpcEntityTargetEvent.NpcTargetReason;

import com.MatCat.NPCTrader.Commands.npcListCommand;



public class NPCTraderEntityListener extends EntityListener {

	private static final Logger logger = Logger.getLogger("Minecraft");
	private final NPCTrader parent;

	public NPCTraderEntityListener(NPCTrader parent) {
		this.parent = parent;
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {

		if (event.getEntity() instanceof HumanEntity) {
			NPCEntity npc = parent.npcm.getNPCE(event.getEntity().getEntityId());

			if (npc != null) {

				// Player p = (Player);
				// p.sendMessage("<" + npc.getName() +
				// "> Don't hit me so much :P");

				event.setCancelled(true);

			}

		}
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		// NPCTraderMySQL NPCDB = new NPCTraderMySQL();
		// NPCDB.dbConnect();
		if (event instanceof NpcEntityTargetEvent) {
			NpcEntityTargetEvent nevent = (NpcEntityTargetEvent) event;
			System.out.println(nevent.getNpcReason() + " " + event.getType().toString() + " " + event.getEntity().getEntityId());
			NPCEntity npc = parent.npcm.getNPCE(event
					.getEntity().getEntityId());
			//System.out.println(npc.displayName);
			if (npc != null && event.getTarget() instanceof Player) {
				System.out.println("We are inside of the IF, NPC != null");
				if (nevent.getNpcReason() == NpcTargetReason.CLOSEST_PLAYER) {
					Player p = (Player) event.getTarget();
					p.sendMessage("<" + npc.displayName
							+ "> Can I interest you in anything?");
					event.setCancelled(true);

				} else if (nevent.getType().toString() == "ENTITY_TARGET") {
					Player p = (Player) event.getTarget();
					String[] as = new String[1];
					parent.NPCInteract.put(p.getName(), npc.ID);
					p.sendMessage("<"
							+ npc.displayName
							+ "> "
							+ parent.NPCDB
									.ReturnScalar("Select GreetingMsg from npctext where NPCID = "
											+ npc.ID));
					// NPCDB.dbClose();
					npcListCommand nC = new npcListCommand(parent, p, "", as,
							"npc.user.list", true);
					nC.Run();

					event.setCancelled(true);

				} else if (nevent.getNpcReason() == NpcTargetReason.NPC_BOUNCED) {
					Player p = (Player) event.getTarget();
					p.sendMessage("<" + npc.displayName + "> OH, excuse me sir!");
					event.setCancelled(true);
				}
			}
		}

	}
}
