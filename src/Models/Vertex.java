package Models;

import DataStructures.Vector3D;

public class Vertex {
    private static final int NO_INDEX = -1;

    private final Vector3D position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;
    private final int index;
    private final float length;

    public Vertex(int index, Vector3D position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    public int getIndex(){
        return index;
    }

    public float getLength(){
        return length;
    }

    public boolean isSet(){
        return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    public boolean hasSameTextureAndNormal(int textureIndex, int normalIndex){
        return this.textureIndex==textureIndex && this.normalIndex==normalIndex;
    }

    public void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }

    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }

    public Vector3D getPosition() {
        return position;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }
}
