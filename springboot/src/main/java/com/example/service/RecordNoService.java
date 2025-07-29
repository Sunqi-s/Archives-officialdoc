package com.example.service;

import com.example.mapper.RecordNoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecordNoService {

    @Resource
    private RecordNoMapper recordNoMapper;

    public String getRecordNo(String key) {
        Map<String,String> map=new HashMap<>();
        map.put("invalue",key);
        map.put("rsvalue","");
        recordNoMapper.getId(map);
        String outValue = map.get("rsvalue");
        if(outValue.equals("X")||outValue.equals("")){
            throw  new RuntimeException(key+"编号获取失败");
        }
        return outValue;
    }
}
