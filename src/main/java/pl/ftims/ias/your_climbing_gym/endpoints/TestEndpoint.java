package pl.ftims.ias.your_climbing_gym.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpoint {
	
	@RequestMapping("/hellouser")
	public String getUser()
	{
		return "Hello User";
	}
	
	@RequestMapping("/helloadmin")
	public String getAdmin()
	{
		return "Hello Admin";
	}

}
