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
          placeholder="请输入用户名"
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
            title="再次确认"
            @confirm="handleStatusChange(scope.row)"
          >
            <template #reference>
              <el-button type="warning" size="small">激活/冻结</el-button>
            </template>
          </el-popconfirm>
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
    :title="action == 'add' ? '新增用户' : '编辑用户'"
    width="35%"
    v-model="dialogVisible"
  >
    <el-form :inline="true" :model="formUser">
      <el-row>
        <el-col :span="12">
          <el-form-item
            label="用户名"
            prop="username"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formUser.username"
              placeholder="请输入用户名"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="姓名"
            prop="nickname"
            :label-width="formLabelWidth"
          >
            <el-input
              v-model="formUser.nickname"
              placeholder="请输入姓名"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" prop="sex" :label-width="formLabelWidth">
            <el-select v-model="formUser.sex" placeholder="请选择性别">
              <el-option label="男" :value="0"></el-option>
              <el-option label="女" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email" :label-width="formLabelWidth">
            <el-input placeholder="请输入邮箱地址" v-model="formUser.email" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="权限" prop="type" :label-width="formLabelWidth">
            <el-select v-model="formUser.type" placeholder="用户权限">
              <el-option label="用户" :value="0"></el-option>
              <el-option label="管理员" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="状态"
            prop="status"
            :label-width="formLabelWidth"
          >
            <el-select v-model="formUser.status" placeholder="账户状态">
              <el-option label="正常" :value="0"></el-option>
              <el-option label="冻结" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-form-item
          label="密码"
          prop="password"
          :label-width="formLabelWidth"
        >
          <el-input
            placeholder="请输入密码"
            v-model="formUser.password"
            style="width: 400px"
          />
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
const formLabelWidth = "60px";
const dialogVisible = ref(false);

// 表格标题
const tableLabel = reactive([
  {
    prop: "id",
    label: "ID",
    width: 100,
  },
  {
    prop: "username",
    label: "用户名",
    width: 150,
  },
  {
    prop: "nickname",
    label: "姓名",
    width: 150,
  },
  {
    prop: "sex",
    label: "性别",
    width: 100,
  },
  {
    prop: "email",
    label: "邮箱",
    width: 200,
  },
  {
    prop: "type",
    label: "权限",
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

// 用户信息Table
const tableList = reactive({
  data: [],
});

// User数据
const formUser = reactive({
  id: "",
  username: "",
  password: "",
  nickname: "",
  email: "",
  status: "",
  type: "",
  sex: "",
});

//  搜索
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

// 加载
onMounted(async () => {
  await getUserData(pageParam);
});

//  请求用户数据
const getUserData = async (params) => {
  let res = await proxy.$api.getAdminUser(params);

  pageConfig.total = res.total;

  tableList.data = res.rows.map((item) => {
    item.sex = item.sex === "0" ? "男" : "女";
    item.status = item.status === "0" ? "正常" : "冻结";
    item.type = item.type === "0" ? "用户" : "管理员";
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
    (item) => item.username.toLowerCase().indexOf(formData.keyword) > -1
  );
};

// 修改内容
const onSubmit = async () => {
  if (action.value === "add") {
    let res = await proxy.$api.addAdminUser(formUser);
    ElMessage({
      message: "添加成功",
      type: "success",
    });
  } else {
    console.log(formUser);
    let res = await proxy.$api.updateAdminUser(formUser);
    ElMessage({
      message: "修改成功",
      type: "success",
    });
  }
  await getUserData(pageParam);
  dialogVisible.value = false;
};

// 编辑用户
const handleEdit = (row) => {
  action.value = "edit";
  dialogVisible.value = true;

  console.log(row);

  formUser.id = row.id;
  formUser.username = row.username;
  formUser.nickname = row.nickname;
  formUser.email = row.email;
  formUser.status = row.status === "正常" ? 0 : 1;
  formUser.sex = row.sex === "男" ? 0 : 1;
  formUser.type = row.type === "用户" ? 0 : 1;
};

// 删除用户
const handleDelete = async (row) => {
  let res = await proxy.$api.deleteUser(row);
  ElMessage({
    message: "删除成功",
    type: "success",
  });
  await getUserData(pageParam);
};

// 修改用户状态
const handleStatusChange = async (row) => {
  let res = await proxy.$api.toggleUserStatus(row);
  ElMessage({
    message: row.status === "冻结" ? "激活成功" : "冻结成功",
    type: "success",
  });
  await getUserData(pageParam);
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
