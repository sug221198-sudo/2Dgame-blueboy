package object;

import Main.GamePanel;
import entity.Entity;

public class OBJ_Sword_Normal extends Entity {
    public OBJ_Sword_Normal(GamePanel gp) {

        super(gp);

        type = type_sword;
        name = "Normal sword";
        down1 = setup("/objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]" + "\n An old sword";
    }

}
