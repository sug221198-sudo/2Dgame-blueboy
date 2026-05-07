package object;

import Main.GamePanel;

import javax.imageio.ImageIO;

public class OBJ_Key extends SuperObject {

    GamePanel gp;

    public OBJ_Key(GamePanel gp)
    {
        this.gp = gp;
        name = "key";
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            uTool.scaledImage(image, gp.tileSize, gp.tileSize);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
