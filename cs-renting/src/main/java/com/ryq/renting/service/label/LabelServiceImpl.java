package com.ryq.renting.service.label;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryq.coldstoragesystem.mapper.LabelMapper;
import com.ryq.coldstoragesystem.bean.RoomLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, RoomLabel> implements LabelService {


    @Autowired
    LabelMapper labelMapper;

    @Override
    public List<RoomLabel> getLabels() {
        return labelMapper.getLabels();
    }

    //删除标签
    @Override
    public boolean deleteById(Integer id) {
        return labelMapper.deleteById(id);
    }

    //根据标签编号获得标签名称
    public RoomLabel getLabelByCode(String labelRoomCode){
        return labelMapper.getLabelByCode(labelRoomCode);
    }
}
