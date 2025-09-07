package net.wookscode.protoquests.task;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BreakTask extends Task{
    private Block block;
    private int number;
    private Vec3d position;

    public BreakTask(String name, Block block, int number, Vec3d position){
        super(name, "break");
        this.block = block;
        this.number = number;
        this.position = position;
    }

    public String getName(){
        return super.getName();
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String toString(){
        return super.getName();
    }

    @Override
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = super.getProps();
        return_hashmap.put("block", Registries.BLOCK.getId(block).toString());
        return_hashmap.put("number", String.valueOf(number));
        if(position != null) {
            return_hashmap.put("position", position.toString());
        }

        return return_hashmap;
    }

    @Override
    public String asString() {
        List<String> list_of_fields = new ArrayList<>();
        list_of_fields.add(super.getName());
        list_of_fields.add(Registries.BLOCK.getId(block).toString());
        list_of_fields.add(Integer.toString(number));
        if(position != null) {
            list_of_fields.add(position.toString());
        }
        list_of_fields.add((String) super.getProps().get("type"));

        return list_of_fields.toString();
    }

    public static Task fromString(String string){
        List<String> list_of_fields = listInStringToList(string);

        if(list_of_fields.size() < 6){
            return new BreakTask(list_of_fields.get(0),
                    Registries.BLOCK.get(new Identifier(list_of_fields.get(1))),
                    Integer.parseInt(list_of_fields.get(2)),
                    null);
        }
        else {
            return new BreakTask(list_of_fields.get(0),
                    Registries.BLOCK.get(new Identifier(list_of_fields.get(1))),
                    Integer.parseInt(list_of_fields.get(2)),
                    vectorInStringToVector(list_of_fields.get(3)));
        }
    }

    private static List<String> listInStringToList(String string) {

        StringBuilder sb = new StringBuilder(string);

        sb.deleteCharAt(string.length() - 1);
        sb.deleteCharAt(0);

        string = sb.toString();

        List<String> return_list = new ArrayList<>();
        for(String element : List.of(string.split(",(?![^(]*\\))"))){
            return_list.add(element.strip());
        }

        return return_list;
    }

    private static List<String> fieldsInStringToList(String string){
       return new ArrayList<>();
    }

    private static Vec3d vectorInStringToVector(String string){
        double x = Double.parseDouble(listInStringToList(string).get(0));
        double y = Double.parseDouble(listInStringToList(string).get(1));
        double z = Double.parseDouble(listInStringToList(string).get(2));

        return new Vec3d(x, y, z);
    }
}
