import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
public class Demo
{
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    File f=new File("C:\\Users\\kriti sharma\\OneDrive\\Desktop\\jukebox project\\songs\\song1.wav");
        AudioInputStream a= AudioSystem.getAudioInputStream(f);
        Clip clip=AudioSystem.getClip();
        clip.open(a);
        clip.start();
        Scanner obj=new Scanner(System.in);
        int ch=obj.nextInt();
        if(ch==1)
        {
            clip.stop();
            clip.close();
        }
    }
}
