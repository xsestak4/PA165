package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;
        
        private Category electro = new Category();
        private Category kitchen = new Category();
        private Product flashlight= new Product();
        private Product kitchenRobot= new Product();
        private Product plate = new Product();
        
        @BeforeClass
        public void create(){
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            electro.setName("Electro");
            kitchen.setName("Kitchen");
            
            em.persist(electro);
            em.persist(kitchen);
            
            flashlight.setName("Flashlight");
            flashlight.addCategory(electro);
            
            kitchenRobot.setName("Kitchen robot");
            kitchenRobot.addCategory(electro);
            kitchenRobot.addCategory(kitchen);

            plate.setName("Plate");
            plate.addCategory(kitchen);
            
            em.persist(flashlight);
            em.persist(kitchenRobot);
            em.persist(plate);
            
            em.getTransaction().commit();
            em.close();
        }
        
        @Test
	public void testFlashlight(){
		EntityManager em = emf.createEntityManager();
		
		Product tstProduct = em.find(Product.class, flashlight.getId());
		Assert.assertEquals(tstProduct.getCategories().size(), 1);
		Assert.assertEquals(tstProduct.getCategories().iterator().next().getName(), "Electro");
		
		em.close();
        }
        
        @Test
	public void testKitchenRobot(){
		EntityManager em = emf.createEntityManager();
		
		Product tstProduct = em.find(Product.class, flashlight.getId());
		Assert.assertEquals(tstProduct.getCategories().size(), 1);
		Assert.assertEquals(tstProduct.getCategories().iterator().next().getName(), "Electro");
		
		em.close();
        
        }@Test
	public void testPlate(){
		EntityManager em = emf.createEntityManager();
		
		Product tstProduct = em.find(Product.class, kitchenRobot.getId());
		Assert.assertEquals(tstProduct.getCategories().size(), 1);
		Assert.assertEquals(tstProduct.getCategories().iterator().next().getName(), "Kitchen");
                
		em.close();
        }
        
        @Test
	public void testCategoryKitchen(){
		EntityManager em = emf.createEntityManager();
		
		Category tstCategory = em.find(Category.class, kitchen.getId());
		Assert.assertEquals(tstCategory.getProducts().size(), 2);
		assertContainsProductWithName(tstCategory.getProducts(), "Kitchen Robot");
		assertContainsProductWithName(tstCategory.getProducts(), "Plate");
		
		em.close();
	}	

	@Test
	public void testCategoryElectro(){
		EntityManager em = emf.createEntityManager();
		
		Category tstCategory= em.find(Category.class, electro.getId());
		Assert.assertEquals(tstCategory.getProducts().size(), 2);
		assertContainsProductWithName(tstCategory.getProducts(), "Flashlight");
		assertContainsProductWithName(tstCategory.getProducts(), "Kitchen Robot");
		
		em.close();
} 
        
        
        

	
	private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	
}
