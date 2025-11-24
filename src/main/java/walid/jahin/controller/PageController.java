package walid.jahin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // templates/index.html
    }

    @GetMapping("/artist")
    public String artist() {
        return "artist"; // templates/artist.html
    }

    @GetMapping("/artshop")
    public String artshop() {
        return "artshop"; // templates/artshop.html
    }

    @GetMapping("/artwork")
    public String artwork() {
        return "artwork"; // templates/artwork.html
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact"; // templates/contact.html
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy"; // templates/privacy.html
    }

    @GetMapping("/purchase")
    public String purchase() {
        return "purchase"; // templates/purchase.html
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms"; // templates/terms.html
    }

    @GetMapping("/solo")
    public String solo() {
        return "solo"; // templates/soloart.html
    }
}
