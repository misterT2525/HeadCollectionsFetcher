package net.mistertgroup.heads_collections_fetcher.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Base64;

/**
 * @author misterT2525
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Head {

    private final static Gson gson = new Gson();

    private String name;
    @NonNull
    private final String url;
    @NonNull
    private final String uuid;

    public static Head parse(@NonNull ItemStack stack) throws NullPointerException, IllegalArgumentException {
        if (!stack.getItem().equalsIgnoreCase("skull") && !stack.getItem().equalsIgnoreCase("minecraft:skull")) {
            throw new IllegalArgumentException("Not Head");
        }
        if (stack.getData() != 3) {
            throw new IllegalArgumentException("Not Player Head");
        }

        String name = null;
        try {
            name = stack.getTag().getAsJsonObject("display").getAsJsonPrimitive("Name").getAsString();
        } catch (Exception ignore) {
            // Ignore because name is not required
        }

        try {
            String url = null;
            for (JsonElement object : stack.getTag().getAsJsonObject("SkullOwner").getAsJsonObject("Properties").getAsJsonArray("textures")) {
                if (url == null) {
                    url = findURLFromTextures(object.getAsJsonObject().getAsJsonPrimitive("Value").getAsString(), "SKIN");
                }
            }
            String uuid = stack.getTag().getAsJsonObject("SkullOwner").getAsJsonPrimitive("Id").getAsString();
            return new Head(name, url, uuid);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid tag data", e);
        }
    }

    private static String findURLFromTextures(String texturesJson, String type) {
        try {
            JsonObject root = gson.fromJson(new String(Base64.getDecoder().decode(texturesJson)), JsonObject.class);

            return root.getAsJsonObject("textures").getAsJsonObject(type).getAsJsonPrimitive("url").getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
