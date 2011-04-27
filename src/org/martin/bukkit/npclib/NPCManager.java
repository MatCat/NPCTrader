package org.martin.bukkit.npclib;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import net.minecraft.server.Entity;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author martin
 */
public class NPCManager {

	private HashMap<String, NPCEntity> npcs = new HashMap<String, NPCEntity>();
	private BServer server;

	public NPCManager(JavaPlugin plugin) {
		server = BServer.getInstance(plugin);
	}

	public NPCEntity spawnNPC(String name, Location l) {
		int i = 0;
		String id = name;
		while (npcs.containsKey(id)) {
			id = name + i;
			i++;
		}
		return spawnNPC(name, l, id);
	}
	public HashMap<String, NPCEntity> NPCMap() {
		return npcs;
	}
	public NPCEntity spawnNPC(String name, Location l, String id) {
		if (npcs.containsKey(id)) {
			server.getLogger().log(Level.WARNING,
					"NPC with that id already exists, existing NPC returned");
			return npcs.get(id);
		} else {
			if (name.length() > 16) { // Check and nag if name is too long,
										// spawn NPC anyway with shortened name.
				String tmp = name.substring(0, 16);
				server.getLogger().log(Level.WARNING,
						"NPCs can't have names longer than 16 characters,");
				server.getLogger().log(Level.WARNING,
						name + " has been shortened to " + tmp);
				name = tmp;
			}
			BWorld world = new BWorld(l.getWorld());
			NPCEntity npcEntity = new NPCEntity(server.getMCServer(),
					world.getWorldServer(), name, new ItemInWorldManager(
							world.getWorldServer()));
			npcEntity.setPositionRotation(l.getBlockX(), l.getBlockY(),
					l.getBlockZ(), l.getYaw(), l.getPitch());
			world.getWorldServer().addEntity(npcEntity); // the right way
			npcEntity.ID = id;
			npcs.put(id, npcEntity);
			return npcEntity;
		}
	}

	public void despawnById(String id) {
		NPCEntity npc = npcs.get(id);
		if (npc != null) {
			npcs.remove(id);
			try {
				npc.world.removeEntity(npc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void despawn(String npcName) {
		if (npcName.length() > 16) {
			npcName = npcName.substring(0, 16); // Ensure you can still despawn
		}
		HashSet<String> toRemove = new HashSet<String>();
		for (String n : npcs.keySet()) {
			NPCEntity npc = npcs.get(n);
			System.out.println(npc.name);
			if (npc != null && npc.name.equals(npcName)) {
				toRemove.add(n);
				try {
					npc.world.removeEntity(npc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for (String n : toRemove) {
			npcs.remove(n);
		}
	}

	public void moveNPC(String npcName, Location l) {
		NPCEntity npc = npcs.get(npcName);
		if (npc != null) {
			npc.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(),
					l.getPitch());
		}
	}

	public void moveNPCStatic(String npcName, Location l) {
		NPCEntity npc = npcs.get(npcName);
		if (npc != null) {
			npc.move(l.getX(), l.getY(), l.getZ());
		}
	}

	public NPCEntity getNPC(String id) {
		return npcs.get(id);
	}
	public NPCEntity getNPCE(int id) {
		for (String NPCID : this.npcs.keySet()) {
			NPCEntity npc = this.getNPC(NPCID);
			if (npc.id == id) {
				return npc;
			}
		}
		return null;

	}
	public List<NPCEntity> getNPCsByName(String name) {
		List<NPCEntity> ret = new ArrayList<NPCEntity>();
		Collection<NPCEntity> i = npcs.values();
		for (NPCEntity e : i) {
			ret.add(e);
		}
		return ret;
	}

	public boolean GetReDraw(String id) {
		NPCEntity npc = getNPC(id);
		if (npc != null) {
			return npc.ReDraw;
		} else {
			return false;
		}
	}
	public void SetReDraw(String id,boolean Bool) {
		NPCEntity npc = getNPC(id);
		if (npc != null) {
			npc.ReDraw = Bool;
		}
		
	}
	public NPCEntity ReDraw(String id) {
		NPCEntity npc = getNPC(id);
		if (npc != null) {
			String Name = npc.name;
			Location l = new Location((World) npc.world, npc.locX, npc.locY, npc.locZ, npc.pitch,npc.yaw);
			this.despawnById(id);
			return this.spawnNPC(Name, l, id);
		} else {
			return null;
		}
	}
	public void rename(String id, String name) {
		if (name.length() > 16) { // Check and nag if name is too long, spawn
									// NPC anyway with shortened name.
			String tmp = name.substring(0, 16);
			server.getLogger().log(Level.WARNING,
					"NPCs can't have names longer than 16 characters,");
			server.getLogger().log(Level.WARNING,
					name + " has been shortened to " + tmp);
			name = tmp;
		}
		NPCEntity npc = getNPC(id);
		npc.setName(name);
		BWorld b = new BWorld(npc.getBukkitEntity().getLocation().getWorld());
		WorldServer s = b.getWorldServer();
		try {
			Method m = s.getClass().getDeclaredMethod("d",
					new Class[] { Entity.class });
			m.setAccessible(true);
			m.invoke(s, (Entity) npc);
			m = s.getClass().getDeclaredMethod("c",
					new Class[] { Entity.class });
			m.setAccessible(true);
			m.invoke(s, (Entity) npc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		s.everyoneSleeping();
	}
}
