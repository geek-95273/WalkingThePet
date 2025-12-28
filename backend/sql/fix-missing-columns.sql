-- 修复缺失的数据库字段
USE walking_pet_bulletin;

-- 检查并添加 address 字段
ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS address VARCHAR(200) NOT NULL DEFAULT '' COMMENT '地址' AFTER status;

-- 检查并添加其他可能缺失的字段
ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS pet_id VARCHAR(50) COMMENT '宠物ID' AFTER address;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS pet_name VARCHAR(50) COMMENT '宠物名称' AFTER pet_id;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS pet_type VARCHAR(50) COMMENT '宠物类型' AFTER pet_name;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS walker_gender VARCHAR(10) COMMENT '宠托师性别偏好' AFTER service_time;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS remark TEXT COMMENT '备注' AFTER walker_gender;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS sitter_id VARCHAR(50) COMMENT '接单宠托师ID' AFTER remark;

ALTER TABLE bulletins 
ADD COLUMN IF NOT EXISTS distance VARCHAR(20) COMMENT '距离' AFTER sitter_id;

-- 确保索引存在
CREATE INDEX IF NOT EXISTS idx_status ON bulletins(status);
CREATE INDEX IF NOT EXISTS idx_service_type ON bulletins(service_type);
CREATE INDEX IF NOT EXISTS idx_user_id ON bulletins(user_id);

SELECT 'Bulletin表字段修复完成' AS message;
