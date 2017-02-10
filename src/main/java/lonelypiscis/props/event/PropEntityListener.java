package lonelypiscis.props.event;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;

import lonelypiscis.props.Props;

public class PropEntityListener {
	
	@Listener
	public void onEntitySpawned(SpawnEntityEvent evt) {
		for (Entity ent : evt.getEntities()) {
			Props.getProps().getPropEntityManager().onEntitySpawn(ent);
		}
	}
}
