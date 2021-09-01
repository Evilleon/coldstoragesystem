package com.ryq.coldstoragesystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryq.coldstoragesystem.bean.RoomLabel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LabelService extends IService<RoomLabel> {

    // 无条件列举可用标签
    public List<RoomLabel> getLabels();

    //删除标签
    public boolean deleteById(Integer id);

    //根据标签编号获得标签名称
    public RoomLabel getLabelByCode(String labelRoomCode);

}
