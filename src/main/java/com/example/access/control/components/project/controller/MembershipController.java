package com.example.access.control.components.project.controller;

import com.example.access.control.components.project.domain.Membership;
import com.example.access.control.components.project.dto.MembershipDto;
import com.example.access.control.components.project.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/membership")
public class MembershipController {

    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Membership begin(@RequestBody MembershipDto membershipDto) {

        return membershipService.begin(membershipDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void complete(@RequestBody MembershipDto membershipDto) {

        membershipService.complete(membershipDto);
    }
}
