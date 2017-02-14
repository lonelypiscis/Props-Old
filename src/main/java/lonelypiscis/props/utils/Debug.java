package lonelypiscis.props.utils;

import org.slf4j.Logger;

import lonelypiscis.props.PropsPlugin;

/* Utility class. Long error statements are not needed to be defined in class and can be substituted
 * with methods from the Debug class.  */

public class Debug {
	public static Logger getLogger() {
		return PropsPlugin.getLogger();
	}
}
