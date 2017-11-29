package psamolysov.bluemix.wlp.demo.util;

public class DebugUtil {
	
    private static boolean debug = Boolean.getBoolean("meeting.debug");

    public static boolean isDebugEnabled() {
        return debug;
    }
}
