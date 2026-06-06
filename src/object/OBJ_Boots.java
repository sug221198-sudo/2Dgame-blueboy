package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;

public class OBJ_Boots extends Entity {

    GamePanel gp;

    public OBJ_Boots(GamePanel gp) {
        super(gp);
        name = "boots";
        down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);

    }
}
