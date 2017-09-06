package lonelypiscis.props.discover;

import org.spongepowered.api.item.ItemType;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class StructModelData {
	@Setting public String override_target;
	@Setting public String model_name;
	
	@Setting public ItemType overriddenItemType;
	@Setting public double damage;
}
