package code;

import java.util.ArrayList;

/**
 *
 * @author Roman Lahin
 */
public class ModelReparser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] models = new String[]{
            "Corridor1_00.z1", "Room1_00.z1", "Room2_00.z1", "Storage1_00.z1", "Storage2_00.z1", "SelskiyDom1_00.z1",
            "Box1_00.z1", "MetalCase1_00.z1", "IndustryBuild1_00.z1", "IndustryBuild2_00.z1",
            "IndustryBuild3_00.z1", "IndustryBuild4_00.z1", "IndustryBuild5_00.z1", "IndustryBuild6_00.z1",
            "IndustryBuildA1_00.z1", "IndustryBuildA2_00.z1", "IndustryBuildA3_00.z1",
            "IndustryBuildB1_00.z1", "IndustryBuildB2_00.z1", "GrassPlot1_00.z1", "GrassPlot2_00.z1",
            "Door1_00.z1", "Door2_00.z1", "DoorStud1_00.z1", "DoorStud2_00.z1",
            "ConcreteSlabs1_00.z1", "CBlock1_00.z1", "Box2_00.z1",
            "WoodTable1_00.z1", "WoodPanel1_00.z1", "WindowSill1_00.z1",
            "Window1_00.z1", "WFence1_00.z1", "WFence2_00.z1", "UAZ1_00.z1",
            "MetalGate1_00.z1"
        };

        for(int i = 0; i < models.length; i++) {
            Z1Model model = Loader.loadModel(models[i]);
            model.recalculateNormals();

            int[] tex1uvs = new int[]{ //x,y,width,height
                32, 64, 64, 32,
                128, 64, 64, 64,
                192, 64, 16, 32,
                128, 128, 32, 64,
                224, 192, 32, 64,
                0, 192, 64, 64, //Barrel
                208, 128, 48, 64,
                192, 0, 64, 64
            };
            int[] tex2uvs = new int[]{
                64, 112, 32, 64,
                96, 96, 64, 48,
                240, 208, 16, 32
            };

            ArrayList<Polygon> pols = new ArrayList<>();
            model.getUVPolygons(pols, "texobj1.png", 256, 256, tex1uvs);
            model.getUVPolygons(pols, "texobj2.png", 256, 256, tex2uvs);
            for(Polygon pol : pols) {
                pol.pinf.alphaThreshold = -1;
            }
            System.out.println(pols.size() + " shiny polygons!");

            Loader.exportModel(model, models[i]);
            System.out.println(models[i] + " exported!");
        }

    }

}
