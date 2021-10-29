package room.entity;

import lombok.Data;

@Data
public class Permission {


    private Integer id;
    /**
     * 权限路径，格式：/xxx
     */
    private String url;

    /**
     * 权限名称，格式：xx:xx
     */
    private String name;

    /**
     *
     * 权限中文名称
     */
    private String comment;
}
