package lonelypiscis.props.prop;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lonelypiscis.props.storage.IStorable;

public class PropEntityManager implements IStorable {
	
	private HashMap<UUID, PropEntity> managedEntities; 
	
	public PropEntityManager() {
		managedEntities = new HashMap<>();
	}
	
	/* Spawns a PropEntity and registered it as managed entity */
	
	public void spawnPropEntity(Location<World> location) {
		PropEntity prop = PropEntity.create(location);
		
		addManagedEntity(prop);
	}
	
	public void addManagedEntity(PropEntity entity) {
		managedEntities.put(entity.getArmorStandUUID(), entity);
	}
	
	/* Called by {@link lonelypiscis.props.event.PropEntityListener#onEntitySpawned()} when a entity entity joins the world.
	 * Method checks if the entity is registered in the list and if it is an armor stand. */
	
	public void onEntitySpawn(Entity entity) {
		if (managedEntities.containsKey(entity.getUniqueId()) && entity.getType().equals(EntityTypes.ARMOR_STAND)) {
			managedEntities.put(entity.getUniqueId(), new PropEntity(entity));
		}
	}

	@Override
	public void saveToJson(JsonObject jsonObject) {
		JsonArray jsonUUIDArray = new JsonArray();
		
		for (UUID uuid : managedEntities.keySet()) {
			jsonUUIDArray.add(new JsonPrimitive(uuid.toString()));
		}
		
		jsonObject.add("entity_uuids", jsonUUIDArray);
	}

	@Override
	public void loadFromJson(JsonObject jsonObject) {
		JsonArray jsonUUIDArray = jsonObject.get("entity_uuids").getAsJsonArray();
		
		for (JsonElement jsonUUID : jsonUUIDArray) {
			managedEntities.put(UUID.fromString(jsonUUID.getAsString()), null);
		}
	}

}
