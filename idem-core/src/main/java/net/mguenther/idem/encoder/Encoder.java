package net.mguenther.idem.encoder;

public interface Encoder<InputType, OutputType> {
    OutputType encode(InputType inputType);
}
