package lonelypiscis.props.prop;

import java.util.UUID;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableArmorStandData;
import org.spongepowered.api.data.manipulator.mutable.entity.ArmorStandData;
import org.spongepowered.api.data.manipulator.mutable.entity.GravityData;
import org.spongepowered.api.data.manipulator.mutable.entity.InvisibilityData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

public class PropEntity {
	private Entity armorStandEntity;
	private UUID armorStandUUID;
	
	private Location<World> location;
	
	public PropEntity(Entity armorStand) {
		armorStandEntity = armorStand;
		armorStandUUID = armorStandEntity.getUniqueId();
	}
	
	public Location<World> getLocation() {
		return location;
	}
	
	public Entity getArmorStandEntity() {
		return armorStandEntity;
	}
	
	public UUID getArmorStandUUID() {
		return armorStandUUID;
	}
	
	/* Creates, spawns and adjusts an ArmorStand. Returns the respective newly created PropEntity. */
	
	public static PropEntity create(Location<World> location) {
		Extent extent = location.getExtent();
		Entity ent = extent.createEntity(EntityTypes.ARMOR_STAND, location.getPosition());
		
		adjustArmorStandData(ent);
		
		extent.spawnEntity(ent, Cause.source(EntitySpawnCause.builder().entity(ent).type(SpawnTypes.PLUGIN).build()).build());
		
		return new PropEntity(ent);
	}
	
	private static void adjustArmorStandData(Entity ent) {
		ArmorStandData armorStandData = ent.get(ArmorStandData.class).get();
		ent.offer(armorStandData.arms().set(true));
		ent.offer(armorStandData.basePlate().set(false));
		
		GravityData gravityData = ent.get(GravityData.class).get();
		ent.offer(gravityData.gravity().set(false));
		
		// TODO Further development to be continued here... Add equipement
	}
	
}
