package lonelypiscis.props;

import lonelypiscis.props.prop.PropEntityManager;
import lonelypiscis.props.storage.StorageManager;

public class Props {
	private static Props INSTANCE;
	
	private PropRegistry propRegistry;
	private StorageManager storageManager;
	private PropEntityManager propEntityManager;
	
	public Props() {
		propRegistry = new PropRegistry();
		storageManager = new StorageManager();
		propEntityManager = new PropEntityManager();
	}
	
	public void loadData() {
		
	}
	
	public static Props getProps() {
		return INSTANCE;
	}
	
	public PropRegistry getRegistry() {
		return propRegistry;
	}
	
	public StorageManager getStorageManager() {
		return storageManager;
	}
	
	public PropEntityManager getPropEntityManager() {
		return propEntityManager;
	}
	
	public static void instatiate() {
		INSTANCE = new Props();
	}
}
