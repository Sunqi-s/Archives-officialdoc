package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Admin;
import com.example.entity.FlowObject;
import com.example.exception.CustomException;
import com.example.mapper.FlowObjectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 流转对象（领导/审批人）业务处理
 */
@Service
public class FlowObjectService {

    @Resource
    private FlowObjectMapper flowObjectMapper;

    /**
     * 新增流转对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowObject object) {
        // 参数校验
        if (ObjectUtil.isEmpty(object.getName())) {
            throw new CustomException("5019", "领导姓名不能为空");
        }
        if (ObjectUtil.isEmpty(object.getPosition())) {
            throw new CustomException("5019", "职务不能为空");
        }
        if (ObjectUtil.isEmpty(object.getLevel())) {
            throw new CustomException("5019", "级别不能为空");
        }
        // 自动填充默认值
        object.setCreator(TokenUtils.getCurrentUser().getId()); // 从Token获取当前用户ID（示例默认值）
        object.setCreateDate(new Date());
        object.setLevel(object.getLevel() == null ? 3 : object.getLevel()); // 默认级别为部门（3）
        flowObjectMapper.insert(object);
    }

    /**
     * 根据ID删除流转对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        FlowObject existObject = getById(id); // 检查是否存在
        flowObjectMapper.deleteById(id);
    }

    /**
     * 批量删除流转对象
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id); // 复用单个删除逻辑
        }
    }

    /**
     * 更新流转对象信息
     */
    public void updateById(FlowObject object) {
        FlowObject existObject = getById(object.getId()); // 检查是否存在
        existObject.setName(object.getName());
        existObject.setPosition(object.getPosition());
        existObject.setLevel(object.getLevel());
        existObject.setUpdater(TokenUtils.getCurrentUser().getId()); // 从Token获取当前用户ID
        existObject.setUpdateDate(new Date());
        flowObjectMapper.updateById(existObject);
    }

    /**
     * 根据ID查询流转对象
     */
    public FlowObject getById(Integer id) {
        FlowObject object = flowObjectMapper.selectById(id);
        if (ObjectUtil.isNull(object)) {
            throw new CustomException("5018", "流转对象不存在");
        }
        return object;
    }

    /**
     * 分页查询流转对象列表
     */
    public PageInfo<FlowObject> selectPage(FlowObject query, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); // 启动分页
        List<FlowObject> list = flowObjectMapper.selectPageList(query);
        return PageInfo.of(list);
    }

    /**
     * 查询所有流转对象（用于下拉菜单等场景）
     */
    public List<FlowObject> selectAll() {
        return flowObjectMapper.selectAll();
    }

    public List<FlowObject> selectByLevel(Integer level) {
        List<FlowObject> list = flowObjectMapper.selectByLevel(level);
        if (list.isEmpty()) {
            throw new CustomException("5020", "未配置level=0的办公室流转对象");
        }
        return list;
    }
}
