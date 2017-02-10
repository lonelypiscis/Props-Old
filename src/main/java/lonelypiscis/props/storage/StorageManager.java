package lonelypiscis.props.storage;

import java.util.ArrayList;

public class StorageManager {
	
	/* Elements of {@link #wakeables} are notified when the world is fully loaded. */
	
	private ArrayList<IWakeable> wakeables;
	
	public StorageManager() {
		wakeables = new ArrayList<>();
	}
	
	public void registerWorldLoadedWakeable(IWakeable wakeable) {
		wakeables.add(wakeable);
	}
}
