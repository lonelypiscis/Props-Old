package lonelypiscis.props.prop;

import lonelypiscis.props.discover.StructModelData;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class StructProp {
	@Setting public StructModelData model_data;
	
	@Setting public String prop_id;
	@Setting public String prop_name;
	
	@Setting public PropType prop_type;
	
	@Setting public float hardness;
}
