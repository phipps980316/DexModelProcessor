package IO;

import Models.ModelData;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class ModelFileSave {
    public static void saveModel(ModelData modelData, String modelName, String output){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(output+"/"+modelName+".modeldata");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(modelData);
            objectOutputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
