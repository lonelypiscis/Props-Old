package lonelypiscis.props.prop;

import com.google.gson.JsonObject;

import lonelypiscis.props.storage.IStorable;

public class Prop implements IStorable {
	
	private ModelData modelData;
	
	private String propId;
	private String propName;
	
	public Prop(String id) {
		propId = id;
		modelData = new ModelData();
	}

	public String getId() {
		return propId;
	}
	
	public void setName(String name) {
		propName = name;
	}
	
	public String getName() {
		return propName;
	}
	
	public ModelData getModelData() {
		return modelData;
	}

	@Override
	public void saveToJson(JsonObject jsonObject) {
		jsonObject.add("model_data", jsonObject);
		jsonObject.addProperty("prop_id", propId);
		jsonObject.addProperty("prop_name", propName);
	}

	@Override
	public void loadFromJson(JsonObject jsonObject) {
		modelData.loadFromJson(jsonObject.get("model_data"));
		propId = jsonObject.get("prop_id").getAsString();
		propName = jsonObject.get("prop_name").getAsString();
	}
	
}
