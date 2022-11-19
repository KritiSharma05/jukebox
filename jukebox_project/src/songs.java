import java.sql.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class songs
{
   Scanner obj= new Scanner(System.in);

    Long currentFrame;
    String status;
    Clip clip;
    AudioInputStream audioInputStream;
    String path;

    int sid;
    //int count;
    //int a[];
    //int i;
    //ArrayList<Integer> left=new ArrayList<>();
    public songs()
    {

    }
    /*public songs( String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(path);
        audioInputStream = AudioSystem.getAudioInputStream(file);
        clip=AudioSystem.getClip();
        clip.open(audioInputStream);
        clip = AudioSystem.getClip();
        this.play();
    }*/

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
        System.out.println("duration of the song :"+clip.getMicrosecondLength()/1000000);
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

       // System.out.println("duration of the song :"+clip.getMicrosecondLength()/1000000);
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
        System.out.println(sid);
        ResultSet r = s.executeQuery("select paths,eid from episode where eid='" + sid + "'");
        while (r.next()) {
            path = r.getString(1);
           // System.out.println(path);
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
        /*clip.close();
        //resetAudioStream();
        Connection con = getcon();
        Statement s = con.createStatement();
        ListIterator<Integer> it = left.listIterator();
        //it.hasNext();
        //int n=it.next();
        //clip.close();
        System.out.println(i);
        int n=i;
        while(it.hasNext())
        {
            int q=it.next();
            ResultSet r = s.executeQuery("select path_of_song,sid from songs where sid='" + q + "'");
            while (r.next()) {
                //System.out.println(r.getInt(2));
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

       /* for(int j=n+1;j<left.size();j++) {
            System.out.println(j);
            //clip.close();
            int next=left.get(j);
            ResultSet r = s.executeQuery("select path_of_song,sid from songs where sid='" + next + "'");
            while (r.next()) {
                System.out.println(r.getInt(2));
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
        }*/


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
        status = "play";
        //start the clip
        clip.start();


    }
    public Connection getcon() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c= DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","kriti@12345");
        return c;
    }
    void displaysongs() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Connection con=getcon();
        Statement s=con.createStatement();
        ResultSet r=s.executeQuery("select sid, name_of_song,path_of_song from songs");
        System.out.println("List of songs : ");
        while(r.next())
        {
            System.out.println("Song id" +" "+r.getInt (1) +" "+" Name of song"+" "+r.getString(2));
        }

        }

    void displaygenre() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("Enter the genre : 1) cinematic 2)jazz 3)hip-hop");
        int num= obj.nextInt();
        if(num==1)
        {
            System.out.println("List of songs where genre is cinematic :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,Genre,path_of_song from songs where genre='cinematic'");
            while(r.next()) {
                System.out.println(r.getInt(1) + "  " + r.getString(2) + "  " + r.getString(3));
            }

            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
            ResultSet r1=s.executeQuery("select path_of_song from songs where genre='cinematic' and sid='"+si+"'");

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
                Scanner obj=new Scanner(System.in);


                while (true)
                {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }
        else if(num==2)
        {
            System.out.println("List of songs where genre is jazz:");
            ResultSet r = s.executeQuery("select sid,name_of_song ,Genre from songs where genre='jazz'");
            //int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
               // k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
            //i=left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where genre='jazz' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }
        else if(num==3)
        {
            System.out.println("List of songs where genre is hip_hop :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,Genre from songs where genre='hip-hop'");
            int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
                k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
            //i=left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where genre='hip-hop' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
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
        }
    }


    void displayartist() throws SQLException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("Enter the artist name : 1)sid 2)taylor 3)john 4)jenny");
        int num= obj.nextInt();
        if(num==1)
        {
            System.out.println("List of songs where artist name is sid :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,artist_name from songs where artist_name='sid'");
            //int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
              //  k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
            //i= left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where artist_name='sid' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }
        if(num==2)
        {
            System.out.println("List of songs where artist name is taylor :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,artist_name from songs where artist_name='taylor'");
            //int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
              //  k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
           // i= left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where artist_name='taylor' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }
        if(num==3)
        {
            System.out.println("List of songs where artist name is john :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,artist_name from songs where artist_name='john'");

            //int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
                //k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
           // i= left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where artist_name='john' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }
        if(num==4)
        {
            System.out.println("List of songs where artist name is jenny :");
            ResultSet r=s.executeQuery("select sid,name_of_song ,artist_name from songs where artist_name='jenny'");
            //int k=0;
            while(r.next())
            {
                System.out.println(r.getInt(1)+"  "+r.getString(2)+"  "+r.getString(3));
              //  k=r.getInt(1);
                //left.add(k);
            }
            System.out.println("Enter the song id you want to play :");
            int si= obj.nextInt();
            sid=si;
            //i= left.indexOf(si);
            ResultSet r1=s.executeQuery("select path_of_song from songs where artist_name='jenny' and sid='"+si+"'");

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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }
        }

    }
    void displayname() throws SQLException, ClassNotFoundException, LineUnavailableException, IOException, UnsupportedAudioFileException {
        Connection con=getcon();
        Statement s=con.createStatement();
        /*songs son=new songs();
        son.displayname();*/
        System.out.println("Enter the song name");
        String num= obj.nextLine();
        //int k=0;
        ResultSet r=s.executeQuery("select sid,name_of_song ,artist_name,genre from songs where name_of_song='"+num+"'");
        while(r.next()) {
            System.out.println(r.getInt(1) + "  " + r.getString(2) + "  " + r.getString(3) + "  " + r.getString(4));
          //  k=r.getInt(1);
           // left.add(k);
            //i= left.indexOf(k);
        }
            ResultSet r1=s.executeQuery("select path_of_song ,sid from songs where name_of_song='"+num+"'");
            while(r1.next())
            {
                sid=r1.getInt(2);
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
                    int ch = obj.nextInt();
                    gotoChoice(ch);
                    if (ch == 4)
                        break;
                }
            }


    }
}

