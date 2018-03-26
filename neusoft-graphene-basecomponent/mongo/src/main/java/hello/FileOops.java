package hello;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Service
public class FileOops {

	@Autowired
	private MongoDbFactory mongodbfactory;

	private GridFS gridFS;

	@PostConstruct
	public void init() {
		gridFS = new GridFS(mongodbfactory.getDb());
	}

	/**
	 * 用给出的id，保存文件，透明处理已存在的情况 id 可以是string，long，int，org.bson.types.ObjectId 类型
	 * 
	 * @param in
	 * @param id
	 */
	public GridFSInputFile save(InputStream in, Object id, String fileName) {
		DBObject query = new BasicDBObject("_id", id);
		GridFSDBFile gridFSDBFile = gridFS.findOne(query);
		if (gridFSDBFile == null) {
			GridFSInputFile gridFSInputFile = gridFS.createFile(in);
			gridFSInputFile.setId(id);
			gridFSInputFile.setFilename(fileName);
			DBObject metaData = new BasicDBObject();
			metaData.put("extra1", "anything 1");
		    gridFSInputFile.setMetaData(metaData);  

			gridFSInputFile.save();
			return gridFSInputFile;
		} else {
//			gridFS.remove(query);
			System.out.println("fileName exits in mongo DB-----"+fileName);
			return null;
		}
	}

	/**
	 * 据id返回文件
	 * 
	 * @param id
	 * @return
	 */
	public GridFSDBFile getById(Object id) {
		DBObject query = new BasicDBObject("_id", id);
		GridFSDBFile gridFSDBFile = gridFS.findOne(query);
		return gridFSDBFile;
	}

	/**
	 * 据文件名返回文件，只返回第一个
	 * 
	 * @param fileName
	 * @return
	 */
	public GridFSDBFile getByFileName(String fileName) {
		DBObject query = new BasicDBObject("filename", fileName);
		GridFSDBFile gridFSDBFile = gridFS.findOne(query);
		return gridFSDBFile;
	}

}
