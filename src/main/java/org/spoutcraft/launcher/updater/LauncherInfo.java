
package org.spoutcraft.launcher.updater;

import net.titanscraft.launchercore.exception.RestfulAPIException;
import net.titanscraft.launchercore.restful.PlatformConstants;
import net.titanscraft.launchercore.restful.RestObject;

public class LauncherInfo {

    public static int getLatestBuild(String stream) throws RestfulAPIException {
        LauncherBuild result = RestObject.getRestObject(LauncherBuild.class, PlatformConstants.API + "launcher/version/" + stream);
        return result.getLatestBuild();
    }

    public static String getDownloadURL(int version, boolean isJar) throws RestfulAPIException {
        String ext = isJar ? "jar" : "exe";

        String url = PlatformConstants.API + "launcher/url/" + version + "/" + ext;
        LauncherURL result = RestObject.getRestObject(LauncherURL.class, url);
        return result.getURL();
    }
}
