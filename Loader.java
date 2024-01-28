package code;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author Roman Lahin
 */
public class Loader {

    public static Z1Model loadModel(String path) {
        File file =  new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            dis.skipBytes(16);
            PolygonInfo[] submeshes = new PolygonInfo[dis.readByte()];
            
            for(int i=0; i<submeshes.length; i++) {
                submeshes[i] = new PolygonInfo(readString(dis),dis.readByte(),dis.readByte(),dis.readByte());
            }
            
            Vector3D[] positions = new Vector3D[dis.readShort()];
            for(int i=0;i<positions.length;i++) {
                positions[i] = new Vector3D(dis.readByte(),dis.readByte(),dis.readByte());
            }
            
            Vector3D[] normals = new Vector3D[dis.readShort()];
            for(int i=0;i<normals.length;i++) {
                normals[i] = new Vector3D(dis.readByte(),dis.readByte(),dis.readByte());
            }
            
            int uvXAdd = dis.readInt();
            int uvYAdd = dis.readInt();
            int uvScale = dis.readInt();
            Vector3D[] uvs = new Vector3D[dis.readShort()];
            for(int i=0;i<uvs.length;i++) {
                uvs[i] = new Vector3D(dis.readUnsignedByte()+uvXAdd,dis.readUnsignedByte()+uvYAdd,0);
            }
            
            Polygon[] pols = new Polygon[dis.readShort()];
            
            for(int i=0;i<pols.length;i++) {
                pols[i] = new Polygon(new PolygonInfo(submeshes[dis.readByte()]));
                dis.skipBytes(1);
            }
            
            for(int i=0;i<pols.length;i++) {
                pols[i].setPosition(positions[dis.readShort()], positions[dis.readShort()], positions[dis.readShort()]);
            }
            
            for(int i=0;i<pols.length;i++) {
                pols[i].setNormals(normals[dis.readShort()], normals[dis.readShort()], normals[dis.readShort()]);
            }
            
            for(int i=0;i<pols.length;i++) {
                pols[i].setUV(uvs[dis.readShort()], uvs[dis.readShort()], uvs[dis.readShort()]);
            }
            
            
            dis.close();
            fis.close();
            
            return new Z1Model(pols,uvScale);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void exportModel(Z1Model model, String path) {
        try {
            File file = new File(path);
            if(!file.exists()) file.createNewFile();
            
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            
            dos.writeLong(0l);
            dos.writeLong(0l);
            
            int minU = Integer.MAX_VALUE;
            int minV = Integer.MAX_VALUE;
            
            ArrayList<Vector3D> positions = new ArrayList<>();
            ArrayList<Vector3D> normals = new ArrayList<>();
            ArrayList<Vector3D> uvs = new ArrayList<>();
            ArrayList<PolygonInfo> submeshes = new ArrayList<>();
            
            for(int i=0;i<model.pols.length;i++) {
                Polygon pol = model.pols[i];
                
                pol.pinfId = find(submeshes,pol.pinf).index;
                
                pol.apid = find(positions,pol.ax,pol.ay,pol.az).index;
                pol.bpid = find(positions,pol.bx,pol.by,pol.bz).index;
                pol.cpid = find(positions,pol.cx,pol.cy,pol.cz).index;
                
                pol.anid = find(normals,pol.anx,pol.any,pol.anz).index;
                pol.bnid = find(normals,pol.bnx,pol.bny,pol.bnz).index;
                pol.cnid = find(normals,pol.cnx,pol.cny,pol.cnz).index;
                
                minU = Math.min(pol.au,minU); //a
                minV = Math.min(pol.av,minV);
                minU = Math.min(pol.bu,minU); //b
                minV = Math.min(pol.bv,minV);
                minU = Math.min(pol.cu,minU); //c
                minV = Math.min(pol.cv,minV);
                
                
                pol.auvid = find(uvs,pol.au,pol.av,0).index;
                pol.buvid = find(uvs,pol.bu,pol.bv,0).index;
                pol.cuvid = find(uvs,pol.cu,pol.cv,0).index;
            }
            
            dos.writeByte((byte)submeshes.size()); //Submeshes
            
            for(int i=0;i<submeshes.size();i++) {
                PolygonInfo submesh = submeshes.get(i);
                
                writeString(submesh.texName,dos);
                
                dos.writeByte(submesh.alphaThreshold);
                dos.writeByte(submesh.blendingMode);
                dos.writeByte(submesh.doubleSideRendering);
            }
            
            dos.writeShort((short)positions.size()); //Positions
            
            for(int i=0;i<positions.size();i++) {
                Vector3D vec = positions.get(i);
                
                dos.writeByte((byte)vec.x);
                dos.writeByte((byte)vec.y);
                dos.writeByte((byte)vec.z);
            }
            
            dos.writeShort((short)normals.size()); //Normals
            
            for(int i=0;i<normals.size();i++) {
                Vector3D vec = normals.get(i);
                
                dos.writeByte((byte)vec.x);
                dos.writeByte((byte)vec.y);
                dos.writeByte((byte)vec.z);
            }
            
            dos.writeInt(minU); //UVs
            dos.writeInt(minV);
            dos.writeInt(model.uvScale);
            
            dos.writeShort((short)uvs.size());
            
            for(int i=0;i<uvs.size();i++) {
                Vector3D vec = uvs.get(i);
                
                dos.writeByte((vec.x-minU));
                dos.writeByte((vec.y-minV));
            }
            
            dos.writeShort((short)model.pols.length); //Polygons
            
            for(int i=0;i<model.pols.length;i++) {
                Polygon pol = model.pols[i];
                
                dos.writeByte((byte)pol.pinfId); //Submesh id
                dos.writeByte(3); //Vertex count
            }
            
            for(int i=0;i<model.pols.length;i++) {
                Polygon pol = model.pols[i];
                
                dos.writeShort((short)pol.apid); //Vertex 1
                dos.writeShort((short)pol.bpid); //Vertex 2
                dos.writeShort((short)pol.cpid); //Vertex 3
                (positions.get(pol.apid)).index = -1;
                (positions.get(pol.bpid)).index = -1;
                (positions.get(pol.cpid)).index = -1;
            }
            
            int used = 0;
            for(int i=0;i<positions.size();i++) {
                if((positions.get(i)).index == -1) used++;
            }
            
            for(int i=0;i<model.pols.length;i++) {
                Polygon pol = model.pols[i];
                
                dos.writeShort((short)pol.anid); //Vertex 1
                dos.writeShort((short)pol.bnid); //Vertex 2
                dos.writeShort((short)pol.cnid); //Vertex 3
            }
            
            for(int i=0;i<model.pols.length;i++) {
                Polygon pol = model.pols[i];
                
                dos.writeShort((short)pol.auvid); //Vertex 1
                dos.writeShort((short)pol.buvid); //Vertex 2
                dos.writeShort((short)pol.cuvid); //Vertex 3
            }
            
            dos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Vector3D find(ArrayList<Vector3D> vec, Vector3D toFind) {
        return find(vec, toFind.x, toFind.y, toFind.z);
    }
    
    public static Vector3D find(ArrayList<Vector3D> vec, int x, int y, int z) {
        for(int i=0;i<vec.size();i++) {
            Vector3D vec3d = vec.get(i);
            if(vec3d.compare(x,y,z)) return vec3d;
        }
        
        Vector3D el = new Vector3D(x,y,z);
        el.index = vec.size();
        vec.add(el);
        
        return el;
    }
    
    public static PolygonInfo find(ArrayList<PolygonInfo> vec, PolygonInfo pinf) {
        for(int i=0;i<vec.size();i++) {
            PolygonInfo pinf2 = vec.get(i);
            if(pinf.compare(pinf2)) return pinf2;
        }
        
        PolygonInfo el = new PolygonInfo(pinf);
        el.index = vec.size();
        vec.add(el);
        
        return el;
    }
    
    public static String readString(DataInputStream dis) throws IOException {
        char[] data = new char[dis.readByte()];
        
        for(int i=0;i<data.length;i++) {
            data[i] = (char)dis.readByte();
        }
        
        return String.valueOf(data);
    }
    
    public static void writeString(String str, DataOutputStream dos) throws IOException {
        dos.writeByte((byte)str.length());
        
        for(int i=0;i<str.length();i++) {
            dos.writeByte(str.charAt(i));
        }
    }

}
