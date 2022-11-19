import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.sql.*;
public class users
{
    public int uid;
    /*int uid;
        String username;
        String password;
        String email;*/
   /* public users (int uid,String username,String password,String email)
    {
        this.uid=uid;
        this.username=username;
        this.password=password;
        this.email=email;
    }*/
    Scanner obj=new Scanner(System.in);
    int id;
    public String checkuser() throws ClassNotFoundException, SQLException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        String result="";
        System.out.println("   1) REGISTERD USER                               2) NEW USER ");
        Scanner obj=new Scanner(System.in);
        System.out.println("Enter your choice : ");
        int n=obj.nextInt();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c= DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","kriti@12345");
        Statement s=c.createStatement();
        String pas="";
        //ResultSet r= s.executeQuery("select * from users");
            if(n==1)
            {
                System.out.println("Enter username");
                String uname = obj.next();
                System.out.println("Enter password");
                String newpass = obj.next();
                ResultSet r= s.executeQuery("select username,passwords ,uid from users where username='"+ uname+"' and passwords='"+newpass+"'");
                while (r.next())
                {
                    //String usn = r.getString(1);
                     pas = r.getString(2);
                     uid=r.getInt(3);
                    // if (uname.equals(usn))
                    //{
                }
                id=uid;
                        if (newpass.equals(pas))
                        {
                            System.out.println("************ Logged in **************** ");

                            menu();
                            result = "***************************************************************************";

                        } else
                        {
                            System.out.println("*********** Wrong username or password ***********");
                            checkuser();
                        }
                    //}


            }
            else if(n==2)
            {
                System.out.println("Enter username : ");
                String name = obj.next();
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection c1= DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","kriti@12345");
                Statement s1=c1.createStatement();
                ResultSet r1=s1.executeQuery("select username from users where username='"+name+"'");
                String na="";
                while(r1.next())
                {
                    na=r1.getString(1);
                }
                if(na.equals(name))
                {
                    System.out.println("username already exists");
                    checkuser();
                }
                else {
                System.out.println("Enter email id : ");
                String emails = obj.next();
                System.out.println("Enter password : ");
                System.out.println("password should be of 8 characters ");
                String pass1 = obj.next();
                int len = pass1.length();
                if (len == 8) {
                    System.out.println("password is of 8 characters");
                    System.out.println("Re-enter password : ");
                    String pass2 = obj.next();
                    if (pass1.equals(pass2)) {
                        System.out.println("password accepted");
                        String query="insert into users(username,passwords,email) values('"+name+"','"+pass2+"','"+emails+"')";
                        s.executeUpdate(query);
                        System.out.println("account created");

                        menu();
                } else {
                    System.out.println("password should be of 8 characters");
                    checkuser();
                }
                /*System.out.println("Re-enter password : ");
                String pass2 = obj.next();
                if (pass1.equals(pass2)) {
                    System.out.println("password accepted");
                    String query="insert into users(username,passwords,email) values('"+name+"','"+pass2+"','"+emails+"')";
                    s.executeUpdate(query);
                    System.out.println("account created");

                    menu();*/
                        result = "*************************************************************";

                }
                else {
                    //System.out.println(" passwords are not same please check !!!");
                    System.out.println(" try again");
                    checkuser();
                }

            }}

        return result;
    }
    public void menu() throws UnsupportedAudioFileException, SQLException, LineUnavailableException, IOException, ClassNotFoundException {
        System.out.println("Enter 1) for songs and 2) for podcast 3) for playlist 4) exit");
        int num=obj.nextInt();
        if(num==1)
        {
            System.out.println("*********************** Welcome to Songs Page **********************");
            songs so=new songs();
            //so.displaysongs();
            System.out.println("How you want to search the songs");
            System.out.println("1) display by genre 2) display by artist name 3) display by song name 4) go back to menu 5) to exit ");
            int ch=obj.nextInt();
            if(ch==1)
            {
                so.displaygenre();
            }
            else if(ch==2)
            {
                so.displayartist();
            }
            else if(ch==3)
            {
                so.displaysongs();
                so.displayname();
            }
            else if(ch==4)
            {
               menu();
            }
            else if(ch==5)
            {
                System.exit(0);
            }
        }
        else if(num==2)
        {
            System.out.println("************************* Welcome to Podcast page *******************");
            podcast po=new podcast();
            po.display();
            episodes ep=new episodes();
            ep.displayepi();
            System.out.println("How do you want search the podcast");
            System.out.println("1)display episodes by artist name 2)display by episode name 3) menu 4)exit");
            int ch= obj.nextInt();
            /*if(ch==1)
            {
                po.displayid();
            }*/
            if(ch==1)
            {
                ep.displayartist();
            }
            else if(ch==2)
            {
                ep.displayname();
            }
          else if(ch==3)
          {
            menu();
           }
          else if(ch==4)
            {
                System.exit(0);
            }
        }
        else if(num==3)
        {
            System.out.println("************************* Welcome to Playlist page *******************");
            playlist pl=new playlist(id);
            pl.displayplay();
            System.out.println("Enter 1) for adding new songs in existing playlist or 2) for creating a new playlist or 3) delete from playlist or 4) to play a song or 5) exit or 6) menu");
            int ch= obj.nextInt();
            if(ch==1)
            {
                pl.displayadd();
            }
            else if(ch==2)
            {
                pl.displaynew();
            }
            else if(ch==3)
            {
                pl.displaydelete();
            }
            else if(ch==4)
            {
                pl.displaysel();
            }
            else if(ch==5)
            {
                System.exit(0);
            }
            else if(ch==6)
            {
                menu();
            }
        }
        else if(num==4)
        {
            System.exit(0);
        }
        System.out.println("Enter 1 to go back to menu page : ");
        int b= obj.nextInt();
        if(b==1)
        {
            menu();
        }
        else
        {
            System.exit(0);
        }
    }

}
