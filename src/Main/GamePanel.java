package Main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

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
    public final int screenWidth = tileSize * maxScreenCol;//760 pixels
    public final int screenHeight = tileSize * maxScreenRow;//48 * 12 pixels

    //WORLD SETTING
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //FPS
    int FPS = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound  se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    //启动和停止 thread 线程
    Thread gameThread;

    //ENTITY  AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];//如果创建a，a消失 会重新补上一个新的







    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));/*setPreferredSize(new Dimension(width, height))
                                                                         = set the size of this class(JPanel)*/
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);//Canvas/双重缓冲函数作为默认值，更好地渲染

        this.addKeyListener(keyH);
        this.setFocusable(true);//this game panel can be "focused" to receive key input
    }

    public void setupGame()
    {
        aSetter.setObject();

        playMusic(0);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
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
    public void run()
    {
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

    public void update()
    {
        player.update();
    }
    public void paintComponent(Graphics g)//graphics: a class that has many functions to draw objects on the screen
    {
        super.paintComponent(g);
        Graphics2D g2 =  (Graphics2D) g;//把g转换成2d形式

        //DEBUG
        long drawStart = 0;
        if(keyH.checkDrawTime) {
            drawStart  = System.nanoTime();
        }


        //TILE
        tileM.draw(g2);//画面一定要在player之前 要不然就覆盖人物了

        //object
        for(int i = 0; i < obj.length; i++)
        {
            if(obj[i] != null){ obj[i].draw(g2, this);}
        }

        //player
        player.draw(g2);

        //UI
        ui.draw(g2);

        //DEBUG
        if(keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw time: " + passed, 10, 400);
            System.out.println("Draw time: " + passed);
        }

        g2.dispose();//dispose of this graphics context and release any system resource that is using
    }

    public void playMusic(int i)
    {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic()
    {
        music.stop();
    }

    public void playSE(int i)
    {
        se.setFile(i);
        se.play();
    }
}
