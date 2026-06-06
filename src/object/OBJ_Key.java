package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;


public class OBJ_Key extends Entity {

    GamePanel gp;

    public OBJ_Key(GamePanel gp)
    {
        super(gp);
        name = "key";
        down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
        description = "[" + name + "]" + "\nIt opens a door";
    }

}
