package net.mguenther.idem.sequence;

import net.mguenther.idem.provider.TimeProvider;
import net.mguenther.idem.provider.LinearTimeProvider;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SequenceConfig {

    public static final int MINIMUM_POOL_SIZE = (int) Math.pow(2, 1);

    public static final int MAXIMUM_POOL_SIZE = (int) Math.pow(2, 16);

    public static final int DEFAULT_MAX_WAIT_TIME_IN_MILLIS = 10;

    public static final TimeProvider DEFAULT_TIME_PROVIDER = new LinearTimeProvider();

    public static class SequenceProviderConfigBuilder {

        private final int maxPoolNumbersPerTick;

        private int maxWaitTimeInMillis = DEFAULT_MAX_WAIT_TIME_IN_MILLIS;

        private TimeProvider timeProvider = DEFAULT_TIME_PROVIDER;

        public SequenceProviderConfigBuilder(final int maxPoolNumbersPerTick) {
            this.maxPoolNumbersPerTick = max(MINIMUM_POOL_SIZE, min(maxPoolNumbersPerTick, MAXIMUM_POOL_SIZE));
        }

        public SequenceProviderConfigBuilder withMaxWaitTime(final int amountOfTime, final TimeUnit timeUnit) {
            maxWaitTimeInMillis = (int) timeUnit.toMillis(amountOfTime);
            return this;
        }

        public SequenceProviderConfigBuilder withTimeProvider(final TimeProvider timeProvider) {
            this.timeProvider = timeProvider;
            return this;
        }

        public SequenceConfig build() {
            return new SequenceConfig(this);
        }
    }

    private final int maxPoolNumbersPerTick;

    private final int maxWaitTimeInMillis;

    private final TimeProvider timeProvider;

    private SequenceConfig(final SequenceProviderConfigBuilder builder) {
        this.maxPoolNumbersPerTick = builder.maxPoolNumbersPerTick;
        this.maxWaitTimeInMillis = builder.maxWaitTimeInMillis;
        this.timeProvider = builder.timeProvider;
    }

    public int getMaxPoolNumbersPerTick() {
        return maxPoolNumbersPerTick;
    }

    public int getMaxWaitTimeInMillis() {
        return maxWaitTimeInMillis;
    }

    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public static SequenceProviderConfigBuilder sequenceProviderConfig(final int maxPoolNumbersPerTick) {
        return new SequenceProviderConfigBuilder(maxPoolNumbersPerTick);
    }

    public static SequenceConfig useDefaults(final int maxPoolNumbersPerTick) {
        return new SequenceProviderConfigBuilder(maxPoolNumbersPerTick).build();
    }
}
