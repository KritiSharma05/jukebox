import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;
import java.sql.*;
public class catelouge
{

    void display()
    {
        System.out.println("********************************************************************************************");
        System.out.println("                                   Welcome to Jukebox                                       ");
        System.out.println("********************************************************************************************");

    }
    public static void main(String [] args) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        catelouge cat = new catelouge();
        cat.display();
        users u = new users();
        String q = u.checkuser();
        //System.out.println(u.uid);
        System.out.println(q);
        //System.out.println(u.uid);

    }
}
