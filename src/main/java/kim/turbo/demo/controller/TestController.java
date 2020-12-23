package kim.turbo.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试controller
 *
 * @author turbo
 * @email turbo-boom@outlook.com
 * @date 2020-12-22 15:58
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("index")
    public String index() {
        return "hello index";
    }

//    @Secured({"ROLE_sale", "ROLE_manager"})
    @GetMapping("update")
//    @PreAuthorize("hasAnyAuthority('admins')")
    @PostAuthorize("hasAnyAuthority('admins')")
    public String update() {
        System.out.println("update");
        return "hello update";
    }
}
