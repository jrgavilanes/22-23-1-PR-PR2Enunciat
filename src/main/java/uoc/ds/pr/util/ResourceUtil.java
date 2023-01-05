package uoc.ds.pr.util;

import static uoc.ds.pr.SportEvents4Club.*;

public class ResourceUtil {

    public static byte getFlag(byte... in) {
        byte ret = 0;
        for (byte b: in) {
            ret = (byte)(ret | b);

        }
        return ret;
    }

    public static boolean hasPrivateSecurity(byte flag) {
        return hasFlag(flag, FLAG_PRIVATE_SECURITY);
    }

    public static boolean hasPublicSecurity(byte flag) {
        return hasFlag(flag, FLAG_PUBLIC_SECURITY);
    }

    public static boolean hasBasicLifeSupport(byte flag) {
        return hasFlag(flag, FLAG_BASIC_LIFE_SUPPORT);
    }

    public static boolean hasVolunteers(byte flag) {
        return hasFlag(flag, FLAG_VOLUNTEERS);
    }

    public static boolean hasAllOpts(byte flag){
        return FLAG_ALL_OPTS == flag;

    }
    private static boolean hasFlag(byte flagSrc, byte flagCmp) {
        byte res = (byte)(flagSrc & flagCmp);
        return (res>0);
    }



}
