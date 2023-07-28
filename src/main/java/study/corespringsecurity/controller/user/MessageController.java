package study.corespringsecurity.controller.user;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

	@GetMapping("/messages")
	public String mypage() throws Exception {

		return "user/messages";
	}

	@PostMapping("/api/messages")
	@ResponseBody
	public String apiMessages() {
		return "messages ok";
	}

	@PostMapping("/api/message")
	@ResponseBody
	public ResponseEntity<String> apiMessage() {
		return ResponseEntity.ok("messages ok");
	}

}
