<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">坤鹏智慧公文管理系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar"/>
            <div>{{ user.name ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体 -->
    <div class="manager-main">
      <!-- 侧边栏 -->
      <div class="manager-main-left">
        <el-menu :default-openeds="[]" router style="border: none" :default-active="$route.path">
          <!-- 系统首页 -->
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

          <!-- 收文管理 -->
          <el-submenu index="incoming-doc" v-if="hasRole(['ADMIN', 'DOCADMIN'])">
            <template slot="title">
              <i class="el-icon-document"></i><span>收文管理</span>
            </template>
            <el-menu-item index="/incoming-doc-normal/0">普通件管理</el-menu-item>
            <el-menu-item index="/incoming-doc-secret/1">密件管理</el-menu-item>
          </el-submenu>

          <!-- 发文管理 -->
          <el-submenu index="outgoing-doc" v-if="hasRole(['ADMIN', 'DOCADMIN'])">
            <template slot="title">
              <i class="el-icon-edit"></i><span>发文管理</span>
            </template>
            <el-menu-item index="/outgoing-doc-normal/0">普通件管理</el-menu-item>
            <el-menu-item index="/outgoing-doc-secret/1">密件管理</el-menu-item>
          </el-submenu>

          <!-- 档案管理
          <el-submenu index="archive" v-if="hasRole(['ADMIN', 'DOCADMIN'])">
            <template slot="title">
              <i class="el-icon-folder"></i><span>档案管理</span>
            </template>
            <el-menu-item index="/archive-manage">存档管理</el-menu-item>
          </el-submenu>-->

          <!-- 系统管理 -->
          <el-submenu index="system" v-if="hasRole(['ADMIN', 'DOCADMIN'])">
            <template slot="title">
              <i class="el-icon-setting"></i><span>系统管理</span>
            </template>
            <el-menu-item index="/admin">用户管理</el-menu-item>
            <el-menu-item index="/flow-object-list">流转对象管理</el-menu-item>
            <el-menu-item index="/doc-type-list">来文类型管理</el-menu-item>
            <el-menu-item index="/reminder-settings-list">提醒设置</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!-- 数据表格 -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      scanBuffer: '', // 扫码输入缓冲区
      lastKeyTime: 0, // 上次按键时间（用于判断是否为快速连续输入）
    }
  },
  mounted() {
    // 挂载全局键盘监听
    window.addEventListener('keyup', this.handleScanKeyup);
  },
  beforeDestroy() {
    // 卸载时移除监听（避免内存泄漏）
    window.removeEventListener('keyup', this.handleScanKeyup);
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    // 键盘事件处理函数
    handleScanKeyup(event) {
      const { key, target } = event;
      const now = Date.now();
      // 过滤以下情况：
    // 1. 当前焦点在输入框/文本域中（避免干扰正常输入）
    // 2. 非数字或非Enter键（假设ID是数字）
    // 3. 两次按键间隔超过500ms（非扫码枪快速输入）
      if (
          (target.tagName === 'INPUT' || target.tagName === 'TEXTAREA') ||
          (!/^\d$/.test(key) && key !== 'Enter') ||
          (this.scanBuffer && now - this.lastKeyTime > 500)
      ) {
        this.scanBuffer = ''; // 重置缓冲区
        return;
      }
      if (key === 'Enter') {
        // 扫码完成，检查ID是否有效
        const docId = this.scanBuffer.trim();
        console.log('docId', docId)
        if (docId) {
          this.$router.push(`/incoming-doc-detail/${docId}`);
        }
        this.scanBuffer = ''; // 清空缓冲区
      } else {
    // 收集数字字符
        this.scanBuffer += key;
        this.lastKeyTime = now;
      }
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/adminPerson')
      }
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    },
    hasRole(roles) {
      return roles.includes(this.user.role.toUpperCase());
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>
