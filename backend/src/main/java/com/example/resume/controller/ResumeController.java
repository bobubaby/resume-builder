package com.example.resume.controller;

import com.example.resume.common.Result;
import com.example.resume.common.UserContext;
import com.example.resume.entity.Resume;
import com.example.resume.entity.ResumeSection;
import com.example.resume.service.ResumeService;
import com.example.resume.vo.ResumeDetailVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    @GetMapping("/list")
    public Result<List<Resume>> list() {
        return Result.ok(resumeService.list(UserContext.getUserId()));
    }

    @PostMapping
    public Result<Long> create(@RequestBody Resume body) {
        return Result.ok(resumeService.create(UserContext.getUserId(), body.getTitle(), body.getTheme()));
    }

    @GetMapping("/{id}")
    public Result<ResumeDetailVO> detail(@PathVariable Long id) {
        return Result.ok(resumeService.detail(id, UserContext.getUserId()));
    }

    @PutMapping("/{id}")
    public Result<Void> updateMeta(@PathVariable Long id, @RequestBody Resume body) {
        resumeService.updateMeta(id, UserContext.getUserId(), body.getTitle(), body.getTheme());
        return Result.ok();
    }

    @PutMapping("/{id}/sections")
    public Result<Void> saveSections(@PathVariable Long id, @RequestBody List<ResumeSection> sections) {
        resumeService.saveSections(id, UserContext.getUserId(), sections);
        return Result.ok();
    }

    @PostMapping("/{id}/copy")
    public Result<Long> copy(@PathVariable Long id) {
        return Result.ok(resumeService.copy(id, UserContext.getUserId()));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        resumeService.delete(id, UserContext.getUserId());
        return Result.ok();
    }
}
