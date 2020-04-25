package test.htl.statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hyjf.web.htl.statistics.HtlStatisticsController;

import test.BaseControllerTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HtlStatisticsTest extends BaseControllerTest  {

	@Test
	public void testRegist() throws Exception {

		HtlStatisticsController htlStatisticsController = (HtlStatisticsController) this.applicationContext.getBean("htlStatisticsController");  
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String test= htlStatisticsController.searchTotalStatistics(request, response);  
		System.out.println(test);
	}
	
}
