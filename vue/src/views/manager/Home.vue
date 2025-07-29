<template>
  <div class="home-container">
    <!-- 数据概览卡片 -->
    <div class="statistic-container">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(item, index) in statistics" :key="index">
          <el-card class="statistic-card" :body-style="{ padding: '20px' }">
            <div class="statistic-content" style="flex-direction: row; justify-content: space-between; align-items: center;">
              <!-- 左侧内容区域 -->
              <div class="statistic-left">
                <div class="statistic-header">
                  <span class="title">{{ item.title }}</span>
                  <span v-if="item.percent" class="percent">{{ item.percent }}</span>
                </div>
                <div class="number">{{ item.value }}</div>
                <div class="sub-text">
                  {{ item.subTitle }} <span :style="{ color: item.fontcolor }" >{{ item.subValue }}</span>
                </div>
              </div>
              <!-- 右侧图标区域 -->
              <div class="icon-container" :style="{ backgroundColor: item.color }">
                <img :src="item.iconClass" alt="统计图标" class="custom-icon">
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作区 -->
    <div class="quick-ops">
      <h3 class="section-title">快速操作</h3>
      <el-row :gutter="20">
        <el-col :span="4" v-for="(op, index) in quickOperations" :key="index">
          <!-- 移除错误的hover-style属性，改为CSS悬停 -->
          <el-card
              class="op-card"
              @click.native="handleOpClick(op.route)"
              :style="{ borderColor: op.hoverColor }"
              @mouseenter="handleOpHover(index)"
              @mouseleave="handleOpLeave(index)"
          >
            <div class="op-icon" :style="{  backgroundColor: op.hoverColor, color: op.color }">
              <i :class="op.iconClass" style="pointer-events: none; display: flex; align-items: center; justify-content: center;" ></i>
            </div>
            <span class="op-text" :style="{ color: op.color }">{{ op.text }}</span>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 待审批文件 -->
    <div class="pending-approval">
      <h3 class="section-title">待审批文件</h3>
      <el-table
          :data="pendingDocs"
          stripe
          style="width: 100%; margin-top: 20px"
      >
        <el-table-column prop="title" label="文件标题" width="200"></el-table-column>
        <el-table-column prop="docNo" label="文号" width="150"></el-table-column>
        <el-table-column prop="type" label="类型" width="100"></el-table-column>
        <el-table-column prop="urgency" label="紧急程度" width="120">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.urgencyLevel)">{{ scope.row.urgencyLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="发起时间" width="180"></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="mini" type="primary" @click="handleApprove(scope.row.id)">审批</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          :current-page="pageNum"
          :page-size="10"
          :total="totalPending"
          @current-change="handlePageChange"
          style="margin-top: 20px"
      ></el-pagination>
    </div>

    <!-- 新增的提醒弹窗 -->
    <el-dialog
        title="提醒信息"
        :visible.sync="reminderDialogVisible"
        width="30%"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
    >
      <template #content>
        <p>{{ reminderMessage }}</p>
      </template>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reminderDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { connectWebSocket } from "@/utils/websocket"
