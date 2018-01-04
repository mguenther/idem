package net.mguenther.idem;

/**
 * A {@code TimeProvider} implements a mechanism to get the current timestamp from the
 * machine on which the {@link IdGenerator} is running. {@code TimeProvider}s must be return
 * the time as a number of milliseconds that have elapsed from the Unix epoch.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
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
