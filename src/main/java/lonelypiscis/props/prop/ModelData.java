package lonelypiscis.props.prop;

import com.google.gson.JsonObject;

import lonelypiscis.props.storage.IStorable;

public class ModelData implements IStorable {
	private String overwrite_path;
	private String model_path;
	
	public String getOverwritePath() {
		return overwrite_path;
	}
	
	public void setOverwritePath(String path) {
		this.overwrite_path = path;
	}
	
	public String getModelPath() {
		return model_path;
	}
	
	public void setModelPath(String path) {
		this.model_path = path;
	}

	@Override
	public void saveToJson(JsonObject jsonObject) {
		jsonObject.addProperty("overwrite_path", getOverwritePath());
		jsonObject.addProperty("model_path", getModelPath());
	}

	@Override
	public void loadFromJson(JsonObject jsonObject) {
		setOverwritePath(jsonObject.get("overwrite_path").getAsString());
		setModelPath(jsonObject.get("model_path").getAsString());
	}
}
