package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;


public class OBJ_Chest extends Entity {

    GamePanel gp;

    public OBJ_Chest(GamePanel gp)
    {
        super(gp);
        name = "chest";
        down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
    }
}
