package lonelypiscis.props;

import java.util.HashMap;

import com.google.gson.JsonObject;

import lonelypiscis.props.prop.Prop;
import lonelypiscis.props.storage.IStorable;

public class PropRegistry implements IStorable {
	
	/* Stores Props in form of 'prop_id -> Prop' */
	private HashMap<String, Prop> propRegister;
	
	public PropRegistry() {
		propRegister = new HashMap<>();
	}
	
	/* Registers a Prop in the storage list. A prop cannot be registered multiple times.*/
	
	public Prop registerProp(Prop prop) {
		if (propRegister.containsKey(prop.getId())) {
			throw new IllegalArgumentException("Illgal multiple registration of " + prop.getId() + ". A Prop can only be registered once.");
		}
		
		propRegister.put(prop.getId(), prop);
		
		return prop;
	}
	
	/* Registers a Prop in the storage list, overwriting any existing registrations. */
	
	public Prop registerProp(Prop prop, boolean overwrite) {
		if (overwrite) {
			propRegister.put(prop.getId(), prop);
		} else {
			return registerProp(prop);
		}
		
		return prop;
	}
	

	@Override
	public void saveToJson(JsonObject jsonObject) {

	}

	@Override
	public void loadFromJson(JsonObject jsonObject) {
		
	}

}
