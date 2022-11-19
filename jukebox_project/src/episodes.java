import javax.sound.sampled.*;
import java.io.File;
import java.io.*;
import java.sql.*;
import java.util.*;

public class episodes
{
    Scanner obj=new Scanner(System.in);
    Long currentFrame;
    String status;
    Clip clip;
    AudioInputStream audioInputStream;
    String path;
    int sid;
    public void pause() throws ClassNotFoundException, SQLException {
        if (status.equals("paused")) {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame = this.clip.getMicrosecondPosition();
        this.clip.stop();
        this.status = "paused";
        long tot= clip.getMicrosecondLength();
        long micro=clip.getMicrosecondPosition();//microseconds to
        System.out.println(clip.getMicrosecondLength()/1000000);
        System.out.println("played in seconds:"+micro/1000000);
        System.out.println("remaining time for this song:"+(tot-micro)/1000000);
    }
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        if (status.equals("play"))
        {
            System.out.println("Audio is already "+
                    "being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        long tot= clip.getMicrosecondLength();
        long micro=clip.getMicrosecondPosition();//microseconds to
        currentFrame = 0L;
        clip.stop();
        clip.close();
        System.out.println("played in seconds:"+micro/1000000);
        System.out.println("remaining time for this song:"+(tot-micro)/1000000);
    }
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        if (c > 0 && c < clip.getMicrosecondLength())
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(path).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    void displaynext() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        clip.close();
        Connection con=getcon();
        Statement s=con.createStatement();
        sid=sid+1;
            ResultSet r = s.executeQuery("select paths,eid from episode where eid='" + sid + "'");
            while (r.next()) {
                path = r.getString(1);
                File f = new File(path);
                AudioInputStream a = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                this.clip = clip;
                // clip.open(a);
                this.status = "play";
                this.clip.open(
                        a
                );
                this.clip.start();
            }


    }
    void displayprevious() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.close();
        Connection con=getcon();
        Statement s=con.createStatement();
        sid=sid-1;
            ResultSet r = s.executeQuery("select paths,eid from episode where eid='" + sid + "'");
            while (r.next()) {
                path = r.getString(1);
                File f = new File(path);
                AudioInputStream a = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                this.clip = clip;
                // clip.open(a);
                this.status = "play";
                this.clip.open(
                        a
                );
                this.clip.start();
            }

    }

    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }
    public Connection getcon() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c= DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","kriti@12345");
        return c;
    }
    public void displayepi() throws SQLException, ClassNotFoundException
    {
        Connection con=getcon();
        Statement s=con.createStatement();
        ResultSet r=s.executeQuery("select eid,ename,artist,pdid from episode");
        System.out.println("List of episodes");
        while(r.next())
        {
            System.out.println(r.getString(1)+"  "+r.getString(2)+"  "+r.getString(3)+"  "+r.getInt(4));
        }

    }
    public void displayartist() throws SQLException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println(" Enter the artist name 1)jenny 2)noah 3)ashley");
        int num= obj.nextInt();
        if(num==1) {
            ResultSet r = s.executeQuery("select eid,ename,artist  from episode where artist='jenny'");
            while (r.next()) {
                System.out.println(r.getInt(1) + " " + r.getString(2) + " " + r.getString(3));
            }
            System.out.println("Enter the episode id you want to play :");
            int si= obj.nextInt();
            sid=si;
            ResultSet r1=s.executeQuery("select paths from episode where artist='jenny' and eid='"+si+"'");

            while(r1.next()) {
                path = r1.getString(1);
                File f=new File(path);
                AudioInputStream a= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        a
                );
                this.clip.start();
                //microseconds to seconds
                //clip.open();
                //this.status="play";
                while (true) {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    System.out.println("6. Next");
                    System.out.println("7. Previous");
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4) {
                        break;
                    }
                }
            }
        }
        if(num==2) {
            ResultSet r = s.executeQuery("select eid,ename,artist  from episode where artist='noah'");
            while (r.next()) {
                System.out.println(r.getInt(1) + " " + r.getString(2) + " " + r.getString(3));
            }
            System.out.println("Enter the episode id you want to play :");
            int si= obj.nextInt();
            sid=si;
            ResultSet r1=s.executeQuery("select paths from episode where artist='noah' and eid='"+si+"'");

            while(r1.next())
            {
                path=r1.getString(1);
                File f=new File(path);
                AudioInputStream a= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        a
                );
                this.clip.start();
                //microseconds to seconds
                //clip.open();
                //this.status="play";
                while (true)
                {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    System.out.println("6. next");
                    System.out.println("7. previous");
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                    {
                        break;
                    }
                }
            }
        }
        if(num==3)
        {
            ResultSet r=s.executeQuery("select eid,ename,artist  from episode where artist='ashley'");
            while(r.next())
            {
                System.out.println(r.getInt(1)+" "+r.getString(2)+" "+r.getString(3));
            }
            System.out.println("Enter the episode id you want to play :");
            int si= obj.nextInt();
            sid=si;
            ResultSet r1=s.executeQuery("select paths from episode where artist='ashley' and eid='"+si+"'");

            while(r1.next())
            {
                path=r1.getString(1);
                File f=new File(path);
                AudioInputStream a= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        a
                );
                this.clip.start();
                //microseconds to seconds
                //clip.open();
                //this.status="play";
                while (true)
                {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    System.out.println("6. next");
                    System.out.println("7. previous");
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4) {
                        break;
                    }
                }
            }
        }
    }
    private void gotoChoice(int ch) throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        switch (ch) {
            case 1:
                pause();
                break;
            case 2:
                resumeAudio();
                break;
            case 3:
                restart();
                break;
            case 4:
                stop();
                break;
            case 5:
                System.out.println("Enter time (" + 0 +
                        ", " + clip.getMicrosecondLength() + ")");
                Scanner sc = new Scanner(System.in);
                long c1 = sc.nextLong();
                jump(c1);
                break;
            case 6:
                displaynext();
                break;
            case 7:
                displayprevious();
                break;
        }
    }
    public void displayname() throws SQLException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("Enter the podacast id :");
        int p=obj.nextInt();
        System.out.println("Enter the episode name :");
        String n=obj.next();
        ResultSet r=s.executeQuery("select eid,ename ,artist,dates from episode where ename='"+n+"' and pdid='"+p+"'");

        while(r.next()) {
            System.out.println(r.getInt(1) + "  " + r.getString(2) + "  " + r.getString(3) + "  " + r.getDate(4));
            /*System.out.println("Enter the episode id you want to play :");
            String si = obj.next();*/
            sid=r.getInt(1);
        }
            ResultSet r1 = s.executeQuery("select paths from episode where ename='" + n + "' and pdid='" + p + "'");

            while (r1.next())
            {
                path = r1.getString(1);
                File f=new File(path);
                AudioInputStream a= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        a
                );
                this.clip.start();
                //microseconds to seconds
                //clip.open();
                //this.status="play";
                while (true) {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    System.out.println("6. next");
                    System.out.println("7. previous");
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4) {
                        break;
                    }
                }
            }


    }


}
