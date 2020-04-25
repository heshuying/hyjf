package test.user.openaccount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.web.user.openaccount.OpenAccountController;

import test.BaseControllerTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class OpenAccountTest extends BaseControllerTest  {

	@Test
	public void testRegist() throws Exception {

		OpenAccountController loginController = (OpenAccountController) this.applicationContext.getBean("openAccountController");  
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("userName", "wangkun");
		request.addParameter("mobile", "18660250807");
		request.addParameter("userId", "102725");
		request.setMethod("POST");  

		ModelAndView test= loginController.initOpenAccount(request, response);  
		System.out.println(test);
	}
	
}
