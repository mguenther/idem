package net.mguenther.idem.provider;

import net.mguenther.idem.IdGenerator;

/**
 * A {@code WorkerIdProvider} implements a mechanism to uniquely identify the worker process on which
 * the {@link IdGenerator} is running. Please note that in order to avoid duplicates you should choose
 * an implementation of {@code WorkerIdProvider} that fits your system setup. For instance, if you run
 * more than a single instance of idem on a machine and use a {@code WorkerIdProvider} that is agnostic
 * of that instance, you could introduce duplicate IDs into your system as idem does not coordinate ID
 * generation between multiple instances.
 */
public interface WorkerIdProvider {
    /**
     * Generates the worker ID that uniquely identifies the worker process on which the
     * {@link IdGenerator} is running.
     *
     * @return
     *      {@code byte[]} representing the generated worker ID
     */
    byte[] getWorkerId();
}
