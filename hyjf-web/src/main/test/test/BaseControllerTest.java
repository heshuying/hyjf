package test;  
  
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
  
@ContextConfiguration(locations = { "classpath:conf/spring-*.xml",  
        "classpath:conf/spring.xml" })  
@RunWith(SpringJUnit4ClassRunner.class)  
public class BaseControllerTest extends AbstractTransactionalJUnit4SpringContextTests {  
      
}  
