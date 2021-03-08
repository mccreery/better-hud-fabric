package mccreery.betterhud.internal;

public final class Bitwise {
    /**
     * Performs a bitwise rotation such that the bits being shifted out on the right are shifted in on the left.
     */
    public static short rotateRight(short x, int bits) {
        // Prevent signed promotion to int
        int y = x & 0xffff;
        // Take remainder after any full rotations
        bits = bits & 0xf;
        // All bitwise ops are promoted to int
        // Unsigned shifts only
        return (short)(y >>> bits | y << (16 - bits));
    }

    private Bitwise() {
    }
}
