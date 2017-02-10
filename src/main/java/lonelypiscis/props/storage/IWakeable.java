package lonelypiscis.props.storage;

import lonelypiscis.props.Props;

public interface IWakeable {
	public void awakeFromWorld();
	
	public default void registerAsWakeable() {
		Props.getProps().getStorageManager().registerWorldLoadedWakeable(this);
	}
}
