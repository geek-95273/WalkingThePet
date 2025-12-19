export const sitters = [
  {
    id: 's-001',
    name: '周小暖',
    gender: '女生',
    distance: '1.2km',
    rating: 4.9,
    orders: 126,
    slogan: '猫咪行为学初级，温柔陪伴，擅长安抚胆小猫。',
    tags: ['猫咪经验丰富', '时间灵活', '擅长服药'],
    pets: [
      { name: '团子', desc: '英短蓝白', cover: 'linear-gradient(135deg, #ffdf85, #ffc857)' },
      { name: '豆沙', desc: '布偶', cover: 'linear-gradient(135deg, #ffe08c, #ffb347)' }
    ],
    services: [
      { type: 'feed-cat', title: '上门喂猫', price: '￥58/次', duration: '30分钟' },
      { type: 'walk-dog', title: '上门遛狗', price: '￥68/次', duration: '30分钟' }
    ]
  },
  {
    id: 's-002',
    name: '李晨',
    gender: '男生',
    distance: '2.6km',
    rating: 4.8,
    orders: 203,
    slogan: '跑步爱好者，力量型，可陪大型犬长距离遛弯。',
    tags: ['大型犬友好', '可陪跑', '时间准时'],
    pets: [
      { name: 'Lucky', desc: '拉布拉多', cover: 'linear-gradient(135deg, #ffd766, #f7b844)' }
    ],
    services: [
      { type: 'walk-dog', title: '上门遛狗', price: '￥78/次', duration: '45分钟' },
      { type: 'feed-cat', title: '上门喂猫', price: '￥68/次', duration: '30分钟' }
    ]
  },
  {
    id: 's-003',
    name: '陈可',
    gender: '女生',
    distance: '3.1km',
    rating: 4.7,
    orders: 148,
    slogan: '擅长照顾幼猫幼犬，细致记录饮水与排便情况。',
    tags: ['幼宠经验', '照片反馈', '卫生到位'],
    pets: [
      { name: '奶酪', desc: '金渐层', cover: 'linear-gradient(135deg, #fff6d8, #ffc857)' },
      { name: '可乐', desc: '比熊', cover: 'linear-gradient(135deg, #ffe9a7, #ffb347)' }
    ],
    services: [
      { type: 'feed-cat', title: '上门喂猫', price: '￥62/次', duration: '30分钟' },
      { type: 'walk-dog', title: '上门遛狗', price: '￥72/次', duration: '30分钟' }
    ]
  }
];

export const findSitter = (id) => sitters.find((item) => item.id === id);

export const addSitter = (payload) => {
  const id = `s-${(Math.random() * 100000).toFixed(0)}`;
  const sitter = {
    id,
    rating: 5.0,
    orders: 0,
    ...payload
  };
  sitters.unshift(sitter);
  return id;
};
