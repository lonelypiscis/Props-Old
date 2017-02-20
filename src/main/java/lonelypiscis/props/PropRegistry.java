package lonelypiscis.props;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lonelypiscis.props.prop.Prop;
import lonelypiscis.props.storage.IStorable;
import lonelypiscis.props.utils.Debug;

public class PropRegistry implements IStorable {
	
	/* Stores Props in form of 'prop_id -> Prop' */
	private HashMap<String, Prop> propRegister;
	
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
	
	public Prop getPropById(String id) {
		return propRegister.get(id);
	}
	
	private Prop registerProp(Prop prop) {
		if (propRegister.containsKey(prop.getId())) {
			Debug.getLogger().warn("The Prop " + prop.getName() + " [id='" + prop.getId() + "'] has already been registered. Props can not be registered multiple times.");
			Debug.getLogger().debug("Returned prop " + prop.getName() + " unmodified and just as given in arguments.");
			
			return prop;
		}
		
		propRegister.put(prop.getId(), prop);
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as default. A prop cannot be registered multiple times.*/
	
	public Prop registerDefaultProp(Prop prop) {
		registerProp(prop);
		propsDefault.add(prop.getId());
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as default and overwriting any existing registrations in case beeing instructed. */
	
	public Prop registerDefaultProp(Prop prop, boolean overwrite) {
		if (overwrite) {
			propRegister.put(prop.getId(), prop);
		} else {
			return registerDefaultProp(prop);
		}
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, marking it as discovered and overwriting any existing registrations in case beeing instructed.*/
	
	public Prop registerDiscoveredProp(Prop prop, boolean overwrite) {
		if (overwrite) {
			propRegister.put(prop.getId(), prop);
		} else {
			propsDiscovered.add(prop.getId());
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

}