export default {
  name: "Home",
  mounted() {
    // 初始化 WebSocket 连接，并传入处理消息的回调函数
    connectWebSocket((message) => {
      this.showReminder(message);
    });
  },
  data() {
    return {
      statistics: [
        {
          title: "本月发文",
          value: "0",
          subTitle: "今日发文",
          subValue: "0",
          iconClass: require('@/assets/imgs/home/outgoing.png'),
          color: "#165DFF19",
          fontcolor: "#165dff"
        },
        {
          title: "本月收文",
          value: "0",
          subTitle: "今日收文",
          subValue: "0",
          iconClass: require('@/assets/imgs/home/incoming.png'),
          color: "#fff5ec",
          fontcolor: "#ff9f43"
        },
        {
          title: "待审批文件",
          value: "0",
          subTitle: "紧急文件",
          subValue: "0",
          iconClass: require('@/assets/imgs/home/approval.png'),
          color: "#FF525219",
          fontcolor: "#ff5252"
        },
        {
          title: "已归档文件",
          value: "0",
          subTitle: "本年度",
          subValue: "0",
          iconClass: require('@/assets/imgs/home/archive.png'),
          color: "#36D39919",
          fontcolor: "#36d399"
        }
      ],
      quickOperations: [
        {
          text: "普通收文登记",
          iconClass: "el-icon-document-add",
          color: "#165DFF",
          hoverColor: "#165DFF1A",
          route: "/incoming-doc-register/0"
        },
        {
          text: "密件收文登记",
          iconClass: "el-icon-document-add",
          color: "#FF9F43",
          hoverColor: "#FF9F431A",
          route: "/incoming-doc-register/1"
        },
        {
          text: "普通发文登记",
          iconClass: "el-icon-edit-outline",
          color: "#36D399",
          hoverColor: "#36D3991A",
          route: "/outgoing-doc-register/0" // 假设发文登记路由（需根据实际后端路由调整）
        },
        {
          text: "密件发文登记",
          iconClass: "el-icon-edit-outline",
          color: "#FF5252",
          hoverColor: "#FF52521A",
          route: "/outgoing-doc-register/1" // 假设发文登记路由（需根据实际后端路由调整）
        },
        {
          text: "普通收文列表",
          iconClass: "el-icon-check",
          color: "#7289DA",
          hoverColor: "#7289DA1A",
          route: "/incoming-doc-normal/0"
        },
        {
          text: "密件收文列表",
          iconClass: "el-icon-check",
          color: "#9966FF",
          hoverColor: "#9966FF1A",
          route: "/incoming-doc-secret/1"
        }
      ],
      pendingDocs: [],
      pageNum: 1,
      totalPending: 0,
      // 查询条件对象
      query: {
        title: '',
        senderOrg: '',
        secretLevel: '',
        urgencyLevel: '',
        receiveDateRange: []
      },
      reminderDialogVisible: false,
      reminderMessage: '',
    };
  },
  created() {
    this.loadStatistics();
    this.loadPendingDocs();
  },
  methods: {
    getTagType(urgencyLevel) {
      const urgencyMap = {
        '特提': 'danger',
        '特急': 'danger',
        '加急': 'warning',
        '平急': 'info',
        '普通': 'success'
      };
      return urgencyMap[urgencyLevel] || 'info';
    },
    handleApprove(id) {
      this.$router.push(`/incoming-doc-detail/${id}`);
    },
    handlePageChange(page) {
      this.pageNum = page;
      this.loadPendingDocs();
    },
    loadStatistics() {
      this.$request.get("/home/statistics").then(res => {
        if (res.code === '200') {
          const apiStatistics = res.data.statistics;
          this.statistics.forEach((item, index) => {
            const apiItem = apiStatistics[index];
            item.title = apiItem.title || item.title;
            item.value = apiItem.value || item.value;
            item.subTitle = apiItem.subTitle || item.subTitle;
            item.subValue = apiItem.subValue || item.subValue;
          });
        }
      });
    },
    loadPendingDocs(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get("/incomingDoc/selectPage", {
        params: {
          pageNum: this.pageNum,
          pageSize: 10,
          ...this.query
        }
      }).then(res => {
        if (res.code === '200') {
          this.pendingDocs = res.data.list;
          this.totalPending = res.data.total;
        }
      });
    },
    // 点击快速操作项跳转路由
    handleOpClick(route) {
      console.log('跳转路由:', route); // 检查控制台是否输出正确路径
      this.$router.push(route);
    },

    // 可选：添加悬停动画（如果需要）
    handleOpHover(index) {
      const icon = this.$el.querySelectorAll('.op-icon')[index];
      icon.style.transform = 'scale(1.1)';
    },
    handleOpLeave(index) {
      const icon = this.$el.querySelectorAll('.op-icon')[index];
      icon.style.transform = 'scale(1)';
    },
    // 重置查询条件
    resetQuery() {
      this.query = {
        title: '',
        senderOrg: '',
        secretLevel: '',
        urgencyLevel: '',
        receiveDateRange: []
      };
      this.loadPendingDocs(1);  // 重置后重新加载第一页
    },
    showReminder(message) {
      // 使用 Element UI 的消息框来显示提醒信息
      this.reminderMessage = message;
      this.reminderDialogVisible = true;
    }
  }
};
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f8f9fa;
}

.statistic-container {
  margin-bottom: 30px;
}

.statistic-card {
  height: 155px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.statistic-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.title {
  font-size: 16px;
  font-weight: 500;
  color: #64748B;
}

.percent {
  font-size: 12px;
  color: #165DFF;
  background-color: #165DFF/10;
  padding: 2px 8px;
  border-radius: 20px;
}

.statistic-content {
  flex-direction: row !important;
  justify-content: space-between !important;
  align-items: center !important;
  align-items: flex-end !important;
}

.statistic-left {
  max-width: calc(100% - 80px);
}

.icon-container {
  width: 60px;
  height: 60px;
}

.custom-icon {
  width: 30px;
  height: 30px;
  object-fit: contain; /* 保持图片比例 */
}

.statistic-content {
  flex-grow: 1;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.number {
  font-size: 28px;
  font-weight: normal;
  color: #1E293B;
}

.sub-text {
  font-size: 14px;
  color: #64748B;
}

.icon-container {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
}

.quick-ops {
  margin-bottom: 30px;
}

.section-title {
  font-size: 20px;
  font-weight: 500;
  margin-bottom: 20px;
  color: #1E293B;
}

.pending-approval {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.quick-ops {
  margin: 30px 0;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.op-text {
  font-size: 14px;
  font-weight: 500;
  text-align: center;
  line-height: 1.4;
}

.op-card {
  height: 160px;
  border: 1px solid #f0f2f5;
  border-radius: 12px;
  transition: all 0.3s ease; /* 保留过渡动画 */
  cursor: pointer;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.op-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

.op-icon {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  margin-bottom: 12px;
  transition: transform 0.3s ease;
}

.op-icon i {
  width: 100%; /* 继承父容器宽度 */
  height: 100%; /* 继承父容器高度 */
}

.op-icon, .op-text {
  pointer-events: none; /* 允许点击穿透到el-card */
}

/* 优化搜索区域样式 */
.search-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
}
</style>
