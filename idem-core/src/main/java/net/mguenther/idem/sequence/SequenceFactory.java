package net.mguenther.idem.sequence;

import java.util.HashMap;
import java.util.Map;

public enum SequenceFactory {

    INSTANCE;

    private static final Map<byte[], Sequence> SEQUENCES_PER_WORKER_ID = new HashMap<>();

    public Sequence getSequenceProvider(final byte[] workerId, final SequenceConfig config) {

        synchronized (SEQUENCES_PER_WORKER_ID) {
            if (!SEQUENCES_PER_WORKER_ID.containsKey(workerId)) {
                final Sequence provider = new Sequence(config);
                SEQUENCES_PER_WORKER_ID.put(workerId, provider);
            }
        }

        return SEQUENCES_PER_WORKER_ID.get(workerId);
    }
}
