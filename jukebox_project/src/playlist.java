import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class playlist
{
    Scanner obj=new Scanner(System.in);
    Long currentFrame;
    String status;
    Clip clip;
    AudioInputStream audioInputStream;
    String path;
    int sid;
    int i;
    playlist(int id)
    {
       this.i=id;
    }
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
            if(sid>=1001 && sid<=1007) {
                ResultSet r = s.executeQuery("select path_of_song,sid from songs where sid='" + sid + "'");
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
        else if(sid>=3001 && sid<=3006) {
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


    }
    void displayprevious() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.close();
        Connection con=getcon();
        Statement s=con.createStatement();
        sid=sid-1;
        if(sid>=1001 && sid<=1007) {
            ResultSet r = s.executeQuery("select path_of_song,sid from songs where sid='" + sid + "'");
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
        else if(sid>=3001 && sid<=3006) {
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
    public void displayplay() throws SQLException, ClassNotFoundException
    {
        Connection con=getcon();
        Statement s=con.createStatement();
        users u=new users();
        System.out.println("names of the available playlist ");
        //System.out.println(i);
        ResultSet r=s.executeQuery("select distinct pname from playlist where uid='"+i+"' ");
        while(r.next())
        {
            System.out.println(r.getString(1));
        }

    }
    public void displaysel() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("enter the name of the playlist you want to listen");
        String name=obj.next();
        ResultSet r1=s.executeQuery("select sid,eid from playlist where pname='"+name+"'");
        System.out.println("song id" +"  "+"episodeid");
        while(r1.next())
        {
            System.out.println(r1.getInt(1)+"   "+r1.getInt(2));
        }
        System.out.println("Enter 1)for songs in playlist and 2) for podcast in playlist");
        int nm= obj.nextInt();
        if(nm==1)
        {
            System.out.println("Enter the song id");
            int t= obj.nextInt();
            sid=t;
            ResultSet r=s.executeQuery("select songs.path_of_song from songs where sid='"+t+"'");
            while(r.next())
            {
                path=r.getString(1);
                File f=new File(path);
                AudioInputStream ai= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        ai
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
                        break;
                }
            }
        }
        if(nm==2)
        {
            System.out.println("Enter the episode id");
            int t= obj.nextInt();
            sid=t;
            ResultSet r=s.executeQuery("select paths from episode where eid='"+t+"'");
            while(r.next())
            {
                path=r.getString(1);
                File f=new File(path);
                AudioInputStream ai= AudioSystem.getAudioInputStream(f);
                Clip clip=AudioSystem.getClip();
                this.clip=clip;
                // clip.open(a);
                this.status="play";
                this.clip.open(
                        ai
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
                        break;
                }
            }
        }

    }
    public void displayadd() throws SQLException, ClassNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("1) to add a song ,  2) to add podacst ");
        int a=obj.nextInt();
        if(a==1)
        {
            System.out.println("Enter the name of the playlist in which you want to add new song or podcast");
            String name = obj.next();
            ResultSet r=s.executeQuery("select sid from playlist where pname='"+name+"'");
            //System.out.println("list of songs present in the playlist");
            while(r.next())
            {
                System.out.println(r.getInt(1));
            }
            System.out.println("list of song is :");
            songs sg=new songs();
            sg.displaysongs();
            System.out.println("Enter the song id which you want to add to playlist");
             int sn = obj.nextInt();
             ResultSet r1=s.executeQuery("select sid from playlist where sid='"+sn+"'and pname='"+name+"' ");
             int q = 0;
             while(r1.next()) {
                  q = r1.getInt(1);
             }
             if(sn==q)
             {
                 System.out.println("Song is already present in the playlist");
             }
             else {
                 users u=new users();
                 int uid = u.uid;
                 //System.out.println("Enter your user id ");
                 //int n = obj.nextInt();
                 s.executeUpdate("insert into playlist(pname,uid,sid,eid) values('" + name + "','" + uid + "','" + sn + "','null')");
                 System.out.println("inserted successfully");
             }

        }
        if(a==2)
        {
            System.out.println("Enter the name of the playlist in which you want to add new  podcast");
            String name = obj.next();
            ResultSet r=s.executeQuery("select eid from playlist where pname='"+name+"'");
            while(r.next())
            {
                System.out.println(r.getString(1));
            }
            //System.out.println("list of song is :");
            episodes eg=new episodes();
            eg.displayepi();
            System.out.println("Enter the episode id which you want to add to playlist");
            String sn = obj.next();
            ResultSet r1=s.executeQuery("select eid from playlist where eid='"+sn+"'and pname='"+name+"' ");
            String q = "";
            while(r1.next()) {
                q = r1.getString(1);
            }
            if(sn.equals(q))
            {
                System.out.println("Episode is already present in the playlist");
            }
            else {
                users u = new users();
                int n = u.uid;
                s.executeUpdate("insert into playlist(pname,uid,sid,eid) values('" + name + "','" + n + "',0,'" + sn + "')");
                System.out.println("inserted to playlist successfully");
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
    public void displaynew() throws SQLException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException, IOException {
        Connection con=getcon();
        Statement s=con.createStatement();
        System.out.println("Enter the new playlist name : ");
        String name =obj.next();
        ResultSet r=s.executeQuery("select distinct pname from playlist ");

        while(r.next()) {
            System.out.println( r.getString(1));
        }
                System.out.println("1)to add a song and 2)to add a podcast");
                int ch = obj.nextInt();
                if (ch == 1) {
                    songs sg = new songs();
                    sg.displaysongs();
                    System.out.println("Enter the song id which you want to add to playlist");
                    int si = obj.nextInt();
                    users u = new users();
                    int n = u.uid;
                    s.executeUpdate("insert into playlist(pname,uid,sid) values('" + name + "','" + n + "','" + si + "')");
                    System.out.println("inserted to playlist successfully");
                } else if (ch == 2) {
                    episodes eg = new episodes();
                    eg.displayepi();
                    System.out.println("Enter the episode id which you want to add to playlist");
                    String sn = obj.next();
                    users u = new users();
                    int n = u.uid;
                    s.executeUpdate("insert into playlist(pname,uid,eid) values('" + name + "','" + n + "','" + sn + "')");
                    System.out.println("inserted to playlist successfully");
                }



    }
    public void displaydelete() throws SQLException, ClassNotFoundException {
        Connection con = getcon();
        Statement s = con.createStatement();
        System.out.println("1) to delete a song ,  2) to delete a podcast  , 3) to delete a playlist");
        int a = obj.nextInt();
        if (a == 1) {
            System.out.println("Enter the name of the playlist in which you want to delete a song ");
            String name = obj.next();
            int co=0;
                    ResultSet r1 = s.executeQuery("select sid  from playlist where pname='" + name + "' ");
                    while (r1.next()) {
                        System.out.println(r1.getInt(1));
                    }
            ResultSet r2 = s.executeQuery("select distinct count(sid) from playlist where pname='" + name + "' ");
                    while(r2.next())
                    {
                        co=r2.getInt(1);
                    }
                    if(co==0)
                    {
                        System.out.println("playlist is empty");
                    }
                    else {
                        System.out.println("Enter the song id which you want to delete to playlist");
                        int sn = obj.nextInt();

                        s.executeUpdate("delete from playlist where sid='" + sn + "' and  pname='" + name + "'");
                        System.out.println("Deleted successfully");
                    }
                }


        if(a==2)
        {
            System.out.println("Enter the name of the playlist in which you want to delete the podcast");
            String name = obj.next();
            int co=0;
            ResultSet r1 = s.executeQuery("select eid  from playlist where pname='" + name + "' ");
            while (r1.next()) {
                System.out.println(r1.getInt(1));
            }
            ResultSet r2 = s.executeQuery("select distinct count(eid) from playlist where pname='" + name + "' ");
            while(r2.next())
            {
                co=r2.getInt(1);
            }
            if(co==0)
            {
                System.out.println("playlist is empty");
            }
            else {
                System.out.println("Enter the episode id which you want to delete  to playlist");
                String sn = obj.next();
                s.executeUpdate("delete from playlist where eid='" + sn + "' and  pname='" + name + "'");
                System.out.println("Deleted successfully");
            }
        }
        if(a==3)
        {
            System.out.println("Enter the name of the playlist in which you want to delete :");
            String name = obj.next();
            ResultSet r2 = s.executeQuery("select  distinct pname from playlist where pname='" + name + "' ");
            String pn="";
            while(r2.next())
            {
                pn=r2.getString(1);
            }
            if(pn.equals(name)) {
                s.executeUpdate("delete from playlist where pname='" + name + "'");
                System.out.println("Deleted successfully");
            }
            else {
                System.out.println("playlist does not exist");
            }
        }

    }
}
