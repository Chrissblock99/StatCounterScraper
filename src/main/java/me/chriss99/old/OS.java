package me.chriss99.old;

public enum OS {
    LINUX(0, "Linux"),
    FREEBSD(1, "FreeBSD"),
    UNKNOWN(2, "Unknown"),
    OTHER(3, "Other"),
    CHROME_OS(4, "Chrome OS"),
    WINDOWS(5, "Windows"),
    OS_X(6, "OS X");

    public final int index;
    public final String name;

    OS(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static OS getOSFromName(String name) {
        return valueOf(name.replace(" ", "_").toUpperCase());
    }
}
