package monster;

import Main.GamePanel;
import entity.Entity;
import entity.Projectile;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_GreenSlime extends Entity {
    GamePanel gp;
    public MON_GreenSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = "Green Slime";
        speed =2;
        maxLife = 5;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_Rock(gp);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage(){

        up1 = setup("/monster/greenslime_down_1",gp.tileSize, gp.tileSize);
        up2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/greenslime_down_1",gp.tileSize, gp.tileSize);
        down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);


    }
    public void setAction(){
        actionLockCounter ++;
        if(actionLockCounter == 120)
        {
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;
            if(i <= 25) { direction = "up";}
            else if(i > 25 && i <= 50){ direction = "down";}
            else if(i > 50 &&i <= 75){ direction = "left";}
            else if(i > 75 && i <= 100){ direction = "right";}
            actionLockCounter = 0;
        }
        int i = new Random().nextInt(199)+1;
        if(i > 99 && !projectile.alive && shotAvailableCounter == 30){
            projectile.set(worldX, worldY, direction, true, this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
    }
    public void damageReaction(){

        actionLockCounter = 0;
        direction = gp.player.direction;

    }
    public void checkDrop(){
        //CAST A DIE
        int i = new Random().nextInt(199)+1;

        //SET THE MONSTER DROP
        if(i<50){
            dropItem(new OBJ_Coin_Bronze(gp));
        }
        if(i >= 50 && i < 75){
            dropItem(new OBJ_Heart(gp));
        }
        if(i >= 75 && i < 100){
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }

}
