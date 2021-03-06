package Processing;

import DataStructures.Vector2D;
import DataStructures.Vector3D;
import IO.ModelFileSave;
import Models.ModelData;
import Models.Vertex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OBJProcessor {
    public static void processFile(String modelName, String input, String output) {
        FileReader isr = null;
        File objFile = new File(input);
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<>();
        List<Vector2D> textures = new ArrayList<>();
        List<Vector3D> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null){
                if(line.startsWith("v ")){
                    String[] currentLine = line.split(" ");
                    Vector3D vertex = new Vector3D(Float.valueOf(currentLine[1]), Float.valueOf(currentLine[2]), Float.valueOf(currentLine[3]));
                    vertices.add(new Vertex(vertices.size(), vertex));
                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2D texture = new Vector2D(Float.valueOf(currentLine[1]), Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3D normal = new Vector3D(Float.valueOf(currentLine[1]), Float.valueOf(currentLine[2]), Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    String[] currentLine = line.split(" ");
                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");
                    processVertex(vertex1, vertices, indices);
                    processVertex(vertex2, vertices, indices);
                    processVertex(vertex3, vertices, indices);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error Reading File");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        int[] indicesArray = convertIndicesListToArray(indices);
        ModelFileSave.saveModel(new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, furthest), modelName, output);
    }

    private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static float convertDataToArrays(List<Vertex> vertices, List<Vector2D> textures,
                                             List<Vector3D> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3D position = currentVertex.getPosition();
            Vector2D textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3D normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.getX();
            verticesArray[i * 3 + 1] = position.getY();
            verticesArray[i * 3 + 2] = position.getZ();
            texturesArray[i * 2] = textureCoord.getX();
            texturesArray[i * 2 + 1] = 1 - textureCoord.getY();
            normalsArray[i * 3] = normalVector.getX();
            normalsArray[i * 3 + 1] = normalVector.getY();
            normalsArray[i * 3 + 2] = normalVector.getZ();
        }
        return furthestPoint;
    }

    private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
                                                       int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }

        }
    }

    private static void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}
