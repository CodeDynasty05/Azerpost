package com.azerpost.app.controller;

import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.dto.ReturnAddRequest;
import com.azerpost.app.service.ReturnService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/returns")
@Validated
public class ReturnController{
    private final ReturnService returnService;

    @PostMapping("/initiate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR','ROLE_USER')")
    public ReturnEntity initiateReturn(@Valid @RequestBody ReturnAddRequest request){
        return returnService.initiateReturn(request);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ReturnEntity approveReturn(@PathVariable @Positive Long id){
        return returnService.approveReturn(id);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ReturnEntity completeReturn(@PathVariable @Positive Long id){
        return returnService.completeReturn(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public ReturnEntity getReturn(@PathVariable @Positive Long id){
        return returnService.getReturn(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
    public List<ReturnEntity> getAllReturns(){
        return returnService.getAllReturns();
    }
}
