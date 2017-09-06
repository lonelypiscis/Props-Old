package lonelypiscis.props.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.typesafe.config.Config;

import lonelypiscis.props.Props;
import lonelypiscis.props.prop.PropType;
import lonelypiscis.props.storage.IHoconStorable;
import lonelypiscis.props.utils.Debug;
import ninja.leaping.configurate.ConfigurationNode;

/**
 * The PropEntityManager acts as a data watcher for all PropEntities in a world.
 * 
 * When an entity joins the world, {@link #onEntitySpawn(Entity) onEntitySpawn}
 * is called by the {@link lonelypiscis.props.event.PropEntityListener
 * PropEntity event listener}.
 *
 */

public class PropEntityManager implements IHoconStorable {
	private HashMap<UUID, PropEntity> managedEntities;

	private HashMap<String, UUID> entityUUIDForLocationString; // Loc = x y z
																// worldName

	public PropEntityManager() {
		managedEntities = new HashMap<>();
		entityUUIDForLocationString = new HashMap<>();
	}
	
	public int countManagedEntities() {
		return managedEntities.size();
	}

	/**
	 * Spawns a PropEntity and registers it as a managed entity.
	 * 
	 * @param location
	 * @param id
	 */

	public void spawnPropEntity(Location<World> location, String id) {
		PropEntity prop = PropEntity.create(location, Props.getProps().getRegistry().getPropById(id));
		prop.spawn();
		addManagedEntity(prop);
	}

	/**
	 * Spawns a rotated PropEntity and registers it as a managed entity. Can be
	 * useful when used in combination with a player head's rotation.
	 * 
	 * @param location
	 * @param id
	 * @param rotation
	 */

	public void spawnPropEntity(Location<World> location, String id, Vector3d rotation) {
		PropEntity prop = PropEntity.create(location, Props.getProps().getRegistry().getPropById(id), rotation);
		prop.spawn();
		//prop.setRotation(rotation);
		
		
		addManagedEntity(prop);
	}

	/**
	 * Starts managing the specified entity. Management can be ended by calling
	 * {@link #removeManagedEntity}.
	 * 
	 * @param entity
	 */

	public void addManagedEntity(PropEntity entity) {
		managedEntities.put(entity.getArmorStandUUID(), entity);

		//System.out.println("uuid " + entity.getArmorStandUUID());

		if (entity.getProp().prop_type.equals(PropType.BLOCK)) {
			String locationString = locationToString(entity.getLocation());
			//System.out.println(locationString);
			entityUUIDForLocationString.put(locationString, entity.getArmorStandUUID());
			System.out.println("Added location " + locationString);
		}
	}

	/**
	 * Stops management of the specified entity and fully conducts its removal.
	 */

	public void killManagedEntity(Location<World> location) {
		Optional<Entity> entity = getEntityByLocation(location);

		if (entity.isPresent()) {
			entity.get().remove();
		}

		PluginContainer plugin = Sponge.getPluginManager().getPlugin("props").get();

		location.setBlockType(BlockTypes.AIR, Cause.source(plugin).build());

		removeManagedEntity(location);
	}

	/**
	 * Stops management of the specified entity and fully conducts its removal.
	 */

	public void killManagedEntity(PropEntity propEntity) {
		ArmorStand entity = propEntity.getArmorStandEntity();
		entity.remove();
		
		PluginContainer plugin = Sponge.getPluginManager().getPlugin("props").get();
		propEntity.getLocation().setBlockType(BlockTypes.AIR, Cause.source(plugin).build());

		removeManagedEntity(propEntity);
	}
	
	/**
	 * Stops management of the specified entity.
	 */

	public void removeManagedEntity(Location<World> location) {
		PropEntity entity = getPropEntityByLocation(location);
		
		if (entity == null) return;
		
		removeManagedEntity(entity);
	}

	/**
	 * Stops watching the specified entity.
	 */

	public void removeManagedEntity(PropEntity entity) {
		UUID uuid = entity.getArmorStandUUID();
		String locString = locationToString(entity.getLocation());

		if (managedEntities.containsKey(uuid))
			managedEntities.remove(uuid);
		if (entityUUIDForLocationString.containsKey(locString))
			entityUUIDForLocationString.remove(locString);
	}

	/**
	 * Called by
	 * {@link lonelypiscis.props.event.PropEntityListener#onEntitySpawned()}
	 * when a entity joins the world. Method checks if the entity is registered
	 * in the list and if it is an armor stand.
	 */

