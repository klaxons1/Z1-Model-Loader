package code;

import java.util.ArrayList;

/**
 *
 * @author Roman Lahin
 */
public class Z1Model {

    public Polygon[] pols;
    public int uvScale;

    public Z1Model(Polygon[] pols, int uvScale) {
        this.pols = pols;
        this.uvScale = uvScale;
    }

    public void recalculateNormals() {
        for(Polygon pol : pols) {
            pol.recalculateNormals(); //Cause im lazy
            //(todo smooth normals based on angle between pols)
        }
    }

    public void getUVPolygons(ArrayList<Polygon> polygons, String texture, int width, int height, int[] texUvs) {

        for(Polygon p : pols) {
            if(!p.pinf.texName.equals(texture)) continue;

            double mu = (p.au + p.bu + p.cu) / 3;
            double mv = (p.av + p.bv + p.cv) / 3;

            mu = mu * uvScale * width / 1000000d;
            mv = mv * uvScale * height / 1000000d;

            for(int x = 0; x < texUvs.length / 4; x++) {
                if(mu >= texUvs[x * 4]
                        && mv >= texUvs[x * 4 + 1]
                        && mu <= (texUvs[x * 4] + texUvs[x * 4 + 2])
                        && mv <= (texUvs[x * 4 + 1] + texUvs[x * 4 + 3])) {
                    polygons.add(p);
                    break;
                }
            }

        }

    }

}
