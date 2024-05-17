<template>
  <div class="user-header">
    <el-form :inline="true" :model="searchData">
      <el-form-item label="">
        <el-input
          v-model="searchData.keyword"
          placeholder="请输入公告标题"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button color="#ab3724" @click="handleSearch">搜索</el-button>
      </el-form-item>
    </el-form>
      <el-button color="#ab3724" @click="dialogVisible=true;action = 'add';  formData.title = '';
  formData.content = ''" style="margin-left: auto;">发布公告
      </el-button>
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
            >查看</el-button
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
    :title="action == 'add' ? '新增公告' : '修改公告'"
    width="35%"
    v-model="dialogVisible"
  >
    <el-form :inline="true" :model="formData">
      <el-row>
        <el-form-item label="公告标题" prop="title" :label-width="formLabelWidth">
          <el-input
            placeholder="请输入公告标题"
            v-model="formData.title"
            style="width: 400px"
          />
        </el-form-item>
      </el-row>

      <el-row>
        <el-form-item label="公告内容" :label-width="formLabelWidth" prop="content">
          <el-input
            v-model="formData.content"
            :rows="15"
            type="textarea"
            placeholder="请输入公告内容(支持Markdown)"
            size="large"
            style="width: 400px;"
          />
        </el-form-item>
      </el-row>

      <el-row style="justify-content: flex-end">
        <el-form-item>
          <el-button color="#ab3724" @click="dialogVisible = false"
            >取消</el-button
          >
          <el-button color="#ab3724" @click="onSubmit">发布</el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { getCurrentInstance, ref, reactive, onMounted } from "vue";
import { useUserStore } from "../../stores/user";
const { proxy } = getCurrentInstance();
const userStore = useUserStore()

/**
 * 数据区
 */

// 表单宽度
const formLabelWidth = "80px";
// 对话框显示
const dialogVisible = ref(false);
// 对话框类型：添加/修改
const action = ref("add");

// 搜索数据
const searchData = reactive({
  keyword: "",
});

// 表格标题
const tableLabel = reactive([
  {
    prop: "id",
    label: "ID",
    width: 150,
  },
  {
    prop: "title",
    label: "标题",
    width: 500,
  },
  {
    prop: "createTime",
    label: "发布时间",
    width: 200,
  },
  {
    prop: "updateTime",
    label: "更新时间",
    width: 200,
  },
]);

// 表格展示数据
const tableList = reactive({
  data: [],
});

// 表单数据
const formData = reactive({
  id: "",
  title: "",
  content: "",
  createBy: "",
  createTime : '',
  updateTime : ''
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
 * 工具区
 */

//  分页请求公告数据
const getBulletin = async () => {
  let res = await proxy.$api.getPageBulletin(pageParam)
  tableList.data = res.rows
  pageConfig.total = res.total
};


/**
 * 事件区
 */
// 搜索
const handleSearch = () => {
  console.log(tableList.data);
  tableList.data = tableList.data.filter(
    (item) => item.title.toLowerCase().indexOf(searchData.keyword) > -1
  );
};

// 启动触发
onMounted(async () => {
  await getBulletin()
});

// 分页
const handleChangePage = async (page) => {
  pageParam.pageNum = page;
  await getBulletin()
};

// 添加公告 / 修改公告
const onSubmit = async () => {

  if (action.value === "add") {
    formData.createBy = userStore.user.userInfo.id
    await proxy.$api.addBulletin(formData)
    ElMessage({
      message: "添加成功",
      type: "success",
    });
  } else {
    formData.createBy = userStore.user.userInfo.id
    await proxy.$api.updateBulletin(formData)
    ElMessage({
      message: "修改成功",
      type: "success",
    });
  }
  await getBulletin(pageParam);
  dialogVisible.value = false;
};

// 编辑公告对话框显示
const handleEdit = (row) => {
  action.value = "edit";
  dialogVisible.value = true;

  formData.id = row.id;
  formData.content = row.content;
  formData.title = row.title;
};

// 删除公告
const handleDelete = async (row) => {
  formData.createBy = userStore.user.userInfo.id
  await proxy.$api.deleteBulletin(row);
  ElMessage({
    message: "删除成功",
    type: "success",
  });
  await getBulletin()
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
  margin-bottom: 20px;
}
</style>
