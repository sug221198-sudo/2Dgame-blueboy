package tile;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp)
    {
        this.gp = gp;

        tile = new Tile[10];//十种瓷砖
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage() {
            //tile[0] = new Tile();
            //tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass00.png"));
            setup(0, "grass00", false);
            setup(1, "wall", true);
            setup(2, "water00", true);
            setup(3, "earth", false);
            setup(4, "tree", true);
            setup(5, "road00", false);

    }

    public void setup(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+ imageName + ".png"));
            tile[index].image = uTool.scaledImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(String fileName)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while(col < gp.maxWorldCol && row < gp.maxWorldRow)
            {
                String line = br.readLine();//read the line of text
                while (col < gp.maxWorldCol)
                {
                    String numbers[] = line.split(" ");//splits this string around matches,the given regular expression

                    int num = Integer.parseInt(numbers[col]);//切割相等
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){ col = 0; row++;}
            }
            br.close();
        }
        catch(Exception e){
        }
    }

    public void draw(Graphics2D g2)
    {
        int worldCol = 0;
        int worldRow = 0;
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow)
        {
            int tileNum = mapTileNum[worldCol][worldRow];

            //物理(x,y) [col, row] 屏幕（x*48, y*48)
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;//到原点的具体差距
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY,null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol)
            {
                worldCol = 0;
                worldRow++;
            }

        }
    }
}
