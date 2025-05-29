<template>
  <div>
    <!-- 搜索栏 -->
    <div class="search">
      <el-input
          placeholder="请输入领导姓名"
          style="width: 200px"
          v-model="query.name"
      ></el-input>
      <el-button
          type="info"
          style="margin-left: 10px"
          @click="load(1)"
      >查询</el-button>
      <el-button
          type="warning"
          style="margin-left: 10px"
          @click="reset"
      >重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="operation">
      <el-button
          type="primary"
          @click="handleAdd"
      >新增</el-button>
      <el-button
          type="danger"
          @click="delBatch"
          :disabled="!ids.length"
      >批量删除</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table">
      <el-table
          :data="tableData"
          stripe
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="领导姓名"></el-table-column>
        <el-table-column prop="position" label="职务"></el-table-column>
        <el-table-column prop="level" label="级别" width="80" align="center">
          <template #default="scope">
            {{ scope.row.level === 1 ? '主任' : scope.row.level === 2 ? '副主任' : '部门' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button
                size="mini"
                type="primary"
                @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="del(scope.row.id)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        ></el-pagination>
      </div>
    </div>

    <!-- 编辑/新增对话框 -->
    <el-dialog
        title="流转对象管理"
        :visible.sync="dialogVisible"
        width="30%"
        :close-on-click-modal="false"
    >
      <el-form
          :model="form"
          label-width="80px"
          :rules="rules"
          ref="formRef"
      >
        <el-form-item label="领导姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入领导姓名"></el-input>
        </el-form-item>
        <el-form-item label="职务" prop="position">
          <el-input v-model="form.position" placeholder="请输入职务"></el-input>
        </el-form-item>
        <el-form-item label="级别" prop="level">
          <el-select v-model="form.level" placeholder="请选择级别">
            <el-option label="办公室（拟办意见）" value="0"></el-option>
            <el-option label="主要领导" value="1"></el-option>
            <el-option label="分管领导" value="2"></el-option>
            <el-option label="部门" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DocFlowObjectList",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      query: { name: null },
      dialogVisible: false,
      form: { level: '1' }, // 默认级别为主任
      rules: {
        name: [{ required: true, message: '请输入领导姓名', trigger: 'blur' }],
        position: [{ required: true, message: '请输入职务', trigger: 'blur' }],
        level: [{ required: true, message: '请选择级别', trigger: 'change' }]
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    // 新增/编辑
    handleAdd() {
      this.form = { level: '1' } // 新增时默认级别
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },

    // 保存
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/flowObject/update' : '/flowObject/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('操作成功')
              this.load(1)
              this.dialogVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },

    // 删除
    del(id) {
      this.$confirm('确认删除？', '提示', { type: "warning" })
          .then(() => {
            this.$request.delete(`/flowObject/delete/${id}`)
                .then(res => {
                  if (res.code === '200') this.load(1)
                })
          })
    },

    // 批量删除
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('确认批量删除？', '提示', { type: "warning" })
          .then(() => {
            this.$request.delete('/flowObject/delete/batch', { data: this.ids })
                .then(res => {
                  if (res.code === '200') this.load(1)
                })
          })
    },

    // 分页加载
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/flowObject/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.query.name
        }
      }).then(res => {
        this.tableData = res.data.list
        this.total = res.data.total
      })
    },

    // 重置搜索
    reset() {
      this.query.name = null
      this.load(1)
    },

    // 选中项变化
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    }
  }
}
</script>
