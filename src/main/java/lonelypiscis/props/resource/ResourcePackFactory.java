package lonelypiscis.props.resource;

import java.util.ArrayList;
import java.util.Optional;

import org.spongepowered.api.data.property.item.UseLimitProperty;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import lonelypiscis.props.prop.ModelData;
import lonelypiscis.props.utils.ResourceUtils;

public class ResourcePackFactory {
	
	private ItemType[] overrideItemTypes = {ItemTypes.CHAINMAIL_HELMET};
	private int pendingOverrideTypeIndex;
	private int pendingOverrideIndex;
	
	private ArrayList<ModelData> modelDatas;
	
	public ResourcePackFactory() {
		pendingOverrideIndex = 0;
		modelDatas = new ArrayList<>();
	}
	
	public ModelData requestModelData(String modelPath) {
		ModelData modelDataOut = new ModelData();
		
		modelDataOut.setItemType(getPendingItemType());
		modelDataOut.setModelName(modelPath);
		
		double damagePercentage = (((double) getOverrideCount()) + 1) / ((double) getPendingMaxOverrides());
		
		modelDataOut.setDamagePercentage(damagePercentage);
		
		System.out.println("DamagePercentage: " + damagePercentage);
		
		modelDatas.add(modelDataOut);
		pendingOverrideIndex++;
		
		return modelDataOut;
	}
	
	public void createResourcePack() {
		
	}
	
	public int getMaxOverrides(ItemType item) {
		
		
		Optional<UseLimitProperty> optional = item.getTemplate().getProperty(UseLimitProperty.class);

		UseLimitProperty property = optional.get();
		int maxDurability = property.getValue();
		System.out.println("Max overrides for " + item.getName() + ": " + maxDurability);
		return maxDurability;
	}
	
	public int getPendingMaxOverrides() {
		return getMaxOverrides(getPendingItemType());
	}
	
	public int getMaxOverrides() {
		int maxOverrides = 0;
		
		for (ItemType itemType : overrideItemTypes) {
			getMaxOverrides(itemType);
		}
		
		return maxOverrides;
	}
	
	public int getOverrideCount() {
		return modelDatas.size();
	}
	
	public ItemType getPendingItemType() {
		return overrideItemTypes[pendingOverrideTypeIndex];
	}
	
}
