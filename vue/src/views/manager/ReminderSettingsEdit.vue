<template>
  <div class="reminder-settings">
    <el-card class="register-card" shadow="always">
      <div class="register-header">
        <h1>修改提醒设置</h1>
      </div>

      <el-form :model="form" label-width="100px">
        <el-form-item label="提醒名称" required>
          <el-input v-model="form.name" placeholder="请输入提醒名称"/>
        </el-form-item>
        <el-form-item label="提前时间" required>
          <div class="time-input-group">
            <el-input-number
                v-model="form.remindBeforeValue"
                :min="minValues[form.remindBeforeUnit]"
                :max="maxValues[form.remindBeforeUnit]"
                controls-position="right"
                class="time-input"
            />
            <el-select
                v-model="form.remindBeforeUnit"
                class="unit-select"
                @change="handleUnitChange">
              <el-option label="分钟" value="minutes"></el-option>
              <el-option label="小时" value="hours"></el-option>
              <el-option label="天" value="days"></el-option>
              <el-option label="月" value="months"></el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" :value="0"></el-option>
            <el-option label="禁用" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提醒类型">
          <el-select v-model="form.limitType">
            <el-option label="收文提醒" value="INCOMING_DOC"></el-option>
            <el-option label="发文提醒" value="OUTGOING_DOC"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文档类型">
          <el-select v-model="form.docType">
            <el-option label="普通件" value="0"></el-option>
            <el-option label="密件" value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="dialogVisible">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ReminderSettingsEdit',
  data() {
    return {
      form: {
        name: '',
        remindBeforeValue: 30,
        remindBeforeUnit: 'minutes',
        status: null,
        limitType: null,
        docType: ''
      },
      minValues: {
        minutes: 5,
        hours: 1,
        days: 1,
        months: 1
      },
      maxValues: {
        minutes: 60 * 24 * 30,  // 约1个月
        hours: 24 * 30,       // 约1个月
        days: 365,          // 1年
        months: 12          // 1年
      }
    };
  },

  mounted(){
    this.loadSettings();
  },

  methods: {
    handleUnitChange(unit) {
      // 单位切换时自动调整数值范围
      if (this.form.remindBeforeValue < this.minValues[unit]) {
        this.form.remindBeforeValue = this.minValues[unit];
      } else if (this.form.remindBeforeValue > this.maxValues[unit]) {
        this.form.remindBeforeValue = this.maxValues[unit];
      }
    },

    // 保存设置
    saveSettings() {
      try {
        if (!this.form.name.trim()) {
          this.$message.warning('请输入提醒名称');
          return;
        }

        if (!this.form.remindBeforeValue.toString().trim()) {
          this.$message.warning('请输入提前时间');
          return;
        }

        // 验证提前时间是否合法
        if (isNaN(this.form.remindBeforeValue) || this.form.remindBeforeValue < 0) {
          this.$message.warning('提前时间必须为正整数');
          return;
        }

        // 验证提前时间单位是否合法
        if (!['minutes', 'hours', 'days', 'months'].includes(this.form.remindBeforeUnit)) {
          this.$message.warning('提前时间单位不合法');
          return;
        }

        // 模拟保存到数据库的请求
        this.$request.put('/settings/update', this.form).then(res => {
          if (res.code === '200'){
            this.$message.success('保存成功');
            this.dialogVisible();
          }
        })
      } catch (error) {
        this.$message.error('保存失败: ' + error.message);
      }
    },

    resetForm() {
      this.form = {
        name: '',
        remindBeforeValue: 30,
        remindBeforeUnit: 'minutes',
        status: null,
        limitType: null,
        docType: ''
      };
    },

    dialogVisible(){
      this.$router.push(`/reminder-settings-list`)
    },

    loadSettings(){
      const id = this.$route.params.id;
      if (!id) {
        this.$message.error('参数错误');
        return;
      }

      console.log('loadSettings', id)
      this.$request.get(`/settings/selectById/${id}`).then(res => {
        console.log('res', res)
        if (res.code === '200') {
          this.form = res.data;
        }else {
          this.$message.error('加载失败: ' + res.message);
        }
      })
      .catch(error => {
        this.$message.error('加载失败: ' + error.message);
      })
    }
  }
};
</script>

<style scoped>
.reminder-settings {
  padding: 20px;
  background-color: #f8f9fa;
}

.register-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 0 15px rgba(0,0,0,0.05);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  color: #1890ff;
}

.form-footer {
  text-align: right;
  margin-top: 30px;
}

</style>
