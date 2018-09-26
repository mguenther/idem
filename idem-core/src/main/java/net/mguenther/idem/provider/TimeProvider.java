package net.mguenther.idem.provider;

import net.mguenther.idem.IdGenerator;

/**
 * A {@code TimeProvider} implements a mechanism to get the current timestamp from the
 * machine on which the {@link IdGenerator} is running. {@code TimeProvider}s must be return
 * the time as a number of milliseconds that have elapsed from the Unix epoch.
 */
public interface TimeProvider {
    /**
     * Generates a timestamp in milliseconds that have elapsed from the Unix epoch.
     *
     * @return
     *      {@code long} representing the time in milliseconds
     */
    long getTimestamp();
}
