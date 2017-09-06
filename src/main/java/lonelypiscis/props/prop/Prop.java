package lonelypiscis.props.prop;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import lonelypiscis.props.Props;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Prop {
	public static ItemStack createItemStack(StructProp prop) {
		ItemStack stack = ItemStack.builder().itemType(prop.model_data.overriddenItemType).build();
		
		stack.offer(Keys.UNBREAKABLE, true);
		stack.offer(Keys.ITEM_DURABILITY, ModelData.getDamageAbsolute(prop.model_data, 0));
		
		stack.offer(Keys.DISPLAY_NAME, Text.of(prop.prop_name));
		
		return stack;
	}
	
	public static ItemStack createItemStack(String id) {
		return createItemStack(Props.getProps().getRegistry().getPropById(id));
	}
	
}
