package net.wookscode.protoquests.result;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XpResult extends Result{
    private int amount;

    public XpResult(String name, int amount){
        super(name, "xp");
        this.amount = amount;
    }

    @Override
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = super.getProps();
        return_hashmap.put("amount", amount);

        return return_hashmap;
    }

    @Override
    public String asString(){
        List<String> list_of_fields = new ArrayList<>();
        list_of_fields.add(super.getName());
        list_of_fields.add(Integer.toString(amount));
        list_of_fields.add((String) super.getProps().get("type"));

        return list_of_fields.toString();
    }

    public static XpResult fromString(List<String> fields){
        return new XpResult(fields.get(0), Integer.parseInt(fields.get(1)));
    }
}
