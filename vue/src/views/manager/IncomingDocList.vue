<template>
  <div class="incoming-list">
    <!-- 搜索条件区域（优化布局+新增查询项） -->
    <el-form :inline="true" class="search-form" label-width="80px">
      <el-form-item label="文件标题">
        <el-input v-model="query.title" placeholder="请输入文件标题" style="width: 200px"></el-input>
      </el-form-item>

      <el-form-item label="来文单位">
        <el-input v-model="query.senderOrg" placeholder="请输入来文单位" style="width: 200px"></el-input>
      </el-form-item>

      <el-form-item label="密级">
        <el-select v-model="query.secretLevel" placeholder="请选择密级" style="width: 180px">
          <el-option label="普通" value="普通"></el-option>
          <el-option label="秘密" value="秘密"></el-option>
          <el-option label="机密" value="机密"></el-option>
          <el-option label="绝密" value="绝密"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="紧急程度">
        <el-select v-model="query.urgencyLevel" placeholder="请选择紧急程度" style="width: 180px">
          <el-option label="普通" value="普通"></el-option>
          <el-option label="平急" value="平急"></el-option>
          <el-option label="加急" value="加急"></el-option>
          <el-option label="特急" value="特急"></el-option>
          <el-option label="特提" value="特提"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="处理状态">
        <el-select v-model="query.processType" placeholder="请选择处理状态" style="width: 180px">
          <el-option label="未处理" value="未处理"></el-option>
          <el-option label="处理中" value="处理中"></el-option>
          <el-option label="存档" value="存档"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="来文日期">
        <el-date-picker
            v-model="query.receiveDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 280px"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="info" plain @click="loadData(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar" style="margin: 20px 0">
      <el-button type="primary" @click="handleAdd">收文登记</el-button>
      <el-button type="success" @click="handleArchive">批量存档</el-button>
    </div>

    <el-table
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
        style="width: 100%; margin-top: 20px"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="fileNo" label="文件编号" width="100"></el-table-column>
      <el-table-column prop="title" label="文件标题" width="220"></el-table-column>
      <el-table-column prop="senderOrg" label="来文单位" width="180"></el-table-column>
      <el-table-column prop="secretLevel" label="密级" width="100"></el-table-column>
      <el-table-column prop="urgencyLevel" label="紧急程度" width="120"></el-table-column>
      <el-table-column prop="receiveDate" label="来文日期" width="120"></el-table-column>
      <el-table-column prop="handlingOrg" label="办件单位" width="180"></el-table-column>
      <el-table-column prop="processType" label="处理状态" width="80"></el-table-column>
      <el-table-column label="打印" width="140">
        <template v-slot="scope">
          <el-button type="warning" size="mini" @click="handlePrint(scope.row.id)">打印处理单</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template v-slot="scope">
          <el-button size="mini" @click="handleDetail(scope.row.id)">详情</el-button>
          <el-button size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      selectedIds: [],
      // 查询条件对象（与后端IncomingDoc实体字段对应）
      query: {
        title: '',          // 文件标题（模糊查询）
        senderOrg: '',      // 来文单位（模糊查询）
        processType: '' ,    // 处理状态（精确查询）
        secretType: null // 密件类型（0-普通件，1-密件）
      }
    };
  },
  created() {
    this.query.secretType = parseInt(this.$route.params.secretType);
    // 初始化加载第一页数据
    this.loadData(1);
  },
  watch: {
    // 监听路由参数变化
    $route(to) {
      this.query.secretType = parseInt(to.params.secretType);
      this.loadData(1);
    }
  },
  methods: {
    // 加载分页数据（核心逻辑）
    loadData(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      // 调用后端分页接口，传递查询条件、页码、页大小
      this.$request.get('/incomingDoc/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          // 展开查询条件（与后端IncomingDoc实体字段一一对应）
          ...this.query
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list;    // 表格数据
          this.total = res.data.total;       // 总条数（用于分页）
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    // 重置查询条件
    resetQuery() {
      this.query = {
        title: '',
        senderOrg: '',
        processType: '',
        secretType: null // 密件类型（0-普通件，1-密件）
      };
      this.loadData(1);  // 重置后重新加载第一页
    },

    // 分页页码变化
    handlePageChange(pageNum) {
      this.loadData(pageNum);
    },

    handleAdd() {
      this.$router.push(`/incoming-doc-register/${this.query.secretType}`);
    },

    handleDetail(id) {
      this.$router.push(`/incoming-doc-detail/${id}`);
    },
    handleEdit(id){
      this.$router.push(`/incoming-doc-edit/${id}/${this.query.secretType}`)
    },
    handleDelete(id) {
      this.$confirm('确定删除该收文？', '提示', { type: 'warning' })
          .then(() => {
            this.$request.delete(`/incomingDoc/delete/${id}`).then(res => {
              if (res.code === '200') {
                this.$message.success('删除成功');
                this.loadData(this.pageNum);  // 删除后刷新当前页
              } else {
                this.$message.error(res.msg);
              }
            });
          });
    },
    handleArchive() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择需要存档的收文');
        return;
      }
      this.$confirm('确定批量存档选中的收文？', '提示', { type: 'warning' })
          .then(() => {
            // 假设后端支持批量存档（需与后端确认接口）
            // 这里示例使用循环调用单个存档接口，实际可优化为批量接口
            Promise.all(this.selectedIds.map(id =>
                this.$request.put(`/incomingDoc/archive/${id}`)
            )).then(() => {
              this.$message.success('批量存档成功');
              this.loadData(this.pageNum);  // 刷新当前页
            });
          });
    },
    handlePrint(id) {
        const tpl_name = "收发文新系统.ureport.xml";
        const pageIndex = 1;     // 页码
        const renderOption = 1;  // 渲染选项
        const url = `http://localhost:9090/ureport/preview?_u=mysql:${tpl_name}&_i=${pageIndex}&_r=${renderOption}&ids=${id}`;
        window.open(url, '_blank');
    },
    handleSelectionChange(rows) {
      this.selectedIds = rows.map(row => row.id);
    }
  }
};
</script>

<style scoped>
/* 优化搜索区域样式 */
.search-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
}

/* 调整表格列间距 */
.el-table .el-table__cell {
  padding: 12px 0;
}

/* 工具栏按钮间距 */
.toolbar .el-button {
  margin-right: 10px;
}
</style>

