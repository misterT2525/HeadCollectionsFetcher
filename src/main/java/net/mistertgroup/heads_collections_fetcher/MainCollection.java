package net.mistertgroup.heads_collections_fetcher;

import net.mistertgroup.heads_collections_fetcher.data.Head;
import net.mistertgroup.heads_collections_fetcher.data.ItemStack;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author misterT2525
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MainCollection {

    public static Multimap<String, Head> fetch(@NonNull Gson gson) throws IOException, NullPointerException, IllegalArgumentException {
        Multimap<String, Head> collections = TreeMultimap.create();
        Document document = Jsoup.connect("http://heads.freshcoal.com/maincollection.php").get();
        findTabNames(document).forEach((id, name) -> collections.putAll(name, findHeads(id, document, gson)));
        return collections;
    }

    private static Map<String, String> findTabNames(Document document) {
        return document.select("#tabs .center1 ul li a").parallelStream()
            .collect(Collectors.toMap(e -> e.attr("href").substring(1),
                Element::text));
    }

    private static List<Head> findHeads(String tabId, Document document, Gson gson) {
        return document.select("#" + tabId + " .heads img").parallelStream()
            .map(e -> e.attr("data-clipboard-text"))
            .map(command -> ItemStack.parse(command, gson))
            .map(stack -> Head.parse(stack, gson))
            .collect(Collectors.toList());
    }
}