	public void onEntitySpawn(Entity entity) {
		if (managedEntities.containsKey(entity.getUniqueId()) && entity.getType().equals(EntityTypes.ARMOR_STAND)) {
			// managedEntities.put(entity.getUniqueId(), new PropEntity(entity,
			// ""));

			// TODO Rewrite entity manager system. Currently, only UUIDs are
			// stored, prop information is not.
		}
	}
	
	public boolean isPropNearby(Location<World> location, double distance) {
		for (PropEntity propEntity : managedEntities.values()) {
			
			return propEntity.distanceTo(location) <= distance;
		}
		
		
		return false;
		
	}
	
	/**
	 * Checks if a PropEntity is registered for a certain location.
	 */
	
	public boolean isPropEntityExisting(Location<World> location) {
		return entityUUIDForLocationString.get(locationToString(location)) != null;
	}
	
	/**
	 * Checks if a Entity is registered as PropEntity.
	 */
	
	public boolean isPropEntity(Entity entity) {
		for (UUID uuid : managedEntities.keySet()) {
			if (entity.getUniqueId().equals(uuid)) return true;
		}
		
		return false;
	}

	/**
	 * Returns the managed PropEntity by location.
	 */

	public PropEntity getPropEntityByLocation(Location<World> location) {
		UUID uuid = entityUUIDForLocationString.get(locationToString(location));
		PropEntity propEntity = managedEntities.get(uuid);
		
		return propEntity;
	}

	/**
	 * Returns the managed Entity by location.
	 */

	public Optional<Entity> getEntityByLocation(Location<World> location) {
		UUID uuid = entityUUIDForLocationString.get(locationToString(location));

		return location.getExtent().getEntity(uuid);
	}
	
	
	@Override
	public void saveToNode(ConfigurationNode node) {
		for (Entry<UUID, PropEntity> entry : managedEntities.entrySet()) {
			String uuid = entry.getKey().toString();
			Location<World> loc = entry.getValue().getArmorStandEntity().getLocation();
			Vector3i vec = entry.getValue().getLocation().getBlockPosition();

			ConfigurationNode entityNode = node.getNode("entities", uuid);
			entityNode.getNode("x").setValue(vec.getX());
			entityNode.getNode("y").setValue(vec.getY());
			entityNode.getNode("z").setValue(vec.getZ());
			entityNode.getNode("propTypeId").setValue(entry.getValue().getProp().prop_id);

			entityNode.getNode("worldName").setValue(loc.getExtent().getName());

		}

	}

	@Override
	public void loadFromNode(ConfigurationNode node) {
		ConfigurationNode listNode = node.getNode("entities");
		
		for (Object key : listNode.getChildrenMap().keySet()) {	
			ConfigurationNode entityNode = listNode.getNode(key);
			
			UUID uuid = UUID.fromString(entityNode.getKey().toString());
			int x = entityNode.getNode("x").getInt();
			int y = entityNode.getNode("y").getInt();
			int z = entityNode.getNode("z").getInt();
			String propTypeId = entityNode.getNode("propTypeId").getString();
			String worldName = entityNode.getNode("worldName").getString();
			Optional<World> world = Sponge.getServer().getWorld(worldName);
			
			if (world.isPresent()) {
				Vector3i vec = new Vector3i(x, y, z);
				PropEntity propEntity = PropEntity.create(world.get(), uuid, propTypeId);
				
				// Checks if PropEntity is present. If not, removes it from the ConfigurationNode 
				if (propEntity != null) {		
					entityUUIDForLocationString.put(vectorToString(vec, worldName), uuid);
					managedEntities.put(uuid, propEntity);
					
					Debug.getLogger().info("Adding Prop id at " + vec.toString());
				} else {
					listNode.removeChild(entityNode.getKey());
					Debug.getLogger().warn("Removing entity from ConfigurationNode");
				}
				
			} else {
				Debug.getLogger().warn("World " + worldName + " does not exist!");
			}
		}
		
		//System.out.println("Loaded to " + managedEntities.size());

	}

	private String locationToString(Location<World> location) {
		Vector3i vec = location.getBlockPosition();
		return vectorToString(vec) + " " + location.getExtent().getName();
	}

	private String vectorToString(Vector3i vec) {
		return vec.getX() + " " + vec.getY() + " " + vec.getZ();
	}

	private String vectorToString(Vector3i vec, String worldName) {
		return vec.getX() + " " + vec.getY() + " " + vec.getZ() + " " + worldName;
	}

}
