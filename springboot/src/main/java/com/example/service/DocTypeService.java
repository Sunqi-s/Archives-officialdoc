package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.DocType;
import com.example.exception.CustomException;
import com.example.mapper.DocTypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DocTypeService {

    @Resource
    private DocTypeMapper docTypeMapper;

    /**
     * 新增来文类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(DocType docType) {
        // 参数校验
        if (ObjectUtil.isEmpty(docType.getName())) {
            throw new CustomException("5019", "类型名称不能为空");
        }
        // 自动填充默认值
        docType.setCreator(TokenUtils.getCurrentUser().getId());
        docType.setCreateDate(new Date());
        docTypeMapper.insert(docType);
    }

    /**
     * 根据ID删除来文类型
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        DocType exist = getById(id);
        docTypeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    /**
     * 更新来文类型
     */
    public void updateById(DocType docType) {
        DocType exist = getById(docType.getId());
        exist.setName(docType.getName());
        exist.setUpdater(TokenUtils.getCurrentUser().getId());
        exist.setUpdateDate(new Date());
        docTypeMapper.updateById(exist);
    }

    /**
     * 根据ID查询
     */
    public DocType getById(Integer id) {
        DocType docType = docTypeMapper.selectById(id);
        if (ObjectUtil.isNull(docType)) {
            throw new CustomException("5018", "来文类型不存在");
        }
        return docType;
    }

    /**
     * 分页查询
     */
    public PageInfo<DocType> selectPage(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DocType> list = docTypeMapper.selectPageList(name);
        return PageInfo.of(list);
    }

    /**
     * 查询所有（用于下拉）
     */
    public List<DocType> selectAll() {
        return docTypeMapper.selectAll();
    }
}
