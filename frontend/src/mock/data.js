// ==================== 用户数据 ====================
export const users = [
  {
    id: 1,
    username: 'admin',
    password: 'admin123',
    realName: '系统管理员',
    role: 'admin',
    phone: '13800000000',
    email: 'admin@campus.edu',
    avatar: '',
    createdAt: '2025-09-01'
  },
  {
    id: 2,
    username: 'zhangsan',
    password: '123456',
    realName: '张三',
    role: 'user',
    phone: '13811111111',
    email: 'zhangsan@campus.edu',
    avatar: '',
    studentId: '2023001001',
    department: '计算机学院',
    createdAt: '2025-09-15'
  },
  {
    id: 3,
    username: 'lisi',
    password: '123456',
    realName: '李四',
    role: 'user',
    phone: '13822222222',
    email: 'lisi@campus.edu',
    avatar: '',
    studentId: '2023001002',
    department: '文学院',
    createdAt: '2025-10-01'
  },
  {
    id: 4,
    username: 'wangwu',
    password: '123456',
    realName: '王五',
    role: 'user',
    phone: '13833333333',
    email: 'wangwu@campus.edu',
    avatar: '',
    studentId: '2023002001',
    department: '理学院',
    createdAt: '2025-10-10'
  },
  {
    id: 5,
    username: 'zhaoliu',
    password: '123456',
    realName: '赵六',
    role: 'user',
    phone: '13844444444',
    email: 'zhaoliu@campus.edu',
    avatar: '',
    studentId: '2023003001',
    department: '经济管理学院',
    createdAt: '2025-11-01'
  }
]

// ==================== 物品分类 ====================
export const categories = [
  '电子产品', '证件卡类', '钥匙', '书籍文具', '衣物鞋帽',
  '生活用品', '运动器材', '食品饮料', '其他'
]

// ==================== 校园地点 ====================
export const locations = [
  '第一教学楼', '第二教学楼', '第三教学楼', '图书馆',
  '学生宿舍1号楼', '学生宿舍2号楼', '学生宿舍3号楼',
  '食堂', '体育馆', '实验楼', '行政楼',
  '校医院', '操场', '篮球场', '校园超市', '校门口'
]

