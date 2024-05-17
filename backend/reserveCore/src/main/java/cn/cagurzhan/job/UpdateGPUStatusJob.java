package cn.cagurzhan.job;

import cn.cagurzhan.entity.GpuStatusParent;
import cn.cagurzhan.entity.Server;
import cn.cagurzhan.mapper.ServerMapper;
import cn.cagurzhan.utils.RedisCache;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cagur
 * @version 1.0
 * 定时任务，定期向各个客户端请求数据。
 */
@Component
public class UpdateGPUStatusJob {
    /** 用于发送HTTP请求 */
    @Autowired
    private RestTemplate restTemplate;

    /** redis缓存 */
    @Autowired
    private RedisCache redisCache;

    /** 服务器mapper */
    @Autowired
    private ServerMapper serverMapper;


    /**
     * 每分钟向各服务器发送请求，获取GPU状态信息
     */
    @Scheduled(cron = " 0 * * * * *")
    public void updateGPUStatusJob(){
        List<String> serverIps = getServerIps();
        for (String serverIp : serverIps) {
            if(serverIp.isEmpty()){
                continue;
            }
            String url = "http://" + serverIp+  ":11200/getGPUStatus";
            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            GpuStatusParent gpuStatusParent = JSON.parseObject(entity.getBody(), GpuStatusParent.class);
            redisCache.setCacheObject("server:"+gpuStatusParent.getId().toString(),gpuStatusParent);
        }

    }

    /**
     * 获取服务器IP
     */
    private List<String> getServerIps(){
        LambdaQueryWrapper<Server> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Server::getIp);
        List<Server> servers = serverMapper.selectList(wrapper);
        return  servers.stream().map(Server::getIp).collect(Collectors.toList());
    }


}
