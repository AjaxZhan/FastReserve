import subprocess
import json
import yaml
from flask import Flask,request,Response

# 读取配置文件
with open('config.yml','r') as file:
   config = yaml.safe_load(file)

port = config['port'] # Flask应用端口
server_id = config['server']['id'] # 本台服务器ID
server_ip = config['server']['ip'] # 本台服务器主机名


# 处理GPU状态信息，删除敏感字段
def processGpuStatus(gpu_info):
    # 解析JSON
    tmp = json.loads(gpu_info)
    # 主字段
    del tmp['hostname']
    del tmp['driver_version']
    del tmp['query_time']
    tmp['id'] = server_id
    tmp['host'] = server_ip
    # gpu字段
    for gpu in tmp['gpus']:
        del gpu['uuid']
        del gpu['utilization.enc']
        del gpu['utilization.dec']
        del gpu['power.draw']
        del gpu['enforced.power.limit']
        gpu['used'] = gpu['memory.used']
        gpu['total'] = gpu['memory.total']
        del gpu['memory.used']
        del gpu['memory.total']
        del gpu['fan.speed']
        gpu['temperature'] = gpu['temperature.gpu']
        del gpu['utilization.gpu']
        del gpu['temperature.gpu']
        for p in gpu['processes']:
            del p['full_command']
            del p['pid']
            del p['cpu_memory_usage']
            del p['cpu_percent']
            p['usage'] = p['gpu_memory_usage']
            del p['gpu_memory_usage']

    return json.dumps(tmp)
## 获取GPU信息
def gpustat():
    try:
        cmd = 'gpustat --json'
        res = subprocess.check_output(cmd,shell=True,universal_newlines=True)
        res = processGpuStatus(res)
        gpu_info = json.loads(res)
        return json.dumps(gpu_info)
    except Exception as e:
        err_msg = {'code' : 400 , 'msg' : e}
        return json.dumps(err_msg)

app = Flask(__name__)

# 获取GPU状态
@app.route('/getGPUStatus',methods=['GET'])
def get_gpu_status():
    return Response(gpustat(),content_type='application/json')

if __name__ == '__main__':
    app.run(host=server_ip,port=port)