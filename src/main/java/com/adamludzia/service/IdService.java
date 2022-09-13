package com.adamludzia.service;

import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IdService {

    private long nextId = 1;

    public IdService(FileService fileService) {

        try {
            List<String> lines = fileService.readAllLines();
            if (lines.isEmpty()) {
                fileService.writeToFile("1");
            } else {
                nextId = Integer.parseInt(lines.get(0));
            }
        } catch (IOException e) {
            throw new RuntimeException("Service error: failed to initialize id database", e);
        }

    }

    public long getNextIdAndIncrement(FileService fileService) {
        try {
            fileService.writeToFile(String.valueOf(nextId + 1));
            return nextId++;
        } catch (IOException e) {
            throw new RuntimeException("Service error: failed to read id file", e);
        }
    }
}
