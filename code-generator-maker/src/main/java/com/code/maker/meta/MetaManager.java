package com.code.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author ctc
 * @date 2024/1/23
 */
public class MetaManager {

    private static volatile Meta meta;

    public static Meta getMetaObject() {
        // 双重检查锁
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    public static Meta initMeta() {
        // 修改生成器的默认配置
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        // String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        // 校验和处理默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }

}
