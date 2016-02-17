package net.mistertgroup.heads_collections_fetcher.data;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author misterT2525
 */
@Data
@AllArgsConstructor
public class ItemStack {

    private static final Gson gson = new Gson();

    @NonNull
    private final String item;
    private int amount;
    private short data;
    private JsonObject tag;


    public static ItemStack parse(@NonNull String command) throws NullPointerException, IllegalArgumentException {
        String[] split = command.split(" ", 6);

        if (split[0].startsWith("/")) {
            split[0] = split[0].substring(1);
        }
        if (!split[0].equalsIgnoreCase("give") && !split[0].equalsIgnoreCase("minecraft:give")) {
            throw new IllegalArgumentException("Invalid command");
        }

        if (split.length < 2) {
            throw new IllegalArgumentException("Invalid command arguments");
        }

        ItemStack stack = new ItemStack(split[2]);
        if (split.length > 3) {
            stack.setAmount(Integer.valueOf(split[3]));
        }
        if (split.length > 4) {
            stack.setData(Short.valueOf(split[4]));
        }
        if (split.length > 5) {
            stack.setTag(gson.fromJson(split[5], JsonObject.class));
        }

        return stack;
    }


    public ItemStack(String item) {
        this(item, 1, (short) 0, null);
    }

    public ItemStack(String item, int amount) {
        this(item, amount, (short) 0, null);
    }

    public ItemStack(String item, int amount, short data) {
        this(item, amount, data, null);
    }


    @Override
    public String toString() {
        return Joiner.on(' ').join("/give", "@p", item, amount, data, tag != null ? tag.toString() : "{}");
    }
}
