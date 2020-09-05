package DataStructures;

public class Vector3D extends Vector{
    public Vector3D(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getZ() {
        return z;
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    private float lengthSquared() {
        return x * x + y * y + z * z;
    }
}
