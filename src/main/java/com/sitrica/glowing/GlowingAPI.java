package com.sitrica.glowing;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.sitrica.glowing.packets.WrapperPlayServerEntityMetadata;

/**
 * @author LimeGlass (https://github.com/TheLimeGlass)
 */
public class GlowingAPI {

	private final Map<Player, Entity> glowing = new HashMap<>(); // Receiver, Entity.
	private final JavaPlugin plugin;

	/**
	 * Create a new instance of GlowingAPI for the defined plugin.
	 * 
	 * @param plugin JavaPlugin that tasks will be allocated on.
	 */
	public GlowingAPI(JavaPlugin plugin) {
		this.plugin = plugin;
		if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"))
			throw new IllegalPluginAccessException("ProtocolLib needs to be loaded before GlowingAPI.");
	}

	/**
	 * Checks if the entity is glowing for the receiver.
	 * 
	 * @param entity The entity to check if glowing for receiver.
	 * @param receiver The player to see if they have the entity glowing.
	 * @return boolean if the check was successful.
	 */
	public boolean isGlowingFor(Entity entity, Player receiver) {
		Optional<Entity> optional = Optional.ofNullable(glowing.get(receiver));
		if (!optional.isPresent())
			return false;
		return optional.get().equals(entity);
	}

	/**
	 * @return Map of which entities and glowing to which players.
	 */
	public Map<Player, Entity> getGlowingMap() {
		return Collections.unmodifiableMap(glowing);
	}

	/**
	 * Grab all the recipients of a glowing effect on the targeted entity.
	 * 
	 * @param entity The entity to search for recipients on.
	 * @return Set<Player> of all players that see a glowing effect on target entity.
	 */
	public Set<Player> getGlowingFor(Entity entity) {
		return glowing.entrySet().stream()
				.filter(entry -> entry.getValue().getUniqueId().equals(entity.getUniqueId()))
				.map(entry -> entry.getKey())
				.collect(Collectors.toSet());
	}

	/**
	 * Grab all the entities glowing for a player.
	 * 
	 * @param player The Player to grab all glowing entities of.
	 * @return Set<Entity> of all entities glowing for the player.
	 */
	public Set<Entity> getGlowingEntities(Player player) {
		return glowing.entrySet().stream()
				.filter(entry -> entry.getKey().getUniqueId().equals(player.getUniqueId()))
				.map(entry -> entry.getValue())
				.collect(Collectors.toSet());
	}

	/**
	 * Set an entity to be glowing for a player.
	 * 
	 * @param entities The Collection<Entity> to have the glowing effect on.
	 * @param receivers The player(s) that is seeing the entity glowing.
	 */
	public <T extends Entity> void setGlowing(Collection<T> entities, Player... receivers) {
		entities.forEach(entity -> setGlowing(entity, receivers));
	}

	/**
	 * Set an entity to be glowing for a player.
	 * 
	 * @param entity The entity to have the glowing effect on.
	 * @param receivers The player(s) that will be seeing the entity glowing.
	 */
	public void setGlowing(Entity entity, Player... receivers) {
		for (Player receiver : receivers) {
			WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
			WrappedDataWatcher watcher = new WrappedDataWatcher();
			watcher.setObject(0, Registry.get(Byte.class), (byte) 0x40);

			packet.setMetadata(watcher.getWatchableObjects());
			packet.setEntityID(entity.getEntityId());
			packet.sendPacket(receiver);

			glowing.put(receiver, entity);
		}
	}

	/**
	 * Timed glowing effect. The effect will start and stop at the defined time.
	 * 
	 * @param delay Delay number.
	 * @param unit TimeUnit of the delay unit.
	 * @param entity The entity to have the glowing effect.
	 * @param receivers The receivers of the glowing effect on the entities.
	 */
	public void setTimedGlowing(long delay, TimeUnit unit, Entity entity, Player... receivers) {
		setTimedGlowing(delay, unit, entity, receivers);
	}

	/**
	 * Timed glowing effect. The effect will start and stop at the defined time.
	 * 
	 * @param delay Delay number.
	 * @param unit TimeUnit of the delay unit.
	 * @param entities Collection<Entity> to have the glowing effect.
	 * @param receivers The receivers of the glowing effect on the entities.
	 */
	public <T extends Entity> void setTimedGlowing(long delay, TimeUnit unit, Collection<T> entities, Player... receivers) {
		setGlowing(entities, receivers);
		Bukkit.getScheduler().runTaskLater(plugin, () -> stopGlowing(entities, receivers), unit.toSeconds(delay) * 20);
	}

	/**
	 * Stop an entity from glowing to player(s).
	 * 
	 * @param entities Collection<Entity> to have the glowing effect removed from.
	 * @param receivers The player(s) that was seeing the entities glowing.
	 */
	public <T extends Entity> void stopGlowing(Collection<T> entities, Player... receivers) {
		entities.forEach(entity -> stopGlowing(entity, receivers));
	}

	/**
	 * Stop an entity from glowing to player(s).
	 * 
	 * @param entity The entity to have the glowing effect removed from.
	 * @param receivers The player(s) that were seeing the entity glowing.
	 */
	public void stopGlowing(Entity entity, Player... receivers) {
		for (Player receiver : receivers) {
			if (!glowing.containsKey(receiver))
				return;
			WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
			WrappedDataWatcher watcher = new WrappedDataWatcher();
			watcher.setObject(0, Registry.get(Byte.class), (byte) 0);

			packet.setMetadata(watcher.getWatchableObjects());
			packet.setEntityID(entity.getEntityId());
			packet.sendPacket(receiver);

			glowing.remove(receiver);
		}
	}

}
