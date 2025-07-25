<template>
  <el-container>
    <el-main style="background: #f8f9fa; padding: 20px;">
      <el-card body-style="padding: 30px;">
        <h1 style="font-size: 25px; font-weight: 500; margin-bottom: 20px; text-align: center; color: #1890ff;">
          收文详情
        </h1>

        <!-- 基础信息 -->
        <h3 style="font-size: 18px; font-weight: 500; margin-bottom: 16px;">基础信息</h3>
        <el-descriptions title="" border :column="3" style="margin-bottom: 24px;">
          <!-- 第一行 -->
          <el-descriptions-item label="来文类型">
            <el-select v-model="doc.docType" placeholder="请选择来文类型">
              <!-- 动态加载来文类型 -->
              <el-option
                  v-for="type in docTypes"
                  :key="type.id"
                  :label="type.name"
                  :value="type.name"
              ></el-option>
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="来文日期">
            <el-date-picker
                v-model="doc.receiveDate"
                type="date"
                placeholder="选择日期"
                value-format="timestamp"
                size="mini"
                style="width: 100%;">
            </el-date-picker>
          </el-descriptions-item>
          <el-descriptions-item label="来文单位">
            <el-input v-model="doc.senderOrg" placeholder="请输入来文单位" size="mini"></el-input>
          </el-descriptions-item>

          <!-- 第二行 -->
          <el-descriptions-item label="联系人">
            <el-input v-model="doc.contactPerson" placeholder="请输入联系人" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            <el-input v-model="doc.contactPhone" placeholder="请输入联系电话" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="密级">
            <el-select v-model="doc.secretLevel" placeholder="请选择密级" size="mini" style="width: 100%;">
              <el-option label="普通" value="普通"></el-option>
              <el-option label="秘密" value="秘密"></el-option>
              <el-option label="机密" value="机密"></el-option>
              <el-option label="绝密" value="绝密"></el-option>
            </el-select>
          </el-descriptions-item>

          <!-- 第三行 -->
          <el-descriptions-item label="紧急程度">
            <el-select v-model="doc.urgencyLevel" placeholder="请选择紧急程度" size="mini" style="width: 100%;">
              <el-option label="普通" value="普通"></el-option>
              <el-option label="紧急" value="紧急"></el-option>
              <el-option label="特急" value="特急"></el-option>
              <el-option label="特提" value="特提"></el-option>
            </el-select>
          </el-descriptions-item>
          <el-descriptions-item label="年度">
            <el-input v-model="doc.annual" placeholder="请输入年度" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="份数">
            <el-input v-model="doc.copies" type="number" min="1" size="mini"></el-input>
          </el-descriptions-item>

          <!-- 第四行 -->
          <el-descriptions-item label="文件页数">
            <el-input v-model="doc.pageCount" type="number" min="1" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="文件标题">
            <el-input v-model="doc.title" placeholder="请输入文件标题" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="文种/期号">
            <el-input v-model="doc.documentType" placeholder="请输入文种/期号" size="mini"></el-input>
          </el-descriptions-item>

          <!-- 第五行 -->
          <el-descriptions-item label="盒号">
            <el-input v-model="doc.boxNo" placeholder="请输入盒号" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="来文编号">
            <el-input v-model="doc.senderDocNo" placeholder="请输入来文编号" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="限时反馈">
            <el-checkbox v-model="doc.feedbackRequired"></el-checkbox>
          </el-descriptions-item>

          <!-- 第六行 -->
          <el-descriptions-item label="处理类型">
            <el-input v-model="doc.processType" placeholder="请输入处理类型" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="归档号">
            <el-input v-model="doc.archiveNo" placeholder="请输入归档号" size="mini"></el-input>
          </el-descriptions-item>
          <el-descriptions-item label="归档日期">
            <el-date-picker
                v-model="doc.archiveDate"
                type="date"
                placeholder="选择日期"
                value-format="timestamp"
                size="mini"
                style="width: 100%;">
            </el-date-picker>
          </el-descriptions-item>

          <!-- 第七行 -->
          <el-descriptions-item label="密件类型">
