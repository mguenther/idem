package net.mguenther.idem.provider;

import net.mguenther.idem.WorkerIdProvider;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class NetworkInterfaceWorkerIdProvider implements WorkerIdProvider {

    private byte[] workerId;

    public NetworkInterfaceWorkerIdProvider() {
        try {
            workerId = workerId();
        } catch (NoSuchAlgorithmException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getWorkerId() {
        return workerId;
    }

    private byte[] workerId() throws NoSuchAlgorithmException, SocketException {
        return workerId(MessageDigest.getInstance("SHA-1"));
    }

    private byte[] workerId(final MessageDigest messageDigest) throws SocketException {

        Collections.list(NetworkInterface.getNetworkInterfaces())
                .stream()
                .map(this::toHardwareAddress)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(messageDigest::update);

        return Arrays.copyOf(messageDigest.digest(), 6);
    }

    private Optional<byte[]> toHardwareAddress(final NetworkInterface networkInterface) {
        Optional<byte[]> hardwareAddress;
        try {
            hardwareAddress = Optional.ofNullable(networkInterface.getHardwareAddress());
        } catch (SocketException e) {
            hardwareAddress = Optional.empty();
        }
        return hardwareAddress;
    }
}
