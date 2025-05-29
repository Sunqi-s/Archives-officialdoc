<template>
  <div class="login-container">
    <!-- 背景动画容器（新增ref） -->
    <div ref="vantaRef" class="vanta-background"></div>

    <!-- 背景光效 -->
    <div class="bg-effects">
      <div class="light-ball effect-1"></div>
      <div class="light-ball effect-2"></div>
      <div class="light-ball effect-3"></div>
    </div>

    <div class="login-card">
      <div class="system-header">
        <div class="title-group">
          <h1 class="main-title">坤鹏智慧公文管理系统</h1>
          <p class="sub-title">党政机关电子公文协同管理平台</p>
        </div>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              placeholder="请输入账号"
              prefix-icon="el-icon-user"
              class="login-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              prefix-icon="el-icon-lock"
              class="login-input"
          />
        </el-form-item>

        <el-checkbox v-model="form.rememberMe" class="remember-me">记住密码</el-checkbox>

        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              @click="login"
              :loading="loginLoading"
          >
            立即登录
          </el-button>
        </el-form-item>

        <div class="footer-info">
          <p>©2025 大连长兴岛经济区管委会</p>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import * as THREE from 'three'
import BIRDS from '@/assets/back.js'  // 假设背景动画库路径正确

export default {
  name: "Login",
  data() {
    return {
      form: {
        username: "",
        password: "",
        rememberMe: false,
        role: 'ADMIN'
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
      loginLoading: false  // 登录按钮加载状态
    }
  },
  mounted() {
    // 初始化背景动画（修复：添加vantaRef）
    if (this.$refs.vantaRef) {
      this.vantaEffect = BIRDS({
        el: this.$refs.vantaRef,
        THREE: THREE,
        color: 0x165dff,
        backgroundAlpha: 0.1
      })
    }

    // 读取记住的密码（初始化时）
    const savedUser = localStorage.getItem('rememberedUser')
    if (savedUser) {
      const { username, password, rememberMe } = JSON.parse(savedUser)
      this.form = { username, password, rememberMe }
    }
  },
  beforeDestroy() {
    // 销毁背景动画
    if (this.vantaEffect) this.vantaEffect.destroy()
  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$router.push('/')  // 跳转主页
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  position: relative;
  min-height: 100vh;  /* 替换height为min-height，避免内容溢出 */
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(-45deg, #165DFF, #0A3FAF, #002B87, #001A52);
  background-size: 400% 400%;
  animation: gradientFlow 15s ease infinite;
  z-index: 2;
}

.bg-effects {
  position: absolute;
  inset: 0;
  pointer-events: none;
  opacity: 0.2;
}

.light-ball {
  position: absolute;
  border-radius: 50%;
  background: rgba(255,255,255,0.1);
  filter: blur(40px);
}

.effect-1 { width: 160px; height: 160px; animation: floatBall 10s infinite linear; left: 20%; top: 30%; }
.effect-2 { width: 240px; height: 240px; animation: floatBall 12s infinite linear; left: 70%; top: 60%; }
.effect-3 { width: 120px; height: 120px; animation: floatBall 8s infinite linear; left: 50%; top: 10%; }

.login-card {
  background: rgba(255,255,255,0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 40px;
  width: min(90%, 650px);  /* 响应式：最大650px，小屏幕下90%宽度 */
  max-height: 90vh;       /* 限制最大高度 */
  overflow-y: auto;       /* 内容过多时滚动 */
  box-shadow: 0 8px 32px rgba(0,43,135,0.15);
}

.system-header {
  margin-bottom: 30px;  /* 调整间距 */
}

.title-group {
  text-align: center; /* 标题组内容居中 */
}

.main-title {
  font-size: 35px;      /* 小屏幕适配 */
  color: #1e3a8a;
  margin: 0 0 4px;
  line-height: 1.3;
  font-weight: 700;
}

.sub-title {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.login-input {
  height: 50px !important;
  border-radius: 8px;
  /* 移除原有border，改为控制内部输入框的边框 */
  /* border: 1px solid #E5E6EB; */
  font-size: 14px;
}

/* 穿透修改ElementUI输入框核心样式 */
::v-deep .el-input__inner {
  height: 100%;  /* 继承父级高度 */
  border: 1px solid #E5E6EB;  /* 统一边框 */
  border-radius: 8px;         /* 统一圆角 */
  padding-left: 40px;         /* 保持图标间距 */
  background: rgba(255,255,255,0.95);  /* 轻微透明背景 */
}

/* 优化聚焦状态 */
::v-deep .el-input__inner:focus {
  border-color: #165DFF !important;  /* 主色边框 */
  box-shadow: 0 0 0 2px rgba(22,93,255,0.15) !important;  /* 更柔和的阴影 */
  background: #fff;  /* 聚焦时背景变纯 */
}

/* 优化未聚焦时的图标颜色 */
::v-deep .el-input__icon {
  color: #9CA3AF;  /* 未激活时图标变灰 */
  font-size: 18px;
}

/* 聚焦时图标颜色跟随主色 */
::v-deep .el-input:focus .el-input__icon,
::v-deep .el-input--focus .el-input__icon {
  color: #165DFF;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;  /* 调整字体大小适配 */
  background: #165DFF;  /* 简化线性渐变 */
  border: none;
  border-radius: 8px;
  margin-top: 15px;
}

.login-btn:hover {
  background: #0A3FAF;  /* 悬停颜色 */
}

.remember-me {
  margin: 15px 0;
  color: #666;
}

.footer-info {
  text-align: center;
  margin-top: 20px;
  color: #999;
  font-size: 14px;
  line-height: 1.6;
}

.footer-info p { margin-bottom: 8px; }
.footer-info a { color: #2a60c9; text-decoration: none; }
.footer-info a:hover { text-decoration: underline; }

@keyframes gradientFlow {
  0% { background-position: 0% 50% }
  50% { background-position: 100% 50% }
  100% { background-position: 0% 50% }
}

@keyframes floatBall {
  0%,100% { transform: translate(0,0) }
  25% { transform: translate(20px,-20px) }
  50% { transform: translate(-20px,20px) }
  75% { transform: translate(-10px,-10px) }
}
</style>

