package net.mistertgroup.heads_collections_fetcher;

import net.mistertgroup.heads_collections_fetcher.data.Head;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * @author misterT2525
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Main {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File outputDir = new File("dist");

    public static void main(String[] args) throws IOException {
        MainCollection.fetch(gson).asMap().forEach(Main::saveCollection);
    }

    private static void saveCollection(@NonNull String name, Collection<Head> heads) {
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IllegalStateException("Failed to create dist directory");
        }

        File file = new File(outputDir, name + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(heads));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
