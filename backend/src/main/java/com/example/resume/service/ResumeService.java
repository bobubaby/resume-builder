package com.example.resume.service;

import com.example.resume.common.BizException;
import com.example.resume.entity.Resume;
import com.example.resume.entity.ResumeSection;
import com.example.resume.mapper.ResumeMapper;
import com.example.resume.mapper.ResumeSectionMapper;
import com.example.resume.vo.ResumeDetailVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResumeService {

    @Resource
    private ResumeMapper resumeMapper;
    @Resource
    private ResumeSectionMapper sectionMapper;

    public List<Resume> list(Long userId) {
        return resumeMapper.selectByUserId(userId);
    }

    public Long create(Long userId, String title, String theme) {
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(title == null || title.isEmpty() ? "未命名简历" : title);
        resume.setTheme(theme == null || theme.isEmpty() ? "default" : theme);
        resumeMapper.insert(resume);
        addDefaultSections(resume.getId());
        return resume.getId();
    }

    private void addDefaultSections(Long resumeId) {
        String[][] defaults = {
                {"basic", "基本信息"},
                {"education", "教育经历"},
                {"experience", "工作经历"},
                {"skill", "技能特长"},
        };
        for (int i = 0; i < defaults.length; i++) {
            ResumeSection s = new ResumeSection();
            s.setResumeId(resumeId);
            s.setType(defaults[i][0]);
            s.setTitle(defaults[i][1]);
            s.setContent("{}");
            s.setSortOrder(i);
            s.setVisible(1);
            sectionMapper.insert(s);
        }
    }

    public ResumeDetailVO detail(Long id, Long userId) {
        Resume resume = checkOwner(id, userId);
        ResumeDetailVO vo = new ResumeDetailVO();
        vo.setResume(resume);
        vo.setSections(sectionMapper.selectByResumeId(id));
        return vo;
    }

    public void updateMeta(Long id, Long userId, String title, String theme) {
        checkOwner(id, userId);
        Resume resume = new Resume();
        resume.setId(id);
        resume.setTitle(title);
        resume.setTheme(theme);
        resumeMapper.update(resume);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSections(Long id, Long userId, List<ResumeSection> sections) {
        checkOwner(id, userId);
        sectionMapper.deleteByResumeId(id);
        if (sections != null) {
            for (int i = 0; i < sections.size(); i++) {
                ResumeSection s = sections.get(i);
                s.setId(null);
                s.setResumeId(id);
                s.setSortOrder(i);
                if (s.getVisible() == null) {
                    s.setVisible(1);
                }
                if (s.getContent() == null || s.getContent().isEmpty()) {
                    s.setContent("{}");
                }
                sectionMapper.insert(s);
            }
        }
    }

    public Long copy(Long id, Long userId) {
        ResumeDetailVO vo = detail(id, userId);
        Resume src = vo.getResume();
        Resume copy = new Resume();
        copy.setUserId(userId);
        copy.setTitle(src.getTitle() + "-副本");
        copy.setTheme(src.getTheme());
        resumeMapper.insert(copy);
        if (vo.getSections() != null) {
            for (ResumeSection s : vo.getSections()) {
                s.setId(null);
                s.setResumeId(copy.getId());
                sectionMapper.insert(s);
            }
        }
        return copy.getId();
    }

    public void delete(Long id, Long userId) {
        checkOwner(id, userId);
        sectionMapper.deleteByResumeId(id);
        resumeMapper.delete(id);
    }

    private Resume checkOwner(Long id, Long userId) {
        Resume resume = resumeMapper.selectById(id);
        if (resume == null || !resume.getUserId().equals(userId)) {
            throw new BizException("简历不存在");
        }
        return resume;
    }
}
