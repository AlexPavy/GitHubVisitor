package alexpavy.applications;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class MVCController {

    @GetMapping
    public Integer testMethod() {
        return 1;
    }

}
