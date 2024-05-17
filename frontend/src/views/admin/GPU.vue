<template>
  <div class="user-header">
    <el-button
      color="#ab3724"
      @click="
        dialogVisible = true;
        action.value = 'add';
      "
      >新增</el-button
    >
    <el-form :inline="true" :model="formData">
      <el-form-item label="">
        <el-input
          v-model="formData.keyword"
          placeholder="请输入服务器ID"
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
    :title="action == 'add' ? '新增GPU' : '编辑GPU'"
    width="35%"
    v-model="dialogVisible"
  >
    <el-form :inline="true" :model="formGPU">
      <el-row>
        <el-form-item
          label="所属服务器ID"
          prop="serverId"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="formGPU.serverId"
            placeholder="这里输入所属服务器的ID"
            clearable
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item
            label="序号"
            prop="number"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formGPU.number"
              placeholder="请输入GPU序号"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-form-item label="型号" prop="model" :label-width="formLabelWidth">
          <el-input
            v-model="formGPU.model"
            placeholder="请输入GPU型号"
            clearable
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
          <el-select v-model="formGPU.status" placeholder="GPU状态">
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
import { ElMessage } from "element-plus";
import { getCurrentInstance, ref, reactive, onMounted } from "vue";
const { proxy } = getCurrentInstance();

/**
 * 数据区
 */

 // 表单宽度
const formLabelWidth = "150px";
// 对话框可见性
const dialogVisible = ref(false);

// 表格标题
const tableLabel = reactive([
  {
    prop: "id",
    label: "ID",
    width: 150,
  },
  {
    prop: "serverId",
    label: "服务器ID",
    width: 150,
  },
  {
    prop: "number",
    label: "GPU序号",
    width: 150,
  },
  {
    prop: "model",
    label: "GPU型号",
    width: 150,
  },
  {
    prop: "status",
    label: "状态",
    width: 150,
  },
]);

// 对话框行为
const action = ref("add");

// GPU表格数据
const tableList = reactive({
  data: [],
});

// GPU表单数据
const formGPU = reactive({
  id: "",
  serverId: "",
  number: "",
  model: "",
  status: "",
});

// 搜索数据
const formData = reactive({
  keyword: "",
});

// 分页信息
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

// 加载
onMounted(async () => {
  await getUserData(pageParam);
});

// 请求用户数据
const getUserData = async (params) => {
  let res = await proxy.$api.pageGPU(params);

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
  console.log(tableList.data);
  tableList.data = tableList.data.filter(
    (item) => item.serverId.toLowerCase().indexOf(formData.keyword) > -1
  );
};

// 修改内容
const onSubmit = async () => {
  if (action.value === "add") {
    let res = await proxy.$api.addGPU(formGPU);
    if (res === undefined) {
      ElMessage({
        message: "添加成功",
        type: "success",
      });
    }
  } else {
    let res = await proxy.$api.updateGPU(formGPU);
    if (res === undefined) {
      ElMessage({
        message: "修改成功",
        type: "success",
      });
    }
  }

  await getUserData(pageParam);
  dialogVisible.value = false;
};

// 编辑GPU信息
const handleEdit = (row) => {
  action.value = "edit";
  dialogVisible.value = true;

  formGPU.id = row.id;
  formGPU.model = row.model;
  formGPU.number = row.number;
  formGPU.serverId = row.serverId;
  formUser.status = row.status === "正常" ? 0 : 1;
};

// 删除GPU
const handleDelete = async (row) => {
  let res = await proxy.$api.deletGPU(row);
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
