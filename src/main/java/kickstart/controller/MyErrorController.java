package kickstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyErrorController {

  @GetMapping("/testerror")
  public void handleRequest() {
      //throw new RuntimeException("test exception");
  }
}