package lonelypiscis.props.event;

import java.util.Optional;

import org.ietf.jgss.GSSManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.TargetBlockEvent;
import org.spongepowered.api.event.block.TickBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.AnimateHandEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import lonelypiscis.props.Props;
import lonelypiscis.props.PropsPlugin;
import lonelypiscis.props.config.GeneralConfig;
import lonelypiscis.props.entity.PropEntity;
import lonelypiscis.props.optimizer.BlockInteractionOptimizer;

public class PropEntityListener {

	/**
	 * Notifies the {@link lonelypiscis.props.entity.PropEntityManager
	 * PropEntityManager} that a watchable entity has spawned.
	 * 
	 * @param evt
	 */

	@Listener
	public void onEntitySpawned(SpawnEntityEvent evt) {
		for (Entity ent : evt.getEntities()) {
			Props.getProps().getPropEntityManager().onEntitySpawn(ent);
		}
	}

	/**
	 * Makes a prop instantly mineable in creative mode, just like in vanilla
	 * fashion.
	 * 
	 * @param evt
	 */

	@Listener
	public void onBlockInteraction(InteractBlockEvent.Primary evt) {
		BlockInteractionOptimizer.breakPropCreative(evt);
	}

	/**
	 * Contains a workaround which bypasses problems regarding placing a block
	 * "on top" of the ArmorStand entity.
	 * 
	 * @param evt
	 */

	@Listener
	public void onBlockInteraction(InteractBlockEvent.Secondary evt) {
		BlockInteractionOptimizer.blockPlacementNearProp(evt);
	}

	@Listener
	public void onHandAnimate(AnimateHandEvent evt) {
		BlockInteractionOptimizer.minePropSurvival(evt);
	}

	@Listener
	public void onEntityInteract(InteractEntityEvent evt) {
		BlockInteractionOptimizer.preventPropEntityModification(evt);
	}

	@Listener
	public void onBlockTick(TickBlockEvent evt) {
	}

}
