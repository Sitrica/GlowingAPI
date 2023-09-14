package com.sitrica.glowing.packets;

/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;

public class WrapperPlayServerEntityMetadata extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.ENTITY_METADATA;

	public WrapperPlayServerEntityMetadata() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerEntityMetadata(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: entity's ID
	 * 
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity.
	 * 
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * @return The current data value collection
	 */
	@Nullable
	public List<WrappedDataValue> getDataValueCollection() {
		return handle.getDataValueCollectionModifier().read(0);
	}

	/**
	 * Add some data values to the data value collection.
	 * 
	 * @param values the {@link WrappedDataValue}s to add.
	 */
	public void addToDataValueCollection(WrappedDataValue... values) {
		List<WrappedDataValue> valuesToAdd = Lists.newArrayList(values);
		List<WrappedDataValue> collection = getDataValueCollection();
		if (collection == null || collection.isEmpty()) {
			collection = valuesToAdd;
		} else {
			collection.addAll(valuesToAdd);
		}
		setDataValueCollection(collection);
	}

	/**
	 * Set data value collection.
	 * 
	 * @param value - new value.
	 */
	public void setDataValueCollection(List<WrappedDataValue> value) {
		handle.getDataValueCollectionModifier().write(0, value);
	}

	/**
	 * Retrieve Metadata.
	 * 
	 * @return The current Metadata
	 */
	public List<WrappedWatchableObject> getMetadata() {
		return handle.getWatchableCollectionModifier().read(0);
	}

	/**
	 * Set Metadata.
	 * 
	 * @param value - new value.
	 */
	public void setMetadata(List<WrappedWatchableObject> value) {
		handle.getWatchableCollectionModifier().write(0, value);
	}

}
