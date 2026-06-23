package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // Full Screen
            if(gp.fullScreenOn){
                bw.write("On");
            }
            else{
                bw.write("Off");
            }
            bw.newLine();

            // Music Volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SE Volume
            bw.write(String.valueOf(gp.music.volume));
            bw.newLine();

            bw.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadConfig(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("Config.txt"));

            String s = br.readLine();

            // Full Screen
            if(s.equals("On")){
                gp.fullScreenOn = true;
            }
            if(s.equals("Off")){
                gp.fullScreenOn = false;
            }

            // Music Volume
            s = br.readLine();
            gp.music.volume = Integer.parseInt(s);

            // SE volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
