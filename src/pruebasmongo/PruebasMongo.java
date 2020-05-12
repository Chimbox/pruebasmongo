package pruebasmongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.bson.Document;

/**
 *
 * @author Invitado
 */
public class PruebasMongo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase database = mongo.getDatabase("restaurants");

        //database.createCollection("establecimientos");
        MongoCollection<Document> collection = database.getCollection("establecimientos");

        collection.drop();

        Document document1 = new Document()
                .append("_id", 1)
                .append("name", "Sushilito")
                .append("address", "Calle Guerrero")
                .append("category", Arrays.asList("Rollos", "Bebidas"))
                .append("rating", 5);

        Document document2 = new Document()
                .append("_id", 2)
                .append("name", "Hot Dogs El Chapo")
                .append("address", "Villas del Rey")
                .append("category", Arrays.asList("Hotdogs", "Hamburguesas", "Salchipapas", "Bebidas"))
                .append("rating", 3);

        Document document3 = new Document()
                .append("_id", 3)
                .append("name", "Bulli Burger")
                .append("address", "Avenida Nainari")
                .append("category", Arrays.asList("Hamburguesas", "Pizzas", "Postres"))
                .append("rating", 4.5);

        Document document4 = new Document()
                .append("_id", 4)
                .append("name", "Lockers")
                .append("address", "Avenida Nainari")
                .append("category", Arrays.asList("Entradas", "Hamburguesas", "Pizzas", "Postres"))
                .append("rating", 4.3);
        
        Document document5 = new Document()
                .append("_id", 5)
                .append("name", "IUME Sushi Express")
                .append("address", "Antonio Caso")
                .append("category", Arrays.asList("Rollos", "Bebidas"))
                .append("rating", 2);
        
        collection.insertMany(Arrays.asList(document1, document2, document3, document4, document5));

        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }

        FindIterable<Document> documents = collection.find(
                Filters.gt("rating", 4)
        );

        System.out.println("\nCon rating>4");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
        
        documents = collection.find(
                Filters.eq("category", "Pizzas")
        );

        System.out.println("\nCon categoría de pizzas");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
        
        documents = collection.find(
                Filters.regex("name", Pattern.compile("sushi", Pattern.CASE_INSENSITIVE))
        );

        System.out.println("\nCon 'sushi' en su nombre");
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
        
        

        System.out.println("\nSe agrega 'Postres' como otra categoría de Sushilito");
        
        UpdateResult updateResult=collection.updateOne(
                Filters.eq("name", "Sushilito"),
                Updates.addToSet("category", "Postres"));

        System.out.println("Se actualizaron: "+updateResult.getModifiedCount());
        
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }

        
       DeleteResult deleteResult = collection.deleteOne(Filters.eq("_id", 1));
       
        System.out.println("\nSe eliminaron: "+deleteResult.getDeletedCount());
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
        
        deleteResult = collection.deleteMany(
                Filters.lte("rating", 3)
        );
       
        System.out.println("\nSe eliminaron: "+deleteResult.getDeletedCount());
        for (Document doc : collection.find()) {
            System.out.println(doc.toJson());
        }
    }
}