// ==================== 物品数据 ====================
export const items = [
  {
    id: 1,
    title: '丢失黑色钱包',
    description: '黑色皮质钱包，内有身份证、银行卡和少量现金。在食堂附近丢失，请拾到者联系我，非常感谢！',
    category: '生活用品',
    location: '食堂',
    publishDate: '2026-05-20',
    expiryDate: '2026-06-19',
    status: 'pending',
    ownerType: 'lost',
    userId: 2,
    userName: '张三',
    phone: '13811111111',
    images: [],
    views: 128,
    createdAt: '2026-05-20T10:30:00'
  },
  {
    id: 2,
    title: '捡到蓝色书包',
    description: '蓝色双肩书包，内有高等数学课本和笔记。在第二教学楼阶梯教室捡到，请失主前来认领。',
    category: '书籍文具',
    location: '第二教学楼',
    publishDate: '2026-06-06',
    expiryDate: '2026-06-21',
    status: 'pending',
    ownerType: 'found',
    userId: 3,
    userName: '李四',
    phone: '13822222222',
    images: [],
    views: 85,
    createdAt: '2026-06-06T14:20:00'
  },
  {
    id: 3,
    title: '丢失校园卡',
    description: '校园一卡通，卡号2023001001，姓名张三。在图书馆丢失，卡内余额约200元，请拾到者归还，必有重谢！',
    category: '证件卡类',
    location: '图书馆',
    publishDate: '2026-06-08',
    expiryDate: '2026-07-08',
    status: 'pending',
    ownerType: 'lost',
    userId: 2,
    userName: '张三',
    phone: '13811111111',
    images: [],
    views: 210,
    createdAt: '2026-06-08T09:15:00'
  },
  {
    id: 4,
    title: '捡到iPhone手机',
    description: '银色iPhone 15，手机壳为透明硅胶壳。在操场跑道旁捡到，已交至保卫处，失主可前往认领。',
    category: '电子产品',
    location: '操场',
    publishDate: '2026-06-05',
    expiryDate: '2026-06-20',
    status: 'pending',
    ownerType: 'found',
    userId: 4,
    userName: '王五',
    phone: '13833333333',
    images: [],
    views: 356,
    createdAt: '2026-06-05T16:45:00'
  },
  {
    id: 5,
    title: '丢失红色围巾',
    description: '红色羊绒围巾，品牌为鄂尔多斯。在学生宿舍2号楼到食堂的路上丢失，天气冷了急需找回。',
    category: '衣物鞋帽',
    location: '学生宿舍2号楼',
    publishDate: '2026-06-01',
    expiryDate: '2026-07-01',
    status: 'pending',
    ownerType: 'lost',
    userId: 5,
    userName: '赵六',
    phone: '13844444444',
    images: [],
    views: 67,
    createdAt: '2026-06-01T08:00:00'
  },
  {
    id: 6,
    title: '捡到眼镜盒',
    description: '黑色眼镜盒，内有一副近视眼镜（约400度）。在实验楼3楼走廊捡到。',
    category: '生活用品',
    location: '实验楼',
    publishDate: '2026-06-07',
    expiryDate: '2026-06-22',
    status: 'pending',
    ownerType: 'found',
    userId: 3,
    userName: '李四',
    phone: '13822222222',
    images: [],
    views: 43,
    createdAt: '2026-06-07T11:30:00'
  },
  {
    id: 7,
    title: '丢失蓝牙耳机',
    description: '黑色AirPods Pro，充电仓有轻微划痕。在图书馆二楼自习区丢失，里面有很重要的学习资料录音。',
    category: '电子产品',
    location: '图书馆',
    publishDate: '2026-06-03',
    expiryDate: '2026-07-03',
    status: 'pending',
    ownerType: 'lost',
    userId: 4,
    userName: '王五',
    phone: '13833333333',
    images: [],
    views: 192,
    createdAt: '2026-06-03T20:10:00'
  },
  {
    id: 8,
    title: '捡到雨伞',
    description: '蓝色折叠伞，品牌为天堂伞。在第一教学楼门口捡到，应该是昨天下午下雨时遗落的。',
    category: '生活用品',
    location: '第一教学楼',
    publishDate: '2026-05-25',
    expiryDate: '2026-06-09',
    status: 'expired',
    ownerType: 'found',
    userId: 5,
    userName: '赵六',
    phone: '13844444444',
    images: [],
    views: 34,
    createdAt: '2026-05-25T17:00:00'
  },
  {
    id: 9,
    title: '丢失充电宝',
    description: '白色小米充电宝10000mAh，外壳有贴纸。在食堂餐桌遗留，内有重要数据线。',
    category: '电子产品',
    location: '食堂',
    publishDate: '2026-05-28',
    expiryDate: '2026-06-27',
    status: 'pending',
    ownerType: 'lost',
    userId: 2,
    userName: '张三',
    phone: '13811111111',
    images: [],
    views: 78,
    createdAt: '2026-05-28T12:30:00'
  },
  {
    id: 10,
    title: '丢失笔记本电脑',
    description: '银色联想小新Pro 16，贴有卡通贴纸。在图书馆三楼靠窗位置遗忘，电脑内有毕业论文资料。',
    category: '电子产品',
    location: '图书馆',
    publishDate: '2026-06-09',
    expiryDate: '2026-07-09',
    status: 'pending',
    ownerType: 'lost',
    userId: 5,
    userName: '赵六',
    phone: '13844444444',
    images: [],
    views: 445,
    createdAt: '2026-06-09T18:20:00'
  },
  // ===== 已领取的物品 =====
  {
    id: 11,
    title: '丢失钥匙一串',
    description: '钥匙串上有3把钥匙和一个HelloKitty钥匙扣。在学生宿舍1号楼丢失。',
    category: '钥匙',
    location: '学生宿舍1号楼',
    publishDate: '2026-05-15',
    expiryDate: '2026-06-14',
    status: 'claimed',
    ownerType: 'lost',
    userId: 2,
    userName: '张三',
    phone: '13811111111',
    images: [],
    views: 156,
    createdAt: '2026-05-15T09:00:00'
  },
  {
    id: 12,
    title: '捡到U盘',
    description: '银色金士顿U盘32GB，在行政楼一楼大厅捡到。',
    category: '电子产品',
    location: '行政楼',
    publishDate: '2026-05-10',
    expiryDate: '2026-05-25',
    status: 'claimed',
    ownerType: 'found',
    userId: 3,
    userName: '李四',
    phone: '13822222222',
    images: [],
    views: 98,
    createdAt: '2026-05-10T15:30:00'
  },
  // ===== 已过期的物品 =====
  {
    id: 13,
    title: '丢失水杯',
    description: '绿色保温杯，膳魔师品牌。在校医院附近丢失。',
    category: '生活用品',
    location: '校医院',
    publishDate: '2026-04-20',
    expiryDate: '2026-05-20',
    status: 'expired',
    ownerType: 'lost',
    userId: 4,
    userName: '王五',
    phone: '13833333333',
    images: [],
    views: 45,
    createdAt: '2026-04-20T10:00:00'
  },
  {
    id: 14,
    title: '捡到篮球',
    description: '斯伯丁篮球，在篮球场捡到。',
    category: '运动器材',
    location: '篮球场',
    publishDate: '2026-05-01',
    expiryDate: '2026-05-16',
    status: 'expired',
    ownerType: 'found',
    userId: 2,
    userName: '张三',
    phone: '13811111111',
    images: [],
    views: 32,
    createdAt: '2026-05-01T16:00:00'
  }
]

