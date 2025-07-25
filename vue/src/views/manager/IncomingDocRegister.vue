<template>
  <div class="incoming-register">
    <el-card class="register-card" shadow="always">
      <div class="register-header">
        <h1>收文登记
          <span v-if=" this.form.secretType === 1" style="color: red; margin-left: 10px;">(密件)</span>
        </h1>
      </div>
      <el-form
          :model="form"
          :rules="rules"
          ref="formRef"
          label-width="100px"
          class="register-form"
      >
        <div class="form-row">
          <el-form-item label="来文类型" prop="docType">
            <el-select v-model="form.docType" placeholder="请选择来文类型">
              <!-- 动态加载来文类型 -->
              <el-option
                  v-for="type in docTypes"
                  :key="type.id"
                  :label="type.name"
                  :value="type.name"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="来文日期" prop="receiveDate">
            <el-date-picker
                v-model="form.receiveDate"
                type="date"
                placeholder="选择来文日期"
                value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>

          <el-form-item label="联系人" prop="contactPerson">
            <el-input v-model="form.contactPerson" placeholder="请输入联系人"></el-input>
          </el-form-item>
        </div>

        <div class="form-row">
          <el-form-item label="来文单位" prop="senderOrg" >
            <el-input v-model="form.senderOrg" placeholder="请输入来文单位" required></el-input>
          </el-form-item>

          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系电话" pattern="^1[3-9]\d{9}$"></el-input>
          </el-form-item>

        </div>

        <div class="form-row">

          <el-form-item label="密级" prop="secretLevel">
            <el-select v-model="form.secretLevel" placeholder="请选择密级">
              <el-option label="普通" value="普通"></el-option>
              <el-option label="秘密" value="秘密"></el-option>
              <el-option label="机密" value="机密"></el-option>
              <el-option label="绝密" value="绝密"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="年度" prop="annual">
            <el-input v-model.number="form.annual" placeholder="请输入年度（如2025）" type="number"></el-input>
          </el-form-item>
          <el-form-item label="份数" prop="copies">
            <el-input v-model.number="form.copies" placeholder="请输入文件份数" type="number"></el-input>
          </el-form-item>

          <el-form-item label="紧急程度" prop="urgencyLevel">
            <el-select v-model="form.urgencyLevel" placeholder="请选择紧急程度">
              <el-option label="普通" value="普通"></el-option>
              <el-option label="平急" value="平急"></el-option>
              <el-option label="加急" value="加急"></el-option>
              <el-option label="特急" value="特急"></el-option>
              <el-option label="特提" value="特提"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="文件页数" prop="pageCount">
            <el-input v-model.number="form.pageCount" placeholder="请输入文件页数" type="number"></el-input>
          </el-form-item>
        </div>

        <div class="form-row">
          <el-form-item label="文件标题" prop="title" >
            <el-input type="textarea" v-model="form.title" placeholder="请输入文件标题" rows="3"></el-input>
          </el-form-item>

          <el-form-item label="文种/期号" prop="documentType">
            <el-input v-model="form.documentType" placeholder="如“函〔2025〕1号”"></el-input>
          </el-form-item>

        </div>

        <div class="form-row">
          <el-form-item label="盒号" prop="boxNo">
            <el-input v-model="form.boxNo" placeholder="请输入盒号"></el-input>
          </el-form-item>

          <el-form-item label="来文编号" prop="senderDocNo">
            <el-input v-model="form.senderDocNo" placeholder="请输入来文编号"></el-input>
          </el-form-item>

          <el-form-item label="限时反馈">
            <el-checkbox v-model="form.feedbackRequired"></el-checkbox>
          </el-form-item>

          <el-form-item label="限办时间">
            <el-date-picker
              v-model="form.limitDate"
              type="datetime"
              placeholder="选择限办日期"
              ></el-date-picker>
          </el-form-item>
        </div>

        <div class="form-row">

          <el-form-item label="处理单类型" prop="processType" >
            <el-select v-model="form.processType" placeholder="请选择处理单类型">
              <el-option label="不打印" value="不打印"></el-option>
              <el-option label="收文单" value="收文单"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="归档号" prop="archiveNo">
            <el-input v-model="form.archiveNo" placeholder="请输入归档号"></el-input>
          </el-form-item>

          <el-form-item label="文件内容" prop="attachmentId">
            <el-upload
                class="document-upload"
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: user.token }"
                list-type="picture"
                :on-success="handleAttachmentSuccess"
                :before-upload="handleBeforeUpload"
            >
              <el-button type="primary">上传扫描件</el-button>
            </el-upload>
          </el-form-item>
        </div>

        <div class="form-row full-width">
          <el-form-item label="办件单位">
            <el-input v-model="form.handlingOrg" readonly value="大连长兴岛经济区管委会办公室"></el-input>
          </el-form-item>

          <el-form-item label="拟办意见" prop="opinion">
            <el-input type="textarea" v-model="form.opinion" placeholder="请输入拟办意见" rows="5"></el-input>
          </el-form-item>
        </div>

        <div class="form-footer">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="submitForm">提交登记</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "IncomingDocRegister",
  data() {
    return {
      docTypes: '',
      form: {
        docType: '',
        receiveDate: new Date(),
        contactPerson: '',
        senderOrg: '',
        contactPhone: '',
        secretLevel: '普通',
        annual: new Date().getFullYear(),
        copies: 1,
        urgencyLevel: '普通',
        pageCount: null,
        title: '',
        documentType: '',
        boxNo: '',
        fileNo: null,
        senderDocNo: '',
        feedbackRequired: false,
        limitDate: null,
        processType: '收文单',
        archiveNo: '',
        handlingOrg: '大连长兴岛经济区管委会办公室',
        opinion: '',
        attachmentId: null, // 附件ID
        secretType: 0, // 0=普通件，1=密件（通过路由参数传递）
        fileList: null
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        docType: [{ required: true, message: '请选择来文类型', trigger: 'change' }],
        receiveDate: [{ required: true, message: '请选择来文日期', trigger: 'change' }],
        title: [{ required: true, message: '请输入文件标题', trigger: 'blur' }],
        senderOrg: [{ required: true, message: '请输入来文单位', trigger: 'blur' }]
      }
    };
  },
  created() {
    // 初始化时同步计算属性值到表单
    this.resetForm();
    this.form.secretType = parseInt(this.$route.params.secretType);
    this.loadDocTypes();
  },

  methods: {
    // 加载来文类型
    loadDocTypes() {
      this.$request.get('/docType/all').then(res => {
        if (res.code === '200') {
          this.docTypes = res.data;
        }
      });
    },
    handleAttachmentSuccess(response, file, fileList) {
      console.log(response, file, fileList)
      this.form.attachmentId = response.data.id; // 假设后端返回附件ID
      let attachment = {
        fileName: file.name,
        filePath: response.data
      }
      this.form.file = attachment;
    },
    handleBeforeUpload(file) {
      const isPDF = file.type === 'application/pdf';
      if (!isPDF) {
        this.$message.error('仅支持PDF文件');
        return false;
      }
      return true;
    },
    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          console.log(this.form)
          this.$request.post('/incomingDoc/add', this.form).then(res => {
            if (res.code === '200') {
              this.$message.success('收文登记成功');
              this.goBack(this.form.secretType);
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    goBack(secretType) {
      // 根据密件类型返回对应的列表页
      const path = secretType === 1
          ? `/incoming-doc-secret/${secretType}`
          : `/incoming-doc-normal/${secretType}`;
      console.log(path)
      this.$router.push(path);
    },
    resetForm() {
      this.form = {
            docType: '',
            receiveDate: new Date(),
            contactPerson: '',
            senderOrg: '',
            contactPhone: '',
            secretLevel: '普通',
            annual: new Date().getFullYear(),
            copies: 1,
            urgencyLevel: '普通',
            pageCount: null,
            title: '',
            documentType: '',
            boxNo: '',
            fileNo: null,
            senderDocNo: '',
            feedbackRequired: false,
            processType: '收文单',
            archiveNo: '',
            handlingOrg: '大连长兴岛经济区管委会办公室',
            opinion: '',
            attachmentId: null, // 附件ID
            secretType: 0 // 0=普通件，1=密件（通过路由参数传递）
      }
    }
  }
};
</script>

<style scoped>
.incoming-register {
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

.form-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 14px;

  &:last-child {
    margin-bottom: 0;
  }

  .el-form-item {
    flex: 1;
    margin-right: 20px;

    &:last-child {
      margin-right: 0;
    }
  }
}

.full-width {
  flex-direction: column;

  .el-form-item {
    flex: 1;
    margin-right: 0;
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.form-footer {
  text-align: right;
  margin-top: 30px;
}
</style>
