package code;

/**
 *
 * @author Roman Lahin
 */
public class PolygonInfo {
    
    public String texName;
    public byte alphaThreshold;
    public byte blendingMode;
    public byte doubleSideRendering;
    
    public int index = 0;
    
    public PolygonInfo(String texName, byte alphaThreshold, byte blendingMode, byte doubleSideRendering) {
        this.texName = texName;
        this.alphaThreshold = alphaThreshold;
        this.blendingMode = blendingMode;
        this.doubleSideRendering = doubleSideRendering;
    }
    
    public PolygonInfo(PolygonInfo pinf) {
        this.texName = pinf.texName;
        this.alphaThreshold = pinf.alphaThreshold;
        this.blendingMode = pinf.blendingMode;
        this.doubleSideRendering = pinf.doubleSideRendering;
        
    }
    
    public boolean compare(PolygonInfo pinf) {
        return texName.equals(pinf.texName) && 
                alphaThreshold==pinf.alphaThreshold && 
                blendingMode==pinf.blendingMode && 
                doubleSideRendering==pinf.doubleSideRendering;
    }
    
}
