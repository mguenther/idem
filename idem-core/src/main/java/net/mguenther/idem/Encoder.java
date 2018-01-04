package net.mguenther.idem;

public interface Encoder<InputType, OutputType> {
    OutputType encode(InputType inputType);
}
