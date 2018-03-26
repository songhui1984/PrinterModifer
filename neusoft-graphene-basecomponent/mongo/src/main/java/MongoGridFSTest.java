import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.net.UnknownHostException;  
  
import com.mongodb.BasicDBObject;  
import com.mongodb.DB;  
import com.mongodb.DBObject;  
import com.mongodb.MongoClient;  
import com.mongodb.gridfs.GridFS;  
import com.mongodb.gridfs.GridFSDBFile;  
import com.mongodb.gridfs.GridFSInputFile;  
  
public class MongoGridFSTest {  
      
    private static MongoClient mongoClient = null;  
    private static DB db = null;  
    private static GridFS gridFS = null;  
   
    private static String dbName = "gridfs";  
  
    public static void main(String[] args) throws UnknownHostException, Exception {  
        mongoClient = new MongoClient("10.253.5.101", 27017);  
        db = mongoClient.getDB(dbName);  
        gridFS = new GridFS(db);  
          
        String file = "src/main/resources/cover.jpg";  
        
        if(!new File(file).exists()){  
            System.out.println("file is not exists.");  
        }  
        String fileName = "cover.jpg";  
        
        //把文件保存到gridfs中，并以文件的md5值为id  
        save(new FileInputStream(file), fileName,fileName);  
        //据文件名从gridfs中读取到文件  
           
        GridFSDBFile gridFSDBFile = getByFileName(fileName);  
        
        if(gridFSDBFile != null){  
            System.out.println("filename:" + gridFSDBFile.getFilename());  
            System.out.println("md5:" + gridFSDBFile.getMD5());  
            System.out.println("length:" + gridFSDBFile.getLength());  
            System.out.println("uploadDate:" + gridFSDBFile.getUploadDate());  
            System.out.println("--------------------------------------");  
            gridFSDBFile.writeTo(new FileOutputStream("src/main/resources/20150315172324_out.png"));  
        }else{  
            System.out.println("can not get file by name:" + fileName);  
        }  
    }  
      
    /** 
     * 用给出的id，保存文件，透明处理已存在的情况 
     * id 可以是string，long，int，org.bson.types.ObjectId 类型 
     * @param in 
     * @param id 
     */  
    public static void save(InputStream in, Object id,String fileName){  
        DBObject query  = new BasicDBObject("_id", id);  
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);  
        if(gridFSDBFile == null){  
             GridFSInputFile gridFSInputFile = gridFS.createFile(in);  
             gridFSInputFile.setId(id);  
             gridFSInputFile.setFilename(fileName);  
             //gridFSInputFile.setChunkSize();  
             //gridFSInputFile.setContentType();  
             //gridFSInputFile.setMetaData();  
             gridFSInputFile.save();  
        }  
    }  
      
    /** 
     * 据id返回文件 
     * @param id 
     * @return 
     */  
    public static GridFSDBFile getById(Object id){  
        DBObject query  = new BasicDBObject("_id", id);  
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);  
        return gridFSDBFile;  
    }  
      
    /** 
     * 据文件名返回文件，只返回第一个 
     * @param fileName 
     * @return 
     */  
    public static GridFSDBFile getByFileName(String fileName){  
        DBObject query  = new BasicDBObject("filename", fileName);  
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);  
        return gridFSDBFile;  
    }  
}  