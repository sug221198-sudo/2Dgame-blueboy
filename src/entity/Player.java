package entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity
{
    GamePanel gp;
    KeyHandler keyH;
    public int hasKey = 0;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH)
    {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize / 2);
        screenY = gp.screenHeight/2 - (gp.tileSize / 2);

        //solidArea = new Rectangle(0,0,gp.tileSize, gp.tileSize);
        solidArea = new Rectangle();//不完全的身体碰撞
        solidArea.x = 8;
        solidArea.y = 16;
        //record the default
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues()
    {
        worldX = gp.tileSize * 23;//stating position(23,21)
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage()
    {
        /*
        try//load image
        {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        */

        up1 = setup("boy_up_1");
        down1 = setup("boy_down_1");
        left1 = setup("boy_left_1");
        right1 = setup("boy_right_1");
        up2 = setup("boy_up_2");
        down2 = setup("boy_down_2");
        left2 = setup("boy_left_2");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imageName)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image= null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image= uTool.scaledImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void update()
    {
        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true|| keyH.upPressed == true || keyH.downPressed == true)
        {
            if(keyH.upPressed == true) { direction = "up"; }
            if(keyH.downPressed == true){ direction = "down";}
            if(keyH.leftPressed == true){ direction = "left"; }
            if(keyH.rightPressed == true){ direction = "right"; }


            //check tile code
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //if collision is false , can not move from this direction
            if(collisionOn == false)
            {
                switch(direction)
                {
                    case "up":  worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 10)//玩家10帧更改一次
            {
                if(spriteNum == 1){ spriteNum = 2; }
                else if(spriteNum == 2){ spriteNum = 1; }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int i)
    {
        if(i != 999){

            String objectName = gp.obj[i].name;
            switch(objectName) {
                case "key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a key!");
                    break;
                case "Door":
                    gp.playSE(3);
                    if (hasKey > 0) {
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("You open a Door!");
                    }

                    else{
                        gp.ui.showMessage("You need a key to open the Door!");
                    }
                    break;
                case "boots":
                    gp.playSE(2);
                    speed += 1;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a boot! speed up!");
                    break;
                case "chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;

            }
        }

    }

    public void draw(Graphics2D g2)
    {
        //g2.setColor(Color.WHITE);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
        switch(direction) {
            case "up":
                if(spriteNum == 1){ image = up1;}
                if(spriteNum == 2){ image = up2;}
                break;
            case "down":
                if(spriteNum == 1){ image = down1;}
                if(spriteNum == 2){ image = down2;}
                break;
            case "left":
                if(spriteNum == 1){ image = left1;}
                if(spriteNum == 2){ image = left2;}
                break;
            case "right":
                if(spriteNum == 1){ image = right1;}
                if(spriteNum == 2){ image = right2;}
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize,  gp.tileSize, null);
    }

}
