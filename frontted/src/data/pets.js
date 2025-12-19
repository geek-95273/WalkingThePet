export const pets = [
  {
    id: 'p-01',
    name: '焦糖',
    breed: '柯基',
    age: '1岁4个月',
    distance: '2.1km',
    weight: '10.2kg',
    personality: '阳光亲人，喜欢和人玩飞盘，出门牵引很听话。',
    tags: ['疫苗齐全', '可短期寄养', '已驱虫'],
    coverGradient: 'linear-gradient(135deg, #ffdf85 0%, #ffc857 45%, #f2a65a 100%)'
  },
  {
    id: 'p-02',
    name: '柠檬',
    breed: '英短蓝白',
    age: '2岁1个月',
    distance: '3.4km',
    weight: '4.1kg',
    personality: '安静黏人，不乱抓家具，喜欢逗猫棒和小零食。',
    tags: ['绝育', '疫苗齐全', '驱虫记录'],
    coverGradient: 'linear-gradient(135deg, #fff3b0 0%, #ffe08c 50%, #ffc857 100%)'
  },
  {
    id: 'p-03',
    name: '糯米',
    breed: '比熊',
    age: '11个月',
    distance: '1.3km',
    weight: '5.3kg',
    personality: '小体型，性格乖巧，外出很配合，喜欢和小朋友玩。',
    tags: ['洗护良好', '小体型', '亲人'],
    coverGradient: 'linear-gradient(135deg, #ffe9a7 0%, #ffd766 55%, #ffb347 100%)'
  },
  {
    id: 'p-04',
    name: '可可',
    breed: '拉布拉多',
    age: '3岁2个月',
    distance: '4.7km',
    weight: '26kg',
    personality: '训练基础扎实，耐心温柔，适合长距离遛弯或陪跑。',
    tags: ['已绝育', '力量型', '可陪跑'],
    coverGradient: 'linear-gradient(135deg, #fff8e1 0%, #ffd766 50%, #f7a440 100%)'
  },
  {
    id: 'p-05',
    name: '雪球',
    breed: '萨摩耶',
    age: '1岁8个月',
    distance: '2.8km',
    weight: '18kg',
    personality: '爱笑的小太阳，喜欢社交，能接受基础洗护。',
    tags: ['社交友好', '中大型犬', '疫苗齐全'],
    coverGradient: 'linear-gradient(135deg, #fff4c1 0%, #ffd766 55%, #f4b860 100%)'
  },
  {
    id: 'p-06',
    name: '芝芝',
    breed: '暹罗猫',
    age: '1岁',
    distance: '1.9km',
    weight: '3.5kg',
    personality: '聪明爱讲话，对新环境好奇，愿意陪伴但不过分黏人。',
    tags: ['短期寄养', '温顺', '已驱虫'],
    coverGradient: 'linear-gradient(135deg, #fff8e1 0%, #ffe08c 48%, #ffbd59 100%)'
  },
  {
    id: 'p-07',
    name: '拿铁',
    breed: '法斗',
    age: '2岁5个月',
    distance: '5.2km',
    weight: '12kg',
    personality: '憨厚黏人，适合短途散步，怕热，需注意时段。',
    tags: ['短鼻犬', '疫苗齐全', '亲人'],
    coverGradient: 'linear-gradient(135deg, #fff1b8 0%, #ffd766 52%, #f7b844 100%)'
  },
  {
    id: 'p-08',
    name: '奶黄',
    breed: '金渐层',
    age: '9个月',
    distance: '1.1km',
    weight: '3kg',
    personality: '活泼撒娇，喜欢逗猫棒，不抗拒抱抱和梳毛。',
    tags: ['幼猫', '亲人', '驱虫记录'],
    coverGradient: 'linear-gradient(135deg, #fff8e1 0%, #ffe08c 45%, #ffb347 100%)'
  }
];

export const findPet = (id) => pets.find((item) => item.id === id);
