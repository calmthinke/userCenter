export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {name: '登录', path: '/user/login', component: './user/Login'},
      {name: '注册', path: '/user/register', component: './user/Register'},
      {component: './404'},
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    // 这里 角色不同进入不同 页面
    access: 'canAdmin',
    routes: [
      { path: '/admin/manager-page', name: '用户管理', icon: 'smile', component: './admin/UserManager' },
      { component: './404' },
    ],
  },
  { name: '查询表格', icon: 'table', path: '/list', component: './TableList' },
  { path: '/', redirect: '/welcome' },
  { component: './404' },
];
