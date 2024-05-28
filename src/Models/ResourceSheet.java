package Models;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.*;

public class ResourceSheet implements Serializable,GameComponent {
    private static final long serialVersionUID = 8607053040307709847L;
    private String name;
    private LinkedHashMap<String, Integer[]> resources;
    //private int value;
    //private final int startValue;

    public ResourceSheet(String name, String resTitle){
        this.resources = new LinkedHashMap<>();
        Integer[] vals = new Integer[2];
        vals[0] = 0;
        vals[1] = 0;
        resources.put(resTitle,vals);

        this.name = name;
        //this.startValue = 0;
        //this.value = 0;
    }

    public ResourceSheet(String name, String resTitle, int value){
        this.resources = new LinkedHashMap<>();
        Integer[] vals = new Integer[2];
        vals[0] = value;
        vals[1] = 0;
        resources.put(resTitle,vals);

        this.name = name;
        //this.startValue = value;
        //this.value = value;
    }

    public ResourceSheet(String name, String resTitle, int value, int startValue){
        this.resources = new LinkedHashMap<>();
        Integer[] vals = new Integer[2];
        vals[0] = value;
        vals[1] = startValue;
        resources.put(resTitle,vals);

        this.name = name;
        //this.startValue = startValue;
        //this.value = value;
    }

    public ResourceSheet(String name, LinkedHashMap<String, Integer[]> resArray){
        this.name = name;

        LinkedHashMap<String, Integer[]> copy = new LinkedHashMap<>();
        for (HashMap.Entry<String, Integer[]> entry : resArray.entrySet())
        {
            copy.put(entry.getKey(),
                    // Or whatever List implementation you'd like here.
                    Arrays.copyOf(entry.getValue(), entry.getValue().length));
        }
        this.resources = copy;


    }

    public String getName(){
        return name;
    }

    public int getValue(String name) {
        return resources.get(name)[0];
    }

    public String getText(){
        return "Resource: " + name;
    }

    public BufferedImage getImage(){
        return null;
    }

    public void setValue(String resName, int value) {
        resources.get(resName)[0] = value;
    }

    public void incrementValue(String resName){
        if(resources != null){
            resources.get(resName)[0]++;
        }
    }

    public void decrementValue(String resName){
        if(resources != null){
            resources.get(resName)[0]--;
        }
    }

    public ResourceSheet copy(){
        return new ResourceSheet(this.name,this.resources);
    }

    public String toString(){
        return String.format("Resource: %s;", name);
    }

    public void setName(String name){
        this.name = name;
    }

    public LinkedHashMap<String, Integer[]> getResources(){return this.resources;}

    public Map.Entry<String, Integer[]> getFirstEntry(){
        if(resources != null){
            return resources.entrySet().iterator().next();
        }else{
            return null;
        }
    }

    public Map.Entry<String, Integer[]> getEntry(int index){
        if(resources != null){
            ArrayList<Map.Entry<String, Integer[]>> list = new ArrayList<>(resources.entrySet());
            return list.get(index);
        }else{
            return null;
        }
    }
}
