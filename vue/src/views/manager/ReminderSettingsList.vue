<template>
  <div class="reminder-settings">

    <!-- 搜索条件区域（优化布局+新增查询项） -->
    <el-form :inline="true" class="search-form" label-width="80px">
      <el-form-item label="提醒名称">
        <el-input v-model="query.name" placeholder="请输入提醒名称" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="请选择状态">
          <el-option label="启用" value="0"></el-option>
          <el-option label="禁用" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="query.limitType" placeholder="请选择提醒类型">
          <el-option label="收文提醒" value="收文提醒"></el-option>
          <el-option label="发文提醒" value="发文提醒"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 添加按钮 -->
    <div class="toolbar">
      <el-button type="primary" @click="openAdd(null)">+ 添加提醒设置</el-button>
    </div>

    <!-- 表格展示 -->
    <el-table :data="tableData"
              stripe
              border
              @selection-change="handleSelectionChange"
              style="width: 100%; margin-top: 20px">
      <el-table-column prop="name" label="提醒名称"></el-table-column>
      <el-table-column label="提前提醒时间" align="center">
        <template #default="{row}">
          {{ row.remindBeforeValue }} {{ row.remindBeforeUnit }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template #default="{row}">
          <el-tag :type="row.status === 0 ? 'success' : 'danger'">
            {{ row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="limitType" label="提醒类型"></el-table-column>
      <el-table-column prop="createdTime" label="创建时间"></el-table-column>
      <el-table-column label="操作" width="180px" align="center">
        <template #default="{row}">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button
              size="small"
              type="danger"
              @click="deleteSetting(row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        :current-page="pageNum"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="handlePageChange"
        style="margin-top: 30px"
    ></el-pagination>

    <!-- 编辑弹窗 -->
<!--    <el-dialog-->
<!--        :title="isEditMode ? '编辑提醒设置' : '添加提醒设置'"-->
<!--        v-model="dialogVisible"-->
<!--        width="500px"-->
<!--    >-->

<!--      </template>-->
<!--    </el-dialog>-->
  </div>
</template>

<script>
export default {
  name: 'ReminderSettings',
  data() {
    return {
      // 表格数据 (模拟数据库内容)
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      selectedIds: [],
      query: {
        name: '',
        status: null,
        limitType: null,
      },
      // 弹窗控制
      dialogVisible: false,
      // 是否为编辑模式
      isEditMode: false,
      // 当前编辑的表单数据
      editForm: {
        id: null,
        name: '',
        minutes: 30,
        enabled: true
      },
      // 默认表单数据 (用于重置)
      defaultForm: {
        id: null,
        name: '',
        minutes: 30,
        enabled: true
      }
    };
  },

  created() {
    this.loadData(1);
  },

  methods: {
    // 加载分页数据（核心逻辑）
    loadData(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      // 调用后端分页接口，传递查询条件、页码、页大小
      this.$request.get('/settings/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          // 展开查询条件（与后端IncomingDoc实体字段一一对应）
          ...this.query
        }
      }).then(res => {
        if (res.code === '200') {
          console.log(res)
          this.tableData = res.data.list;    // 表格数据
          this.total = res.data.total;       // 总条数（用于分页）
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 分页页码变化
    handlePageChange(pageNum) {
      this.loadData(pageNum);
    },

    handleSelectionChange(rows) {
      this.selectedIds = rows.map(row => row.id);
    },

    resetQuery(){
      this.query = {
        name: '',
        status: null,
        limitType: null,
      },
      this.loadData(1);
    },

    // 打开添加页面
    openAdd() {
      this.$router.push(`/reminder-settings-register`)
    },
    // 打开编辑页面
    openEdit(row){
      this.$router.push(`/reminder-settings-edit/${row.id}`)
    },

    // 删除设置
    async deleteSetting(id) {
      try {
        await this.$confirm('确定要删除该提醒设置吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });

        // 捕获请求响应
        const res = await this.$request({ // 添加 res 接收返回值
          url: `/settings/delete/${id}`,
          method: 'DELETE'
        });

        // 根据响应码处理结果
        if (res.code === '200') {
          this.$message.success('删除成功');
          this.loadData(this.pageNum);
        } else {
          this.$message.error(res.msg || '删除失败'); // 添加默认错误提示
        }
      } catch (error) {
        // 用户取消时跳过错误提示
        if (error !== 'cancel') {
          const msg = error.response?.data?.msg || error.message;
          this.$message.error(`删除失败: ${msg}`); // 更详细的错误信息
        }
      }
    }
  }
};
</script>

<style scoped>
.reminder-settings {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
  text-align: right;
}

.unit {
  margin-left: 10px;
  color: #888;
  font-size: 14px;
}
</style>
