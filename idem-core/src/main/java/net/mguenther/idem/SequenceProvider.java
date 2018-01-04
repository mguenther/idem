package net.mguenther.idem;

/**
 * A {@code SequenceProvider} implements a mechanism that generates unique sequence IDs for a
 * dedicate time slot, where the time slot is given in milliseconds since the Unix epoch. A
 * {@code SequenceProvider} guarantees that only hands out sequence number that are unique
 * within its own context.
 *
 * This means, as idem is coordination-free, be sure to share the {@code SequenceProvider} between
 * multiple {@link IdGenerator}s, otherwise you could potentially introduce duplicate IDs.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public interface SequenceProvider {
    /**
     * Generates the next unique sequence number.
     *
     * @param currentTime
     *      the current time as timestamp in milliseconds that have elapsed since the Unix epoch
     * @return
     *      {@code byte[]} representing the next sequence number
     */
    byte[] nextSequenceNumber(long currentTime);
}
