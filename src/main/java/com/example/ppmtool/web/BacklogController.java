package com.example.ppmtool.controllers;

import com.example.ppmtool.entitys.ProjectTask;
import com.example.ppmtool.services.MapValidationErrorService;
import com.example.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/backlog")
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/{backlog_id")
        public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
            BindingResult result, @PathVariable String backlog_id) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService((result));
        if(errorMap != null ) {
            return errorMap;
        }
        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }


}
