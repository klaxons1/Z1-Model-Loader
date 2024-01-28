package code;

/**
 *
 * @author Roman Lahin
 */
public class Polygon {
    public PolygonInfo pinf;
    
    //poses
    public int ax,ay,az;
    public int bx,by,bz;
    public int cx,cy,cz;
    
    //normals
    public int anx,any,anz;
    public int bnx,bny,bnz;
    public int cnx,cny,cnz;
    
    //uvm
    public int au,av;
    public int bu,bv;
    public int cu,cv;
    
    //Used for export
    public int nx,ny,nz;
    public int pinfId, apid, bpid, cpid, anid, bnid, cnid, auvid, buvid, cuvid;
    
    public Polygon(PolygonInfo pinf) {
        this.pinf = pinf;
    }
    
    public Polygon(PolygonInfo pinf, 
            Vector3D a, Vector3D b, Vector3D c, 
            Vector3D na, Vector3D nb, Vector3D nc,
            Vector3D auv, Vector3D buv, Vector3D cuv) {
        this.pinf = pinf;
        setPosition(a,b,c);
        setNormals(na,nb,nc);
        setUV(auv,buv,cuv);
    }
    
    public void setPosition(Vector3D a, Vector3D b, Vector3D c) {
        ax = a.x; ay = a.y; az = a.z;
        bx = b.x; by = b.y; bz = b.z;
        cx = c.x; cy = c.y; cz = c.z;
        
        Vector3D normal = new Vector3D();
        normal.calculateNormals(a,b,c);
        nx = normal.x; ny = normal.y; nz = normal.z;
    }
    
    public void setNormals(Vector3D na, Vector3D nb, Vector3D nc) {
        anx = na.x; any = na.y; anz = na.z;
        bnx = nb.x; bny = nb.y; bnz = nb.z;
        cnx = nc.x; cny = nc.y; cnz = nc.z;
    }
    
    public void setUV(Vector3D auv, Vector3D buv, Vector3D cuv) {
        au = auv.x; av = auv.y;
        bu = buv.x; bv = buv.y;
        cu = cuv.x; cv = cuv.y;
    }
    
    public void recalculateNormals() {
        anx = bnx = cnx = nx;
        any = bny = cny = ny;
        anz = bnz = cnz = nz;
    }
    
}
