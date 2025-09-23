package net.wookscode.protoquests.task;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BreakTask extends Task {
    private Block block;
    private BlockPos position;

    public BreakTask(String name, Block block, int number, BlockPos position){
        super(name, "break", number, 0);
        this.block = block;
        this.position = position;
    }

    @Override
    public String toString(){
        return super.getName();
    }

    @Override
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = super.getProps();
        return_hashmap.put("block", Registries.BLOCK.getId(block).toString());
        if(position != null) {
            return_hashmap.put("position", position);
        }

        return return_hashmap;
    }

    @Override
    public String asString() {
        List<String> list_of_fields = new ArrayList<>();
        list_of_fields.add(super.getName());
        list_of_fields.add(Registries.BLOCK.getId(block).toString());
        list_of_fields.add(Integer.toString((Integer) super.getProps().get("actions_to_complete")));
        if(position != null) {
            list_of_fields.add(position.toString());
        }
        list_of_fields.add((String) super.getProps().get("type"));

        return list_of_fields.toString();
    }

    public static Task fromString(List<String> fields){

        if(fields.size() < 6){
            return new BreakTask(fields.get(0),
                    Registries.BLOCK.get(new Identifier(fields.get(1))),
                    Integer.parseInt(fields.get(2)),
                    null);
        }
        else {
            return new BreakTask(fields.get(0),
                    Registries.BLOCK.get(new Identifier(fields.get(1))),
                    Integer.parseInt(fields.get(2)),
                    blockPosInStringToBlockPos(List.of(fields.get(3), fields.get(4), fields.get(5))));
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

    private static BlockPos blockPosInStringToBlockPos(List<String> position){
        int x = Integer.parseInt(position.get(0));
        int y = Integer.parseInt(position.get(1));
        int z = Integer.parseInt(position.get(2));

        return new BlockPos(x, y, z);
    }
}
