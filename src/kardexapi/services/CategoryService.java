package kardexapi.services;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kardexapi.entities.Category;


// Servicio para gestionar las categor�as
@Path("/category")
@Consumes(value=MediaType.APPLICATION_JSON)
@Produces(value=MediaType.APPLICATION_JSON)
public class CategoryService {
	
	// Se obtiene el Entity_manager_factory creado en el archivo de persistencia
	private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
			.createEntityManagerFactory("AppKardex");
	
	// M�todo para obtener todas las categor�as. Recibe 2 par�metros para realizar la paginaci�n
	@GET
    public Response getCategories(@QueryParam("limit") int limit, @QueryParam("offset") int offset) {
		
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		
		try {
			// Se obtienen las categor�as con la sentencia definida en la entidad
			TypedQuery<Category> query = em.createNamedQuery("Category.findAll", Category.class);
			
			// Se obtiene la cantidad de categor�as de la bd
			int count = ((Number)em.createNamedQuery("Category.findCount").getSingleResult()).intValue();
			
			// Si alguno de los par�metros de la paginaci�n no se recibe, darles un valor por defecto
			if(offset == 0 || offset < 0) offset = 0;
			if(limit == 0 || limit < 0) limit = 5;
			
			// Se realiza la paginaci�n
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			
			// Se crea un HashMap para guardar todos los datos que se enviar�n en la respuesta
			HashMap<String, Object> map = new HashMap<>();
			
			// Se obtienen las categor�as paginadas del query
			List<Category> cats = query.getResultList();
			
			// Se definen las variables para llevar el control de la paginaci�n
			String next = "null";
			String previous = "null";
			
			// Si la consulta no es vac�a, construir la url para la siguiente p�gina
			if(!cats.isEmpty()) {
				int nextData = offset + limit;
				next = "/category/?limit=" + limit + "&offset=" + nextData;
			}
			
			// Si el offset no es nulo, construir la url para la p�gina previa
			if(offset != 0) {
				int prevData = offset - limit;
				previous = "/category/?limit=" + limit + "&offset=" + prevData;
			}
			
			// Se colocan los resultados que se enviar�n en la respuesta
			map.put("previous", previous);
			map.put("next", next);
			map.put("count", count);
			map.put("results", cats);
			
			// Se env�a la respuesta
			return Response.ok(map,MediaType.APPLICATION_JSON).build();   
		} catch (NoResultException ex) {
			
			// En caso de error, enviar en la respuesta el error
			ex.printStackTrace();
			HashMap<String, Object> map = new HashMap<>();
			map.put("error", ex.getMessage());
			return Response.serverError().entity(map).build();
		} finally {
			em.close();
		}
		
       
    }  
	
	// M�todo para guardar una categor�as. 
	// Recibe como par�metro una entidad con los datos de la categor�a a guardar
	@POST
	public Response createCategory(Category cat) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		try {
			et = em.getTransaction();
			et.begin();
			Category categ = new Category();
			categ.setName(cat.getName());
			categ.setStatus(true);
			em.persist(categ);
			et.commit();
			
			return Response.ok(categ).build();
		} 
		catch(Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
			HashMap<String, Object> map = new HashMap<>();
			map.put("error", ex.getMessage());
			return Response.serverError().entity(map).build();
		}
		finally {
			em.close();
		}
	}
	
	// M�todo para actualizar una categor�as. 
	// Recibe como par�metro una entidad con los datos de la categor�a a actualizar
	@PUT
	public Response updateCategory(Category cat) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		Category categ = null;
		try {
			et = em.getTransaction();
			et.begin();
			categ = em.find(Category.class, cat.getId());
			if (categ != null) {
				categ.setName(cat.getName());
				categ.setStatus(true);
				em.persist(categ);
				et.commit();
				
				return Response.ok(categ).build();
			} else {
				HashMap<String, Object> map = new HashMap<>();
				String err = "No se encontr� la categor�a con el id " + cat.getId();
				map.put("error", err);
				return Response.status(Response.Status.NOT_FOUND).entity(map).build();
			}
			
		} 
		catch(Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
			HashMap<String, Object> map = new HashMap<>();
			map.put("error", ex.getMessage());
			return Response.serverError().entity(map).build();
		}
		finally {
			em.close();
		}
	}
	
	// M�todo para eliminar una categor�as. 
	// Recibe como par�metro una entidad que contenga el id de la categor�a a eliminar
	@DELETE
	public Response deleteCategory(Category cat) {
		EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
		EntityTransaction et = null;
		Category categ = null;
		try {
			et = em.getTransaction();
			et.begin();
			categ = em.find(Category.class, cat.getId());
			if (categ != null) {
				categ.setStatus(false);
				em.persist(categ);
				et.commit();
				
				return Response.ok(categ).build();
			} else {
				HashMap<String, Object> map = new HashMap<>();
				String err = "No se encontr� la categor�a con el id " + cat.getId();
				map.put("error", err);
				return Response.status(Response.Status.NOT_FOUND).entity(map).build();
			}
			
		} 
		catch(Exception ex) {
			if(et != null) {
				et.rollback();
			}
			ex.printStackTrace();
			HashMap<String, Object> map = new HashMap<>();
			map.put("error", ex.getMessage());
			return Response.serverError().entity(map).build();
		}
		finally {
			em.close();
		}
	}
	
}
