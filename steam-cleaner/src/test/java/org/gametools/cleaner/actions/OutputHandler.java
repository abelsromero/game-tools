package org.gametools.cleaner.actions;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class OutputHandler {

    private final PrintStream originalOut;
    private final OutputStream newOut;

    private OutputHandler() {
        this.originalOut = System.out;
        this.newOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(newOut));
    }

    public static OutputHandler start() {
        return new OutputHandler();
    }

    public void release() {
        System.setOut(originalOut);
    }

    public String getOutput() {
        return newOut.toString();
    }
}
