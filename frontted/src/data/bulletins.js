import { reactive } from 'vue';

const seed = [
  {
    id: 'b-001',
    title: '上门喂猫 · 晚饭场',
    serviceType: 'feed-cat',
    address: '和平里小区 5 号楼 2 单元',
    petName: '奶糖',
    petType: '英短',
    serviceTime: '今天 18:30',
    walkerGender: '不限',
    remark: '换水、湿粮 60g，陪玩 15 分钟，检查猫砂。',
    status: '待接单',
    distance: '2.3km',
    createdAt: '刚刚'
  },
  {
    id: 'b-002',
    title: '晚间遛狗 30 分钟',
    serviceType: 'walk-dog',
    address: '金辉府南门集合',
    petName: 'Lucky',
    petType: '柯基',
    serviceTime: '今天 20:00',
    walkerGender: '女生',
    remark: '牵引绳在鞋柜，避开大狗；如有拉粑粑请及时清理。',
    status: '待接单',
    distance: '1.5km',
    createdAt: '1 小时前'
  },
  {
    id: 'b-003',
    title: '中午喂猫加猫砂',
    serviceType: 'feed-cat',
    address: '华府天地 3-1-1203',
    petName: '橘子',
    petType: '橘猫',
    serviceTime: '明天 12:30',
    walkerGender: '不限',
    remark: '猫砂需要补一勺，零食两颗，注意锁门。',
    status: '待接单',
    distance: '3.8km',
    createdAt: '2 小时前'
  }
];

export const bulletins = reactive([...seed]);

export const addBulletin = (payload) => {
  const id = `b-${(Math.random() * 100000).toFixed(0)}`;
  bulletins.unshift({
    id,
    title: payload.title,
    serviceType: payload.serviceType,
    address: payload.address,
    petId: payload.petId || '',
    petName: payload.petName,
    petType: payload.petType,
    serviceTime: payload.serviceTime,
    walkerGender: payload.walkerGender,
    remark: payload.remark,
    status: '待接单',
    acceptedBy: null,
    distance: payload.distance || '附近',
    createdAt: '刚刚'
  });
  return id;
};

export const findBulletin = (id) => bulletins.find((item) => item.id === id);

export const acceptBulletin = (id, sitterId) => {
  const item = findBulletin(id);
  if (item) {
    item.status = '已接单';
    item.acceptedBy = sitterId || null;
  }
};
