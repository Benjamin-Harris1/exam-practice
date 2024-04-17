package kea.exercise.examframework.api;

import kea.exercise.examframework.dto.VanDTO;
import kea.exercise.examframework.service.van.VanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/van")
public class VanController {

    private final VanService vanService;

    public VanController(VanService vanService) {
        this.vanService = vanService;
    }

    @GetMapping
    public ResponseEntity<List<VanDTO>> getAllVans(){
        List<VanDTO> vans = vanService.findAll();
        return ResponseEntity.ok(vans);
    }

}
