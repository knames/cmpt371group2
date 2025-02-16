package DataBase;

import ServerClientShared.ChannelIdentifier;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Connor
 */
public class Queries {

    /**
     * get a query used to get the dynamic form specifications number from the
     * database
     *
     * @param channelName the string name of the channel whose spec will be
     * retrieved
     * @param ownerName the channel's creator, for uniqueness
     * @return a query that will execute to retrieve the form spec number. this
     * can be combined with the ownerName to get the name of the spec file ex:
     * okenso.6 if given owner: okenso.
     */
    public static String getFormSpec(String channelName, String ownerName) {
        String sql = "SELECT ch_spec FROM channels WHERE ch_name = \""
                + channelName + "\" and ch_owner = \"" + ownerName + "\";";
        return sql;
    }

    /**
     * get a query to save in the database a filepath for a recently saved
     * picture
     *
     * @param channelName the name of the channel to save to
     * @param ownerName the name of the creator of the channel
     * @param postNumber the number of the post to be populated
     * @param fieldName the title of the picture field
     * @param filePath the file path to the picture
     * @return a query that can be executed to save the filepath to the database
     */
    public static String saveField(String channelName, String ownerName, int postNumber, String fieldName, String filePath) {
    	//TODO Needs testing.
        //update posts_okenso_pothole set p_field1 = "yo" where p_number=1; sample of SQL code.
        String sql = "update posts_" + ownerName + "_" + channelName
                + " set p_" + fieldName + " = \"" + filePath + "\" where p_number = " + postNumber + ";";
        return sql;
    }

    /**
     * returns a query which will insert a new row named after hte poster and
     * then grab the highest post number in a given
     * channel table
     *
     * @param channelName the channel name
     * @param ownername the owner of the channel
     * @param posterName the person posting this new incident
     * @return the sql query that will give the highest number in the post
     */
    public static String newRow(String channelName, String ownername, String posterName) {

        String sql = "Insert into posts_" + ownername + "_" + channelName + " (p_poster) values (\""
                + posterName + "\");";
        return sql;
    }
    
    /**
     * returns the next post number for a channel
     * @param channelName the name of the channel
     * @param ownername the channel's creator
     * @return 
     */
    public static String getNewPostNum(String channelName, String ownername)
    {
        String sql = "select p_number from posts_" + ownername + "_" + channelName + " order by 1 desc limit 1;";
        return sql;
    }
    
    
    /** creates a default post table
     @param ownername the name of the owner of the post
     @param tablename the name of the table.
     @param fields an array of column names for the fields. can take null of no fields.
     * 
     @return the sql for the table creation*/
    public static String createPosts(String ownername, String tablename, ArrayList<String> fields){
        String allfields = "";
        if (fields != null){
            for (String i : fields){
                allfields = allfields + "p_" + i + " varchar(100), ";
            }
        }
        String sql ="CREATE TABLE posts_" + ownername + "_" + tablename + " ( "
                + "p_poster varchar(25) NOT NULL, "
                + "p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "p_title varchar(100), "
                + "p_geotag varchar(100), "
                + allfields
                + "FOREIGN KEY (p_poster) REFERENCES users (u_username));";
        return sql;
    }
    
    /**
     * @param owner the owner to check for specs from
     * @return a string which will find the next number for this user's spec files
     */
    public static String nextSpecNum(String owner)
    {
        String sql = "select ch_spec from channels where ch_owner =\"" + owner + "\"order by 1 desc limit 1;";
        return sql;
    }
    
    public static String channelExists(String title, String owner)
    {
        String sql = "select count(*) from channels where ch_name = \"" + title + "\" and ch_owner = \""
		    		  + owner + "\";";
        return sql;
    }
    
    /**
     * query to make a new channel on the database
     * @param title the name of the channel
     * @param owner the creator of the channel
     * @param specNum the owners next spec number
     * @param isPublic whether the channel should be publicly visible
     * @param fieldNames the names of all non-standard fields
     * @return a string to be run by an sql statement
     */
    public static String makeChannel(String title, String owner, int specNum, boolean isPublic, ArrayList<String> fieldNames)
    {
        String sql = "insert into channels values (\"" + title + "\", \"" + owner + "\", " + specNum + ", " + isPublic +");";
        sql += createPosts(owner, title, fieldNames);
        return sql;
    }
    
    /**
     * a query to get a list of all channels in the system
     * @return a runnable sql query
     */
    public static String getAllChannels()
    {
        String sql = "SELECT ch_name, ch_owner from channels;";
        return sql;
    }
    
    public static String getSubscriptionIDs(String username)
    {
        String sql = "SELECT ch_fav_chname, ch_fav_chowner FROM channelfavs WHERE ch_fav_username = \""
                + username + "\";";
        return sql;
    }
    
//    /**  a main class to test some functions output
//     * @param args.*/
//    public static void main(String[] args)
//    {
//    	String[] test = {"pic", "video", "audio"};
//        System.out.println(createPosts("okenso", "toast", test));
//        
//    }
}

