package lonelypiscis.props.prop;

import java.util.Optional;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.item.UseLimitProperty;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import com.google.gson.JsonObject;

import lonelypiscis.props.discover.StructModelData;
import lonelypiscis.props.storage.IHoconStorable;
import lonelypiscis.props.storage.IJsonStorable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ModelData {
	public static int getDamageAbsolute(StructModelData modelData, int side) {
		Optional<UseLimitProperty> optional = modelData.overriddenItemType.getTemplate().getProperty(UseLimitProperty.class);

		UseLimitProperty property = optional.get();
		int maxDurability = property.getValue();
			
		return (int) (maxDurability * (1 - modelData.damage));
	}
}