// ==================== 认领记录 ====================
export const claims = [
  {
    id: 1,
    itemId: 11,
    itemTitle: '丢失钥匙一串',
    claimerId: 3,
    claimerName: '李四',
    ownerId: 2,
    ownerName: '张三',
    reason: '这串钥匙是我捡到的，一直保管着等待失主。现在看到发布的消息，确认是张三的钥匙。',
    status: 'approved',
    adminNote: '已核实，认领信息匹配。',
    createdAt: '2026-05-16T10:00:00',
    reviewedAt: '2026-05-16T14:30:00'
  },
  {
    id: 2,
    itemId: 12,
    itemTitle: '捡到U盘',
    claimerId: 4,
    claimerName: '王五',
    ownerId: 3,
    ownerName: '李四',
    reason: '这个U盘是我的，里面有我的课程设计文件，我可以描述文件内容来证明。',
    status: 'approved',
    adminNote: '已验证文件内容描述，确认为失主。',
    createdAt: '2026-05-11T09:00:00',
    reviewedAt: '2026-05-11T11:00:00'
  },
  {
    id: 3,
    itemId: 1,
    itemTitle: '丢失黑色钱包',
    claimerId: 4,
    claimerName: '王五',
    ownerId: 2,
    ownerName: '张三',
    reason: '我在食堂捡到了一个黑色钱包，可能是张三丢失的。',
    status: 'pending',
    adminNote: '',
    createdAt: '2026-06-08T16:00:00',
    reviewedAt: null
  },
  {
    id: 4,
    itemId: 2,
    itemTitle: '捡到蓝色书包',
    claimerId: 2,
    claimerName: '张三',
    ownerId: 3,
    ownerName: '李四',
    reason: '这个蓝色书包是我的！里面有我的高等数学课本和课堂笔记，笔记第一页有我的名字。',
    status: 'pending',
    adminNote: '',
    createdAt: '2026-06-09T08:30:00',
    reviewedAt: null
  },
  {
    id: 5,
    itemId: 4,
    itemTitle: '捡到iPhone手机',
    claimerId: 5,
    claimerName: '赵六',
    ownerId: 4,
    ownerName: '王五',
    reason: '这台iPhone是我的，手机壳是透明硅胶壳，手机壁纸是我和室友的合照。',
    status: 'pending',
    adminNote: '',
    createdAt: '2026-06-09T10:00:00',
    reviewedAt: null
  },
  {
    id: 6,
    itemId: 3,
    itemTitle: '丢失校园卡',
    claimerId: 5,
    claimerName: '赵六',
    ownerId: 2,
    ownerName: '张三',
    reason: '我好像在图书馆捡到了这张校园卡。',
    status: 'rejected',
    adminNote: '认领人非卡片本人，信息不符。',
    createdAt: '2026-06-09T14:00:00',
    reviewedAt: '2026-06-09T16:00:00'
  }
]

// ==================== 系统公告 ====================
export const announcements = [
  {
    id: 1,
    title: '失物招领系统上线通知',
    content: '校园失物招领系统已正式上线，欢迎广大师生使用！如有问题请联系管理员。',
    createdAt: '2026-03-01'
  },
  {
    id: 2,
    title: '认领须知',
    content: '认领物品时请提供充分的证明信息，管理员将在24小时内审核您的认领申请。',
    createdAt: '2026-03-15'
  }
]
