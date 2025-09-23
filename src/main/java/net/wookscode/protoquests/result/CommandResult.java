package net.wookscode.protoquests.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandResult extends Result
{
    private String command;

    public CommandResult(String name, String command){
        super(name, "command");
        this.command = command;
    }

    @Override
    public HashMap<String, Object> getProps(){
        HashMap<String, Object> return_hashmap = super.getProps();
        return_hashmap.put("command", command);

        return return_hashmap;
    }

    @Override
    public String asString(){
        List<String> list_of_fields = new ArrayList<>();
        list_of_fields.add(super.getName());
        list_of_fields.add(command);
        list_of_fields.add((String) super.getProps().get("type"));

        return list_of_fields.toString();
    }

    public static CommandResult fromString(List<String> fields){
        return new CommandResult(fields.get(0), fields.get(1));
    }
}
