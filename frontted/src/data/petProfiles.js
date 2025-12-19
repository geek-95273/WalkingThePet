import { reactive } from 'vue';

const seed = [
  {
    id: 'pet-001',
    type: '猫',
    name: '奶糖',
    age: '1岁2个月',
    gender: '母',
    weight: '4.1kg',
    breed: '英短蓝白',
    aggressive: false,
    rabiesVaccine: true,
    intro: '性格温柔，喜欢逗猫棒，怕生人需轻声接触。',
    image: 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?auto=format&fit=crop&w=400&q=80'
  },
  {
    id: 'pet-002',
    type: '狗',
    name: 'Lucky',
    age: '2岁',
    gender: '公',
    weight: '12kg',
    breed: '柯基',
    aggressive: false,
    rabiesVaccine: true,
    intro: '活泼外向，牵引配合，喜欢飞盘和零食。',
    image: 'https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=400&q=80'
  }
];

export const petProfiles = reactive([...seed]);

const genId = () => `pet-${(Math.random() * 100000).toFixed(0)}`;

export const findPetProfile = (id) => petProfiles.find((item) => item.id === id);

export const addPetProfile = (payload) => {
  const id = genId();
  petProfiles.unshift({ id, ...payload });
  return id;
};

export const updatePetProfile = (id, payload) => {
  const target = findPetProfile(id);
  if (target) {
    Object.assign(target, payload);
  }
};

export const removePetProfile = (id) => {
  const index = petProfiles.findIndex((item) => item.id === id);
  if (index !== -1) {
    petProfiles.splice(index, 1);
  }
};
