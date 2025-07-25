<template>
  <el-container>
    <el-main style="background: #f8f9fa; padding: 20px;">
      <el-card body-style="padding: 30px;">
        <h1 style="font-size: 25px; font-weight: 500; margin-bottom: 20px; text-align: center; color: #1890ff;">
          发文详情
        </h1>
        <!-- 基础信息 -->
        <h3 style="font-size: 18px; font-weight: 500; margin-bottom: 16px;">基础信息</h3>
        <el-descriptions title="" border :column="3" style="margin-bottom: 24px;">
          <!-- 第一行 -->
          <el-descriptions-item label="发文日期">{{ doc.createDate | dateFormat }}</el-descriptions-item>
          <el-descriptions-item label="发文单位">{{ doc.senderOrg || '无' }}</el-descriptions-item>

          <!-- 第二行 -->
          <el-descriptions-item label="密级">{{ doc.secretLevel || '普通' }}</el-descriptions-item>

          <!-- 第三行 -->
          <el-descriptions-item label="紧急程度">{{ doc.urgencyLevel || '普通' }}</el-descriptions-item>

          <!-- 第四行 -->
          <el-descriptions-item label="文件页数">{{ doc.pageCount || '无' }}</el-descriptions-item>
          <el-descriptions-item label="文件标题">{{ doc.title || '无' }}</el-descriptions-item>

          <!-- 第五行 -->
          <el-descriptions-item label="来文编号">{{ doc.senderDocNo || '无' }}</el-descriptions-item>
          <el-descriptions-item label="限时反馈">{{ doc.feedbackRequired ? '是' : '否' }}</el-descriptions-item>

          <!-- 第六行 -->
          <el-descriptions-item label="处理类型">{{ doc.processType || '无' }}</el-descriptions-item>
          <el-descriptions-item label="归档号">{{ doc.recordNo || '无' }}</el-descriptions-item>
          <el-descriptions-item label="归档日期">{{ doc.archiveDate | dateFormat }}</el-descriptions-item>

          <!-- 第七行 -->
          <el-descriptions-item label="密件类型">{{ doc.secretType === 1 ? '密件' : '普通件' }}</el-descriptions-item>
          <el-descriptions-item label="当前状态">{{ getStatusText(doc.status) }}</el-descriptions-item>
          <el-descriptions-item label="办件单位">{{ doc.handlingOrg || '无' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 流转意见 -->
        <div style="margin-top: 24px;">
          <h3 style="font-size: 18px; font-weight: 500; margin-bottom: 16px;">审批流转记录</h3>
          <el-timeline>
            <!-- 审批意见时间线 -->
            <el-timeline-item
                v-for="(opinion, index) in doc.opinions"
                :key="opinion.id || index"
                :timestamp="opinion.createDate"
                :type="getTimelineType(opinion.level)"
            >
              <div style="padding: 20px; background: #f8f9fa; border-radius: 8px;">
                <div style="font-size: 16px; font-weight: 500; margin-bottom: 10px;">
                  {{ getOpinionTitle(opinion.level) }}
                </div>
                <p style="color: #606266;">{{ opinion.opinion || '无具体意见' }}</p>
                <div style="font-size: 12px; color: #909399; margin-top: 8px;">
                  处理人：{{ opinion.name || '未知' }} ({{ opinion.position || '无职位' }})
                </div>
                <div style="font-size: 12px; color: #909399; margin-top: 8px;">
                  录入人：{{ opinion.recorderName || '系统自动录入' }}
                </div>
              </div>
            </el-timeline-item>

            <!-- 当前处理人独立时间线节点 -->
            <el-timeline-item
                v-if="hasCurrentHandler"
                :timestamp="''"
                type="warning"
                style="margin-top: 10px;"
            >
              <div style="padding: 20px; background: #f8f9fa; border-radius: 8px; display: flex; align-items: center;">
                <div style="font-size: 16px; font-weight: 500; color: #d63031;">
                  当前流转至：{{ currentHandler }}
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <!-- 填写流转意见 -->
        <div style="margin-top: 24px; padding-top: 24px; border-top: 1px solid #ebeef5;">
          <h3 style="font-size: 18px; font-weight: 500; margin-bottom: 16px;">填写流转意见</h3>
          <el-form :model="newOpinion" ref="opinionForm" label-width="100px">
            <el-form-item label="选择流转对象" prop="objectId">
              <el-select v-model="newOpinion.objectId" placeholder="请选择流转对象" style="width: 100%;">
                <el-option
                    v-for="obj in flowObjects"
                    :key="obj.id"
                    :label="`${obj.name} (${obj.position})`"
                    :value="obj.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="流转意见" prop="opinion">
              <el-input type="textarea" v-model="newOpinion.opinion" rows="3" placeholder="请输入流转意见" style="width: 100%;"/>
            </el-form-item>

            <el-form-item label="下一流转对象" prop="nextObjectId">
              <el-select v-model="newOpinion.nextObjectId" placeholder="可选" style="width: 100%;">
                <el-option
                    v-for="obj in flowObjects"
                    :key="obj.id"
                    :label="`${obj.name} (${obj.position})`"
                    :value="obj.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveOpinion">提交意见</el-button>
              <el-button type="warning" @click="handlePrint()">打印处理单</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "IncomingDocDetail",
  data() {
    return {
      doc: {},
      flowObjects: [],
      newOpinion: {
        docId: null,
        objectId: null,
        opinion: '',
        nextObjectId: null
      }
    };
  },
  mounted() {
    this.loadDoc();
    this.loadFlowObjects();
  },
  computed: {
    getTimelineType() {
      return (level) => {
        const typeMap = { 1: 'primary', 2: 'success', 3: 'info', default: 'default' };
        return typeMap[level] || typeMap.default;
      };
    },
    getOpinionTitle() {
      return (level) => {
        const titleMap = { 1: '主要领导意见：', 2: '分管领导意见：', 3: '部门意见：', default: '办公室拟办意见：' };
        return titleMap[level] || titleMap.default;
      };
    },
    currentHandler() {
      const lastOpinion = this.doc.opinions[this.doc.opinions.length - 1];
      return lastOpinion.nextObjectId
          ? this.getNextHandler(lastOpinion.nextObjectId)
          : '无后续流转';
    },
    hasCurrentHandler() {
      const lastOpinion = this.doc.opinions[this.doc.opinions.length - 1];
      return !!lastOpinion.nextObjectId;
    }
  },
  filters: {
    // 日期格式化过滤器（示例：2024-06-15）
    dateFormat(date) {
      if (!date) return '无';
      const d = new Date(date);
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }
  },
  methods: {
    // 状态文本转换（根据业务常量）
    getStatusText(status) {
      const statusMap = {
        0: '待审核',
        1: '流转中',
        2: '已存档'
      };
      return statusMap[status] || '未知状态';
    },

    getNextHandler(nextObjectId) {
      if (!nextObjectId) return '无后续流转';
      const obj = this.flowObjects.find(o => o.id === nextObjectId);
      return obj ? `${obj.name} (${obj.position})` : '未知处理人';
    },

    loadDoc() {
      const id = this.$route.params.id;
      if (!id) {
        this.$message.error('文档ID不存在');
        return;
      }
      this.$request.get(`/outgoingDoc/${id}`)
          .then(res => {
            if (res.code === '200') {
              this.doc = res.data;
              this.newOpinion.docId = id;
            } else {
              this.$message.error('加载文档失败：' + res.msg);
            }
          })
          .catch(err => {
            console.error('加载文档请求失败:', err);
            this.$message.error('网络请求失败，请重试');
          });
    },

    loadFlowObjects() {
      this.$request.get('/flowObject/all')
          .then(res => {
            if (res.code === '200') {
              this.flowObjects = res.data;
            } else {
              this.$message.error('加载流转对象失败：' + res.msg);
            }
          })
          .catch(err => {
            console.error('加载流转对象请求失败:', err);
            this.$message.error('网络请求失败，请重试');
          });
    },

    handlePrint() {
      const id = this.$route.params.id;
      const tpl_name = "收发文新系统.ureport.xml";
      const pageIndex = 1;     // 页码
      const renderOption = 1;  // 渲染选项
      const url = `http://localhost:9090/ureport/preview?_u=mysql:${tpl_name}&_i=${pageIndex}&_r=${renderOption}&ids=${id}`;
      window.open(url, '_blank');
    },

    saveOpinion() {
      this.$refs.opinionForm.validate(valid => {
        if (!valid) return;
        this.$request.post('/outgoingOpinion/add', this.newOpinion)
            .then(res => {
              if (res.code === '200') {
                this.$message.success('意见提交成功');
                this.loadDoc();
                this.newOpinion = { docId: this.doc.id };
              } else {
                this.$message.error('提交失败：' + res.msg);
              }
            })
            .catch(err => {
              console.error('提交意见请求失败:', err);
              this.$message.error('网络请求失败，请重试');
            });
      });
    }
  }
};
</script>

<style scoped>

</style>
