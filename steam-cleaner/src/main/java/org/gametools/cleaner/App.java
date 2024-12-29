package org.gametools.cleaner;

public record App(Integer id, String name, String installDir) {

    public void printSummary() {
        System.out.printf("%-10d %-10s%n", id, name);
    }
}
