package net.mguenther.idem.encoder;

/**
 * Encodes a given input of parameterized type {@code InputType} to the parameterized
 * output type {@code OutputType}.
 *
 * @param <InputType>
 * @param <OutputType>
 */
public interface Encoder<InputType, OutputType> {
    /**
     * Encodes an input of type {@code InputType} to an output of type {@code OutputType}.
     *
     * @param inputType
     *      the non-encoded input of type {@code InputType} that ought to be encoded
     * @throws EncoderException
     *      in case enconding the given input failed
     * @return
     *      the encoded input of type {@code OutputType}
     */
    OutputType encode(InputType inputType);
}
