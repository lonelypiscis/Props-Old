package lonelypiscis.props.utils;

import java.io.File;
import java.net.URL;

import com.google.common.base.Utf8;

import lonelypiscis.props.discover.PropDiscoverer;

public class ResourceUtils {
	public static String assetsDirectory = "/assets/props/";
	
	/*
	 *  This is a workaround as long as I can't get the AssetManager working properly...
	 */
	
	public static URL getAssetURL(String assetPath) {
		return PropDiscoverer.class.getResource(assetsDirectory + assetPath);
	}
	
	public static String getFullAssetPath(String assetPath) {
		return PropDiscoverer.class.getResource(assetsDirectory + assetPath).getFile().replaceAll("%20", " ");
	}
	
	public static File getAssetFile(String assetPath) {
		return new File(getFullAssetPath(assetPath));
	}
}
