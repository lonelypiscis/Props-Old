package lonelypiscis.props;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lonelypiscis.props.prop.Prop;
import lonelypiscis.props.prop.StructProp;
import lonelypiscis.props.storage.IJsonStorable;
import lonelypiscis.props.utils.Debug;

public class PropRegistry implements IJsonStorable {
	
	/* Stores Props in form of 'prop_id -> Prop' */
	private HashMap<String, StructProp> propRegister;
	
	/* Stores the ids of Props, that have been registered by the plugin */
	private ArrayList<String> propsDefault;
	
	/* Stores the ids of Props, that have been discovered. Only these are stored. */
	private ArrayList<String> propsDiscovered;
	
	public PropRegistry() {
		propRegister = new HashMap<>();
		
		propsDefault = new ArrayList<>();
		propsDiscovered = new ArrayList<>();
	}
	
	/* Returns a prop instance by its id. */
	
	public StructProp getPropById(String id) {
		return propRegister.get(id);
	}
	
	public void clear() {
		propRegister.clear();
		propsDefault.clear();
		propsDiscovered.clear();
	}
	
	private StructProp registerProp(StructProp prop) {
		if (propRegister.containsKey(prop.prop_id)) {
			Debug.getLogger().warn("The Prop " + prop.prop_name + " [id='" + prop.prop_id + "'] has already been registered. Props can not be registered multiple times.");
			Debug.getLogger().debug("Returned prop " + prop.prop_name + " unmodified and just as given in arguments.");
			
			return prop;
		}
		
		propRegister.put(prop.prop_id, prop);
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as default. A prop cannot be registered multiple times.*/
	
	public StructProp registerDefaultProp(StructProp prop) {
		registerProp(prop);
		propsDefault.add(prop.prop_id);
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as default and overwriting any existing registrations in case beeing instructed. */
	
	public StructProp registerDefaultProp(StructProp prop, boolean overwrite) {
		if (overwrite) {
			propRegister.put(prop.prop_id, prop);
		} else {
			return registerDefaultProp(prop);
		}
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as discovered and overwriting any existing registrations in case beeing instructed.*/
	
	public StructProp registerDiscoveredProp(StructProp prop, boolean overwrite) {
		if (overwrite) {
			propRegister.put(prop.prop_id, prop);
		} else {
			propsDiscovered.add(prop.prop_id);
			return registerProp(prop);
		}
		
		return prop;
	}
	
	/* Saves all discovered props in 'cache_discovered_props' -> JsonArray
	 */

	@Override
	public void saveToJson(JsonObject jsonObject) {
		JsonArray jsonDiscoveredProps = new JsonArray();
		
		for (String id : propsDiscovered) {
			jsonDiscoveredProps.add(new JsonPrimitive(id));
		}
		
		Debug.getLogger().debug("Found " + propsDiscovered.size() + " discovered props to save.");
		
		jsonObject.add("cache_discovered_props", jsonDiscoveredProps);
		
	}

	@Override
	public void loadFromJson(JsonObject jsonObject) {
		
	}
	
	public int count() {
		return propRegister.size();
	}

}
