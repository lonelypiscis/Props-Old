package lonelypiscis.props.entity;

import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableArmorStandData;
import org.spongepowered.api.data.manipulator.mutable.entity.ArmorStandData;
import org.spongepowered.api.data.manipulator.mutable.entity.GravityData;
import org.spongepowered.api.data.manipulator.mutable.entity.InvisibilityData;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;

import lonelypiscis.props.PropRegistry;
import lonelypiscis.props.Props;
import lonelypiscis.props.PropsPlugin;
import lonelypiscis.props.config.GeneralConfig;
import lonelypiscis.props.prop.Prop;
import lonelypiscis.props.prop.StructProp;
import lonelypiscis.props.storage.HoconUtils;
import lonelypiscis.props.storage.IHoconStorable;
import lonelypiscis.props.utils.Debug;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class PropEntity implements IHoconStorable {
	private ArmorStand armorStandEntity;
	private UUID armorStandUUID;

	private Location<World> location;

	private StructProp prop;

	private float hits = 0;
	private long lastUpdateTime;

	public PropEntity(Entity armorStand, StructProp type) {
		lastUpdateTime = System.currentTimeMillis();

		if (armorStand instanceof ArmorStand) {
			armorStandEntity = (ArmorStand) armorStand;
			armorStandUUID = armorStandEntity.getUniqueId();
			prop = type;

			// System.out.println("Type = " + type.getId());
		} else {
			throw new IllegalArgumentException("Entity has to be an instance of ArmorStand");
		}
	}

	public StructProp getProp() {
		return prop;
	}

	public Location<World> getLocation() {
		return location;
	}

	public Vector3d getPivotPoint() {
		double x = location.getX() + 0.5d;
		double y = location.getY() + 0.5d;
		double z = location.getZ() + 0.5d;

		return new Vector3d(x, y, z);
	}

	public ArmorStand getArmorStandEntity() {
		return armorStandEntity;
	}

	public UUID getArmorStandUUID() {
		return armorStandUUID;
	}
	
	/**
	 * Returns distance to given location.
	 */
	
	public double distanceTo(Location<World> loc) {
		return loc.getPosition().distance(getLocation().getPosition());
	}

	/**
	 * Executes a hit on the PropEntity. If the number of hits exceeds the
	 * hardness, the PropEntity will break and therefor will be removed.
	 * 
	 * Note that only one hit per second is allowed. This is also checked.
	 * 
	 * @return if the hit was executed.
	 */

	public boolean hit(float strength) {
		if (System.currentTimeMillis() - lastUpdateTime > 2000) {
			hits = 0;
			
			lastUpdateTime = System.currentTimeMillis();
			
		}
		
		if (System.currentTimeMillis() - lastUpdateTime > 1000) {
			hits = hits + strength;
			

			if (hits >= getProp().hardness) {
				breakProp();
			}
			
			lastUpdateTime = System.currentTimeMillis();
		}
		

		return true;
	}

	/**
	 * Creates, spawns and adjusts an ArmorStand.
	 * 
	 * @return the created PropEntity
	 */

	public static PropEntity create(Location<World> location, StructProp type) {
		location = new Location<World>(location.getExtent(), location.getBlockPosition());

		Extent extent = location.getExtent();
		ArmorStand ent = (ArmorStand) extent.createEntity(EntityTypes.ARMOR_STAND, location.getPosition());
		PropEntity createdEntity = new PropEntity(ent, type);

		createdEntity.adjustArmorStandData();
		createdEntity.spawnBlockPlaceholder();

		return createdEntity;
	}

	/**
	 * Creates, spawns, rotates and adjusts an ArmorStand.
	 * 
	 * @param location
	 * @param type
	 * @param rotation
	 * @return the created Entity.
	 */

	public static PropEntity create(Location<World> location, StructProp type, Vector3d rotation) {
		location = new Location<World>(location.getExtent(), location.getBlockPosition());

		Extent extent = location.getExtent();
		ArmorStand ent = (ArmorStand) extent.createEntity(EntityTypes.ARMOR_STAND, location.getPosition());
		PropEntity createdEntity = new PropEntity(ent, type);

		createdEntity.adjustArmorStandData();
		createdEntity.setRotation(rotation);

		return createdEntity;
	}

	/**
	 * Creates, spawns, rotates and adjusts an ArmorStand.
	 * 
	 * @param location
	 * @param type
	 * @param rotation
	 * @return the created Entity.
	 */

	public static PropEntity create(World world, UUID uuid, String propId) {
		Optional<Entity> armorStand = world.getEntity(uuid);
		StructProp type = Props.getProps().getRegistry().getPropById(propId);

		System.out.println("Type: " + type);

		if (armorStand.isPresent()) {
			PropEntity createdEntity = new PropEntity(armorStand.get(), type);

			Vector3d locArmorStand = armorStand.get().getLocation().getPosition();
			
			Location<World> locPropEntity = armorStand.get().getLocation().sub(GeneralConfig.VEC_TRANSLATION_ITEM_BASE);
			
			createdEntity.location = locPropEntity;

			Debug.getLogger().info("PropEntity has been successfully created by UUID " + uuid.toString());
			
			createdEntity.spawnBlockPlaceholder();

			return createdEntity;
		}

		Debug.getLogger().warn("PropEntity creation was not successful by UUID " + uuid.toString());

		return null;
	}

	public void spawn() {
		spawnBlockPlaceholder();

		location.getExtent().spawnEntity(getArmorStandEntity(),
				Cause.source(EntitySpawnCause.builder().entity(getArmorStandEntity()).type(SpawnTypes.PLUGIN).build())
						.build());
	}

	public void adjustArmorStandData() {
		ArmorStandData armorStandData = getArmorStandEntity().get(ArmorStandData.class).get();
		getArmorStandEntity().offer(armorStandData.arms().set(true));
		getArmorStandEntity().offer(armorStandData.basePlate().set(false));

		GravityData gravityData = getArmorStandEntity().get(GravityData.class).get();
		getArmorStandEntity().offer(gravityData.gravity().set(false));

		getArmorStandEntity().setItemInHand(HandTypes.MAIN_HAND, Prop.createItemStack(prop));
		getArmorStandEntity().offer(Keys.RIGHT_ARM_ROTATION, new Vector3d(270, 0, 0));
		getArmorStandEntity().offer(Keys.INVISIBLE, true);
		getArmorStandEntity().offer(Keys.AI_ENABLED, false);

		getArmorStandEntity().setRotation(Vector3d.ZERO);
		// getArmorStandEntity().setRotation(new Vector3d(20, 133, 4));

		Location<World> loc = getArmorStandEntity().getLocation().add(GeneralConfig.VEC_TRANSLATION_ITEM_BASE);
		location = getArmorStandEntity().getLocation();
		getArmorStandEntity().setLocation(loc);
	}

	public void spawnBlockPlaceholder() {
		PluginContainer plugin = Sponge.getPluginManager().getPlugin("props").get();

		location.setBlockType(BlockTypes.BARRIER, Cause.source(plugin).build());
	}

	/**
	 * Sets the prop's rotation.
	 * 
	 * Since the Sponge API only allows rotation of an ArmorStandEntity around
	 * its own origin point, this function calculates the correct positions in
	 * order to seemlessly use the prop as the actual pivot.
	 * 
	 * @param rotation
	 */

	public void setRotation(Vector3d rotation) {
		System.out.println("Inital rotation: " + getArmorStandEntity().getRotation());
		System.out.println("Rotating :" + rotation);

		double yaw = Math.toRadians(rotation.getY());

		Location<World> loc = getArmorStandEntity().getLocation();

		Vector3d elementCenter = getArmorStandEntity().getLocation().getPosition().add(0.5, 0, 0.5);
		Vector3d pivot = getPivotPoint();

		Vector3d v = elementCenter.sub(pivot);

		Vector3d vRotated = rotateVec(v, yaw);
		Vector3d vRotatedXZ = new Vector3d(vRotated.getX(), 0, vRotated.getZ());

		Vector3d propPivot = getArmorStandEntity().getLocation().getPosition();
		Vector3d vPropCenter = elementCenter.sub(propPivot).add(0, -1, 0);

		// System.out.println(vRotated);

		loc = loc.setPosition(pivot.add(vRotated).sub(vRotatedXZ.div(2)));
		// loc = loc.setPosition(pivot.add(vRotated));
		// loc = loc.sub(vPropCenter);

		getArmorStandEntity().setLocationAndRotation(loc, rotation);

	}
	
	/**
	 * Triggers the block break. Handles item drops, sound effects, etc.
	 */
	
	public void breakProp() {
		remove();
	}
	
	/** 
	 * Removes the PropEntity, including all references in various managers.
	 */
	
	public void remove() {
		Props.getProps().getPropEntityManager().killManagedEntity(this);
	}
	

	@Override
	public void saveToNode(ConfigurationNode node) {
		node.getNode("armorStandUUID").setValue(armorStandUUID.toString());
		HoconUtils.toNode(getLocation(), node.getNode("location"));
		node.getNode("propType").setValue(prop.prop_id);
	}

	@Override
	public void loadFromNode(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

	private Vector3d rotateVec(Vector3d vec, double radianYaw) {
		double x = vec.getX() * Math.cos(radianYaw) - vec.getY() * Math.sin(radianYaw);
		double y = vec.getX() * Math.sin(radianYaw) + vec.getY() * Math.cos(radianYaw);

		return new Vector3d(x, vec.getY(), y);
	}

}
