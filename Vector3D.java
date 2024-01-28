package code;

/**
 *
 * @author Roman Lahin
 */
public class Vector3D {
    
    public int x,y,z;
    public int index = 0;
    
    public Vector3D() {}
    
    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3D(Vector3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
    
    public boolean compare(Vector3D vec) {
        return (x==vec.x) && (y==vec.y) && (z==vec.z);
    }
    
    public boolean compare(int x, int y, int z) {
        return (this.x==x) && (this.y==y) && (this.z==z);
    }
    
    public void calculateNormals(Vector3D a, Vector3D b, Vector3D c) {
        long xx = (long)(a.y-b.y)*(a.z-c.z) - (long)(a.z-b.z)*(a.y-c.y);
        long yy = (long)(a.z-b.z)*(a.x-c.x) - (long)(a.x-b.x)*(a.z-c.z);
        long zz = (long)(a.x-b.x)*(a.y-c.y) - (long)(a.y-b.y)*(a.x-c.x);
        double sqrt = Math.sqrt(xx*xx + yy*yy + zz*zz)/(1<<7);

        x = (int) (xx/sqrt);
        y = (int) (yy/sqrt);
        z = (int) (zz/sqrt);
        
        if(x>Byte.MAX_VALUE) x = Byte.MAX_VALUE;
        if(y>Byte.MAX_VALUE) y = Byte.MAX_VALUE;
        if(z>Byte.MAX_VALUE) z = Byte.MAX_VALUE;
        
        if(x<Byte.MIN_VALUE) x = Byte.MIN_VALUE;
        if(y<Byte.MIN_VALUE) y = Byte.MIN_VALUE;
        if(z<Byte.MIN_VALUE) z = Byte.MIN_VALUE;
   }
    
}