<!--            <el-radio-group v-model="doc.secretType" size="mini">-->
<!--              <el-radio :label="1">密件</el-radio>-->
<!--              <el-radio :label="0">普通件</el-radio>-->
<!--            </el-radio-group>-->
            {{ doc.secretType === 1 ? '密件' : '普通件' }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
<!--            <el-select v-model="doc.status" placeholder="请选择状态" size="mini">-->
<!--              <el-option label="待审核" :value="0"></el-option>-->
<!--              <el-option label="流转中" :value="1"></el-option>-->
<!--              <el-option label="已存档" :value="2"></el-option>-->
<!--            </el-select>-->
            {{ getStatusText(doc.status) }}
          </el-descriptions-item>
          <el-descriptions-item label="办件单位">
            <el-input v-model="doc.handlingOrg" placeholder="请输入办件单位" size="mini"></el-input>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 保存基础信息按钮 -->
        <div style="text-align: center; margin: 20px 0;">
          <el-button type="primary" @click="saveDoc">保存基础信息</el-button>
          <el-button type="warning" @click="handlePrint">打印处理单</el-button>
        </div>

        <!-- 流转意见 -->
        <div style="margin-top: 24px;">
          <h3 style="font-size: 18px; font-weight: 500; margin-bottom: 16px;">审批流转记录</h3>
          <el-timeline>
            <!-- 审批意见时间线 - 可编辑模式 -->
            <el-timeline-item
                v-for="(opinion, index) in doc.opinions"
                :key="opinion.id || index"
                :timestamp="opinion.createDate"
                :type="getTimelineType(opinion.level)"
            >
              <div style="padding: 20px; background: #f8f9fa; border-radius: 8px;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                  <div style="font-size: 16px; font-weight: 500;">
                    {{ getOpinionTitle(opinion.level) }}
                  </div>
                  <div>
                    <el-button
                        type="text"
                        size="mini"
                        icon="el-icon-edit"
                        @click="editOpinion(index)">
                      编辑
                    </el-button>
                    <el-button
                        type="text"
                        size="mini"
                        icon="el-icon-delete"
                        @click="removeOpinion(index)">
                      删除
                    </el-button>
                  </div>
                </div>

                <el-input
                    v-if="editingIndex === index"
                    type="textarea"
                    v-model="editingOpinion.opinion"
                    rows="3"
                    style="margin-bottom: 10px;"
                ></el-input>
                <p v-else style="color: #606266;">
                  {{ opinion.opinion || '无具体意见' }}
                </p>

                <div style="display: flex; justify-content: space-between; flex-wrap: wrap;">
                  <div style="font-size: 12px; color: #909399;">
                    处理人：
                    <el-select
                        v-if="editingIndex === index"
                        v-model="editingOpinion.objectId"
                        placeholder="请选择处理人"
                        size="mini"
                        style="width: 200px; margin-left: 5px;">
                      <el-option
                          v-for="obj in flowObjects"
                          :key="obj.id"
                          :label="`${obj.name} (${obj.position})`"
                          :value="obj.id"
                      />
                    </el-select>
                    <template v-else>
                      {{ opinion.name || '未知' }} ({{ opinion.position || '无职位' }})
                    </template>
                  </div>

                  <div style="font-size: 12px; color: #909399;">
                    创建日期：
                    <el-date-picker
                        v-if="editingIndex === index"
                        v-model="editingOpinion.createDate"
                        type="datetime"
                        value-format="timestamp"
                        size="mini"
                        style="width: 200px; margin-left: 5px;">
                    </el-date-picker>
                    <template v-else>
                      {{ opinion.createDate | datetimeFormat }}
                    </template>
                  </div>
                </div>

                <div style="font-size: 12px; color: #909399; margin-top: 8px;">
                  录入人：
                  <el-select
                      v-if="editingIndex === index"
                      v-model="editingOpinion.recorderId"
                      placeholder="请选择录入人"
                      size="mini"
                      style="width: 200px; margin-left: 5px;">
                    <el-option
                        v-for="obj in flowObjects"
                        :key="obj.id"
                        :label="obj.name"
                        :value="obj.id"
                    />
                  </el-select>
                  <template v-else>
                    {{ opinion.recorderName || '系统自动录入' }}
                  </template>
                </div>

                <!-- 编辑模式按钮 -->
                <div v-if="editingIndex === index" style="margin-top: 15px; text-align: right;">
                  <el-button size="mini" @click="cancelEdit">取消</el-button>
                  <el-button type="primary" size="mini" @click="saveOpinionEdit(index)">保存</el-button>
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

        <!-- 填写新流转意见 -->
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

            <el-form-item label="意见等级" prop="level">
              <el-select v-model="newOpinion.level" placeholder="请选择意见等级" style="width: 100%;">
                <el-option label="主要领导意见" :value="1"></el-option>
                <el-option label="分管领导意见" :value="2"></el-option>
                <el-option label="部门意见" :value="3"></el-option>
                <el-option label="办公室拟办意见" :value="4"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="流转意见" prop="opinion">
              <el-input type="textarea" v-model="newOpinion.opinion" rows="3" placeholder="请输入流转意见" style="width: 100%;"/>
            </el-form-item>

            <el-form-item label="录入人" prop="recorderId">
              <el-select v-model="newOpinion.recorderId" placeholder="请选择录入人" style="width: 100%;">
                <el-option
                    v-for="obj in flowObjects"
                    :key="obj.id"
                    :label="obj.name"
                    :value="obj.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="创建时间" prop="createDate">
              <el-date-picker
                  v-model="newOpinion.createDate"
                  type="datetime"
                  placeholder="选择日期时间"
                  value-format="timestamp"
                  style="width: 100%;">
              </el-date-picker>
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
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-main>

    <!-- 编辑意见的弹窗 -->
    <el-dialog :visible.sync="showEditDialog" title="编辑流转意见" width="600px">
      <el-form :model="editingOpinion">
        <el-form-item label="意见内容">
          <el-input type="textarea" v-model="editingOpinion.opinion" rows="4"></el-input>
        </el-form-item>
        <el-form-item label="处理人">
          <el-select v-model="editingOpinion.objectId" placeholder="请选择处理人">
            <el-option
                v-for="obj in flowObjects"
                :key="obj.id"
                :label="`${obj.name} (${obj.position})`"
                :value="obj.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="录入人">
          <el-select v-model="editingOpinion.recorderId" placeholder="请选择录入人">
            <el-option
                v-for="obj in flowObjects"
                :key="obj.id"
                :label="obj.name"
                :value="obj.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
              v-model="editingOpinion.createDate"
              type="datetime"
              placeholder="选择日期时间"
              value-format="timestamp">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveOpinionEdit">保存</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
export default {
  name: "IncomingDocDetail",
  data() {
    return {
      docTypes: '',
      doc: {
        opinions: []
      },
      flowObjects: [],
      newOpinion: {
        docId: null,
        objectId: null,
        level: 1,
        opinion: '',
        recorderId: null,
        nextObjectId: null,
        createDate: new Date().getTime()
      },
      editingIndex: -1,
      editingOpinion: {
        id: null,
        docId: null,
        objectId: null,
        name: '',
        position: '',
        level: 1,
        opinion: '',
        recorderId: null,
        recorderName: '',
        nextObjectId: null,
        createDate: new Date().getTime()
      },
      showEditDialog: false
    };
  },
  mounted() {
    this.loadDoc();
    this.loadFlowObjects();
    this.loadDocTypes();
  },
  computed: {
    // 加载来文类型
    loadDocTypes() {
      this.$request.get('/docType/all').then(res => {
        if (res.code === '200') {
          this.docTypes = res.data;
        }
      });
    },
    getTimelineType() {
      return (level) => {
        const typeMap = { 1: 'primary', 2: 'success', 3: 'info', default: 'default' };
        return typeMap[level] || typeMap.default;
      };
    },
    getOpinionTitle() {
      return (level) => {
        const titleMap = {
          1: '主要领导意见：',
          2: '分管领导意见：',
          3: '部门意见：',
          4: '办公室拟办意见：'
        };
        return titleMap[level] || titleMap[4];
      };
    },
    currentHandler() {
      const lastOpinion = this.doc.opinions[this.doc.opinions.length - 1];
      return lastOpinion && lastOpinion.nextObjectId
          ? this.getNextHandler(lastOpinion.nextObjectId)
          : '无后续流转';
    },
    hasCurrentHandler() {
      const lastOpinion = this.doc.opinions[this.doc.opinions.length - 1];
      return lastOpinion && lastOpinion.nextObjectId;
    }
  },
  filters: {
    dateFormat(date) {
      if (!date) return '无';
      const d = new Date(date);
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    },
    datetimeFormat(date) {
      if (!date) return '无';
      const d = new Date(date);
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}
              ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
    }
  },
  methods: {
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
      this.$request.get(`/incomingDoc/${id}`)
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
              // 设置默认录入人（当前用户）
              const currentUserId = localStorage.getItem('userId');
              if (currentUserId) {
                this.newOpinion.recorderId = currentUserId;
                this.editingOpinion.recorderId = currentUserId;
              }
            } else {
              this.$message.error('加载流转对象失败：' + res.msg);
            }
          })
          .catch(err => {
            console.error('加载流转对象请求失败:', err);
            this.$message.error('网络请求失败，请重试');
          });
    },

    saveDoc() {
      this.$request.put('/incomingDoc/update', this.doc)
          .then(res => {
            if (res.code === '200') {
              this.$message.success('文档信息更新成功');
              this.loadDoc(); // 重新加载文档以确保数据一致性
            } else {
              this.$message.error('更新失败：' + res.msg);
            }
          })
          .catch(err => {
            console.error('更新文档失败:', err);
            this.$message.error('网络请求失败，请重试');
          });
    },

    handlePrint() {
      // 打印功能不变
      const id = this.$route.params.id;
      const tpl_name = "收发文新系统.ureport.xml";
      const pageIndex = 1;     // 页码
      const renderOption = 1;  // 渲染选项
      const url = `http://localhost:9090/ureport/preview?_u=mysql:${tpl_name}&_i=${pageIndex}&_r=${renderOption}&ids=${id}`;
      window.open(url, '_blank');
    },

    editOpinion(index) {
      this.editingIndex = index;
      const opinion = this.doc.opinions[index];
      // 创建编辑副本
      this.editingOpinion = {
        id: opinion.id,
        docId: opinion.docId,
        objectId: opinion.objectId,
        name: opinion.name,
        position: opinion.position,
        level: opinion.level,
        opinion: opinion.opinion,
        recorderId: opinion.recorderId,
        recorderName: opinion.recorderName,
        nextObjectId: opinion.nextObjectId,
        createDate: opinion.createDate
      };
    },

    cancelEdit() {
      this.editingIndex = -1;
    },

    saveOpinionEdit(index) {
      const originalOpinion = this.doc.opinions[index];

      // 更新原始意见
      Object.assign(originalOpinion, this.editingOpinion);

      // 更新关联的处理人信息
      const flowObj = this.flowObjects.find(o => o.id === this.editingOpinion.objectId);
      if (flowObj) {
        originalOpinion.name = flowObj.name;
        originalOpinion.position = flowObj.position;
      }

      // 更新录入人名称
      const recorder = this.flowObjects.find(o => o.id === this.editingOpinion.recorderId);
      if (recorder) {
        originalOpinion.recorderName = recorder.name;
      }

      // 调用API保存到数据库
      this.$request.put('/incomingOpinion/update', originalOpinion)
          .then(res => {
            if (res.code === '200') {
              this.$message.success('意见修改成功');
              this.editingIndex = -1;
            } else {
              this.$message.error('意见修改失败：' + res.msg);
            }
          })
          .catch(err => {
            console.error('修改意见失败:', err);
            this.$message.error('网络请求失败，请重试');
          });
    },

    removeOpinion(index) {
      this.$confirm('确定要删除这条意见吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const opinion = this.doc.opinions[index];
        if (opinion.id) {
          this.$request.delete(`/incomingOpinion/delete/${opinion.id}`)
              .then(res => {
                if (res.code === '200') {
                  this.doc.opinions.splice(index, 1);
                  this.$message.success('意见删除成功');
                } else {
                  this.$message.error('删除失败：' + res.msg);
                }
              })
              .catch(err => {
                console.error('删除意见失败:', err);
                this.$message.error('网络请求失败，请重试');
              });
        } else {
          // 本地新增的尚未保存的意见
          this.doc.opinions.splice(index, 1);
          this.$message.success('意见删除成功');
        }
      });
    },

    saveOpinion() {
      // 验证必要字段
      if (!this.newOpinion.objectId) {
        this.$message.warning('请选择流转对象');
        return;
      }

      // 设置关联的用户信息
      const flowObj = this.flowObjects.find(o => o.id === this.newOpinion.objectId);
      if (flowObj) {
        this.newOpinion.name = flowObj.name;
        this.newOpinion.position = flowObj.position;
      }

      // 设置录入人名称
      const recorder = this.flowObjects.find(o => o.id === this.newOpinion.recorderId);
      if (recorder) {
        this.newOpinion.recorderName = recorder.name;
      } else {
        this.newOpinion.recorderName = '系统自动录入';
      }

      // 添加到本地文档中（优化响应速度）
      const newOp = JSON.parse(JSON.stringify(this.newOpinion));
      this.doc.opinions.push(newOp);

      // 保存到数据库
      this.$request.post('/incomingOpinion/add', this.newOpinion)
          .then(res => {
            if (res.code === '200') {
              this.$message.success('意见提交成功');
              // 更新本地数据的ID
              const addedOpinion = res.data;
              const latestOpinion = this.doc.opinions[this.doc.opinions.length - 1];
              latestOpinion.id = addedOpinion.id;

              // 重置表单
              this.resetOpinionForm();
            } else {
              this.$message.error('提交失败：' + res.msg);
              // 移除本地添加的意见
              this.doc.opinions.pop();
            }
          })
          .catch(err => {
            console.error('提交意见请求失败:', err);
            this.$message.error('网络请求失败，请重试');
            // 移除本地添加的意见
            this.doc.opinions.pop();
          });
    },

    resetOpinionForm() {
      this.newOpinion = {
        docId: this.doc.id,
        objectId: null,
        level: 1,
        opinion: '',
        recorderId: this.newOpinion.recorderId || null, // 保留录入人
        nextObjectId: null,
        createDate: new Date().getTime()
      };
      this.$refs.opinionForm.resetFields();
    }
  }
};
</script>

<style scoped>
.el-input, .el-select, .el-date-picker {
  width: 100%;
}
</style>
