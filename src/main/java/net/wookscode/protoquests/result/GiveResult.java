package net.wookscode.protoquests.result;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GiveResult extends Result{
    private Item item;
    private int amount;

    public GiveResult(String name, Item item, int amount){
        super(name, "give");
        this.item = item;
        this.amount = amount;
    }

    @Override
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = super.getProps();
        return_hashmap.put("item", item);
        return_hashmap.put("amount", amount);

        return return_hashmap;
    }

    @Override
    public String asString(){
        List<String> list_of_fields = new ArrayList<>();
        list_of_fields.add(super.getName());
        list_of_fields.add(Registries.ITEM.getId(item).toString());
        list_of_fields.add(Integer.toString(amount));
        list_of_fields.add((String) super.getProps().get("type"));

        return list_of_fields.toString();
    }
}
