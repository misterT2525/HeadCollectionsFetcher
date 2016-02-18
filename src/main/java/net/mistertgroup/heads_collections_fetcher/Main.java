package net.mistertgroup.heads_collections_fetcher;

import net.mistertgroup.heads_collections_fetcher.data.Head;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author misterT2525
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Main {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File outputDir = new File("dist");

    public static void main(String[] args) throws IOException {
        Stopwatch totalStopwatch = Stopwatch.createStarted();

        Stopwatch fetchStopwatch = Stopwatch.createStarted();
        Map<String, Collection<Head>> collections = MainCollection.fetch(gson).asMap();
        fetchStopwatch.stop();

        Stopwatch saveStopwatch = Stopwatch.createStarted();
        collections.forEach(Main::saveCollection);
        saveStopwatch.stop();

        totalStopwatch.stop();

        System.out.println("Fetched " + collections.size() + " collections to '" + outputDir.getCanonicalPath() + "'");
        printCounts(collections);

        System.out.println("Fetch Time: " + fetchStopwatch.toString());
        System.out.println("Save  Time: " + saveStopwatch.toString());
        System.out.println("Total Time: " + totalStopwatch.toString());
    }

    private static void saveCollection(@NonNull String name, Collection<Head> heads) {
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Failed to create dist directory");
        }

        File file = new File(outputDir, name + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(heads));
            writer.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCounts(Map<String, Collection<Head>> collections) {
        int maxCollectionNameLength = collections.keySet().stream().mapToInt(String::length).max().orElse(0);
        int maxNameLength = Math.max(maxCollectionNameLength, "Total".length());

        int sum = collections.values().stream().mapToInt(Collection::size).sum();

        collections.forEach((name, heads) -> {
            String indent = Strings.repeat(" ", maxNameLength - name.length());
            System.out.println(name + indent + ": " + heads.size() + " heads");
        });

        String indent = Strings.repeat(" ", maxNameLength - "Total".length());
        System.out.println("Total" + indent + ": " + sum + " heads");
    }
}
