package hello;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface XmlRepository extends MongoRepository<XmlTemplate, String>{
	
    public XmlTemplate findByName(String name);
    
    public XmlTemplate findByNameAndFileName(String name, String fileName);

}
