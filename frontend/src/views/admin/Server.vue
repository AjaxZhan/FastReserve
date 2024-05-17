<template>
  <div class="user-header">
    <el-button
      color="#ab3724"
      @click="
        dialogVisible = true;
        action = 'add';
      "
      >新增</el-button
    >
    <el-form :inline="true" :model="formData">
      <el-form-item label="">
        <el-input
          v-model="formData.keyword"
          placeholder="请输入服务器名字"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button color="#ab3724" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
  </div>
  <div class="table">
    <el-table
      :data="tableList.data"
      style="width: 100%"
      row-style="height:50px"
    >
      <el-table-column
        v-for="item in tableLabel"
        :key="item.prop"
        :label="item.label"
        :prop="item.prop"
        :width="item.width ? item.width : 125"
        align="center"
        style="font-size: 20px"
      />

      <el-table-column fixed="right" label="操作" min-width="200">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-popconfirm
            title="确定要删除吗？"
            @confirm="handleDelete(scope.row)"
          >
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="parseInt(pageConfig.total)"
      :page-size="pageConfig.pageSize"
      class="pager mt-4"
      @current-change="handleChangePage"
    />
  </div>

  <el-dialog
    :title="action == 'add' ? '新增服务器' : '编辑服务器'"
    width="35%"
    v-model="dialogVisible"
  >
    <el-form :inline="true" :model="formServer">
      <el-row>
        <el-form-item
          label="服务器名字"
          prop="name"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="formServer.name"
            placeholder="请输入服务器名字"
            clearable
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item
          label="GPU数量"
          prop="volume"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="formServer.volume"
            placeholder="请输入服务器容量"
            clearable
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item
          label="内网IP"
          prop="ip"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="formServer.ip"
            placeholder="请输入服务器内网IP地址"
            clearable
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
          <el-select v-model="formServer.status" placeholder="服务器状态">
            <el-option label="正常" :value="0"></el-option>
            <el-option label="冻结" :value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-row>

      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button color="#ab3724" @click="dialogVisible = false"
            >取消</el-button
          >
          <el-button color="#ab3724" @click="onSubmit">确定</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { ElMessage, ElNotification } from "element-plus";
import { getCurrentInstance, ref, reactive, onMounted } from "vue";
const { proxy } = getCurrentInstance();


/**
 * 数据区
 */

// 表单宽度
const formLabelWidth = "180px";
// 对话框可视性
const dialogVisible = ref(false);

// 表格标题
const tableLabel = reactive([
  {
    prop: "id",
    label: "ID",
    width: 200,
  },
  {
    prop: "name",
    label: "服务器名称",
    width: 200,
  },
  {
    prop: "volume",
    label: "服务器容量",
    width: 200,
  },
  {
    prop : 'ip',
    label : '内网IP',
    width : 200,
  },
  {
    prop: "status",
    label: "服务器状态",
    width: 200,
  },
]);

// 对话框行为
const action = ref("add");

// 服务器表格信息
const tableList = reactive({
  data: [],
});

// 服务器表单数据
const formServer = reactive({
  id: "",
  name: "",
  volume: "",
  status: "",
  ip : '',
});

// 搜索
const formData = reactive({
  keyword: "",
});

// Page 分页信息
const pageConfig = reactive({
  total: 0,
  pageSize: 10,
});

// 分页请求参数
const pageParam = reactive({
  pageNum: 1,
  pageSize: pageConfig.pageSize,
});

/**
 * 事件区
 */

// onMounted
onMounted(async () => {
  await getUserData(pageParam);
});


// 请求服务器数据
const getUserData = async (params) => {
  let res = await proxy.$api.getPageServer(params);

  pageConfig.total = res.total;

  tableList.data = res.rows.map((item) => {
    item.status = item.status === "0" ? "正常" : "冻结";
    return item;
  });
};

// 分页
const handleChangePage = (page) => {
  pageParam.pageNum = page;
  getUserData(pageParam);
};

// 搜索
const handleSearch = () => {
  tableList.data = tableList.data.filter(
    (item) => item.name.toLowerCase().indexOf(formData.keyword) > -1
  );
};

//  修改内容
const onSubmit = async () => {
  if (action.value === "add") {
    let res = await proxy.$api.addServer(formServer);
      ElMessage({
        message: "添加成功",
        type: "success",
      });
  } else {
    let res = await proxy.$api.updateServer(formServer);
      ElMessage({
        message: "修改成功",
        type: "success",
      });
  }

  await getUserData(pageParam);
};

// 编辑服务器
const handleEdit = (row) => {

  action.value = "edit";
  dialogVisible.value = true;

  formServer.id = row.id;
  formServer.name = row.name;
  formServer.status = row.status === "正常" ? 0 : 1;
  formServer.volume = row.volume;
  formServer.ip = row.ip
};

// 删除服务器
const handleDelete = async (row) => {
  let res = await proxy.$api.deleteServer(row);
  if (res !== null) {
    ElMessage({
      message: "删除成功",
      type: "success",
    });
    await getUserData(pageParam);
  }
};
</script>

<style lang="less" scoped>
.table {
  position: relative;
  height: 520px;
  .pager {
    position: relative;
    margin-top: 30px;
    justify-content: right;
  }
}

.el-table .td {
  text-align: center !important;
}

.user-header {
  display: flex;
  justify-content: space-between;
}
</style>
