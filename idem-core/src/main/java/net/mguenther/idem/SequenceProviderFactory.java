package net.mguenther.idem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public enum SequenceProviderFactory {

    INSTANCE;

    private static final Map<byte[], SequenceProvider> SEQUENCES_PER_WORKER_ID = new HashMap<>();

    public SequenceProvider getSequenceProvider(final byte[] workerId, final SequenceProviderConfig config) {

        synchronized (SEQUENCES_PER_WORKER_ID) {
            if (!SEQUENCES_PER_WORKER_ID.containsKey(workerId)) {
                final SequenceProvider provider = new SequenceProvider(config);
                SEQUENCES_PER_WORKER_ID.put(workerId, provider);
            }
        }

        return SEQUENCES_PER_WORKER_ID.get(workerId);
    }
}
