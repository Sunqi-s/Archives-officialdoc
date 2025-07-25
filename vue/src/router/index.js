import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航 tabBar 中的 vue-router 在 3.0 版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },

      //收发文模块相关
      { path: 'doc-type-list', name: 'DocTypeList', meta: { name: '来文类型管理' }, component: () => import('../views/manager/DocTypeList.vue') },
      { path: 'flow-object-list', name: 'FlowObjectList', meta: { name: '流转对象管理' }, component: () => import('../views/manager/FlowObjectList.vue') },
      { path: 'incoming-doc-register/:secretType', name: 'IncomingDocRegister', meta: { name: '收文登记' }, component: () => import('../views/manager/IncomingDocRegister') },
      { path: 'incoming-doc-detail/:id', name: 'IncomingDocDetail', meta: { name: '收文详情' }, component: () => import('../views/manager/IncomingDocDetail') },
      { path: 'incoming-doc-normal/:secretType', name: 'IncomingDocList', meta: { name: '普通收文列表' }, component: () => import('../views/manager/IncomingDocList') },
      { path: 'incoming-doc-secret/:secretType', name: 'IncomingDocList', meta: { name: '密件收文列表' }, component: () => import('../views/manager/IncomingDocList') },
      { path: 'outgoing-doc-register/:secretType', name: 'OutgoingDocRegister', meta: { name: '发文登记' }, component: () => import('../views/manager/OutgoingDocRegister.vue') },
      { path: 'outgoing-doc-detail/:id', name: 'OutgoingDocDetail', meta: { name: '发文详情' }, component: () => import('../views/manager/OutgoingDocDetail.vue') },
      { path: 'outgoing-doc-normal/:secretType', name: 'OutgoingDocList', meta: { name: '普通发文列表' }, component: () => import('../views/manager/OutgoingDocList.vue') },
      { path: 'outgoing-doc-secret/:secretType', name: 'OutgoingDocList', meta: { name: '密件发文列表' }, component: () => import('../views/manager/OutgoingDocList.vue') }

    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
