package Main;

import entity.Entity;
import entity.Player;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GamePanel extends JPanel implements Runnable
{
    //SCREEN SETTING
    final int originalTileSize = 16;//16 x 16 size
    final int scale = 3;

    /*
    set player's default position--用来测试
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4; */
    public final int tileSize = originalTileSize * scale;//48 x 48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    //Window mode
    public final int screenWidth = tileSize * maxScreenCol;//760 pixels
    public final int screenHeight = tileSize * maxScreenRow;//48 * 12 pixels

    //Full screen

    //WORLD SETTING
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound  se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    //启动和停止 thread 线程
    Thread gameThread;

    //ENTITY  AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[20];//如果创建a，a消失 会重新补上一个新的
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public InteractiveTile iTile[] = new InteractiveTile[50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    //GAME STATE
    //体现很多屏幕界面
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));/*setPreferredSize(new Dimension(width, height))
                                                                         = set the size of this class(JPanel)*/
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);//Canvas/双重缓冲函数作为默认值，更好地渲染

        this.addKeyListener(keyH);
        this.setFocusable(true);//this game panel can be "focused" to receive key input
    }
    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        //playMusic(0);
        gameState = titleState;

    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    /*
    public void run()//启动thread所要单独用的run方法
    {
        double drawInterval = 100000100 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null)
        {
            //long currentTime = System.nanoTime();//用纳秒来检查当前的系统时间
            //System.out.println("current time"+ currentTime);

            //step 1. Update:update information such as character position
            update();
            //step 2. Draw: draw the screen with the updated information
            repaint();//call paintComponent


            try{
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;//看sleep里面long的单位是million

                if(remainingTime < 0){ remainingTime = 0;}
                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

        }
    }
    */
    @Override
    public void run() {
        double drawInterval  = 1000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1)
            {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000)
            {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update() {
        if(gameState == playState)
        {
            player.update();

            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if(monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    else{
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    else{
                        projectileList.remove(i);
                    }
                }
            }
            for(int i = 0; i < particleList.size(); i++){
                if(particleList.get(i) != null){
                    if(particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    else{
                        particleList.remove(i);
                    }
                }
            }
            for(int i = 0; i < iTile.length; i++){
                if(iTile[i] != null) {
                    iTile[i].update();
                }
            }
        }
        if(gameState == pauseState) {
            //暂时不更新用户信息
        }
    }
    public void paintComponent(Graphics g){//graphics: a class that has many functions to draw objects on the screen

        super.paintComponent(g);
        Graphics2D g2 =  (Graphics2D) g;//把g转换成2d形式

        //DEBUG
        long drawStart = 0;
        if(keyH.showDebugText) {
            drawStart  = System.nanoTime();
        }
        //TITLE SCREEN
        if(gameState == titleState) {
            ui.draw(g2);
        }
        //Others
        else{
            //TILE
            tileM.draw(g2);//画面一定要在player之前 要不然就覆盖人物了

            //INTERACTIVE TILE
            for(int i = 0; i < iTile.length; i++) {
                if(iTile[i] != null) {
                    iTile[i].draw(g2);
                }
            }

            //ADD ENTITY TO THE LIST
            entityList.add(player);

            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }
            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }
            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }
            for(int i = 0; i < particleList.size(); i++) {
                if(particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

            //SORT
            Collections.sort(entityList,  new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {
                    //TODO Auto-generated method stub
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST
            entityList.clear();

            //UI
            ui.draw(g2);

        }


        //DEBUG
        if(keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("worldX: " + player.worldX, x, y); y += lineHeight;
            g2.drawString("worldY: " + player.worldY, x, y); y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y); y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;

            g2.drawString("Draw time: " + passed, x, y);
            System.out.println("Draw time: " + passed);
        }

        g2.dispose();//dispose of this graphics context and release any system resource that is using
    }
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
