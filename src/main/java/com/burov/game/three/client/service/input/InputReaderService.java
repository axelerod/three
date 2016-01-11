package com.burov.game.three.client.service.input;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Service
public class InputReaderService {
    private final InputStream inputStream;
    private final PrintStream printStream;

    @Autowired
    public InputReaderService(InputStream inputStream, PrintStream out) {
        this.inputStream = inputStream;
        this.printStream = out;
    }

    public String getString(String message, List<String> responseValues) {
        Scanner scanner = new Scanner(inputStream);
        String targetString = null;

        printStream.println(message);
        if (!responseValues.isEmpty()) {
            printStream.println("Possible values: " + responseValues);
        }
        if (scanner.hasNext()) {
            targetString = scanner.next();
        }

        return targetString;
    }

    public String getString(String meassge) {
        return getString(meassge, Collections.<String>emptyList());
    }
}
