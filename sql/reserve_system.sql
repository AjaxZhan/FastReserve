/*
 Navicat Premium Data Transfer

 Source Server         : Cagur
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : localhost:3306
 Source Schema         : reserve_open

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 17/05/2024 21:32:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `tb_bulletin`;
CREATE TABLE `tb_bulletin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_bulletin
-- ----------------------------
INSERT INTO `tb_bulletin` VALUES (1, '注意', 'GPU资源珍贵，预约后长时间未使用，一经发现将取消使用资格。', 1, '2023-10-01 17:14:54', '2023-10-05 14:29:03', 0);
INSERT INTO `tb_bulletin` VALUES (2, '公告2', '我是测试公告22222', 1, '2023-10-01 17:25:26', '2023-10-05 19:36:41', 0);

-- ----------------------------
-- Table structure for tb_global
-- ----------------------------
DROP TABLE IF EXISTS `tb_global`;
CREATE TABLE `tb_global`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，用于检索',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站标题',
  `bulletin` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '网站公告',
  `register_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0为注册使用，1为注册后需要审批',
  `max_time` bigint(10) NULL DEFAULT NULL COMMENT '用户最多预约时间，以小时为单位',
  `card_limit` bigint(10) NULL DEFAULT NULL COMMENT '用户最多预约的卡数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_global
-- ----------------------------
INSERT INTO `tb_global` VALUES (1, 'FastReserveGPU预约系统', '# 注意！\n\n#### 1. 用户不可以在未预约的情况下使用GPU\n\n#### 2. 用户不得使用别人预约的GPU。\n', '0', 60, 5);

-- ----------------------------
-- Table structure for tb_gpu
-- ----------------------------
DROP TABLE IF EXISTS `tb_gpu`;
CREATE TABLE `tb_gpu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_id` bigint(20) NOT NULL COMMENT '服务器编号',
  `model` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'GPU型号',
  `number` int(20) NOT NULL COMMENT 'GPU编号',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0正常，1停用',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_gpu
-- ----------------------------
INSERT INTO `tb_gpu` VALUES (1, 1, 'A6000', 0, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (2, 1, 'A6000', 2, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (3, 1, 'A6000', 3, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (4, 1, 'A6000', 4, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (5, 1, 'A6000', 5, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (6, 1, 'A6000', 6, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (7, 1, 'A6000', 7, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (8, 1, 'A6000', 8, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (9, 2, 'A100', 1, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (10, 2, 'A100', 2, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (11, 2, 'A100', 3, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (12, 2, 'A100', 4, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (13, 2, 'A100', 5, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (14, 2, 'A100', 6, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (15, 2, 'A100', 7, '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_gpu` VALUES (16, 2, 'A100', 8, '0', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名字',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NULL DEFAULT 0 COMMENT '展示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由组件',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单类型(M目录 C菜单 F按钮)',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 307 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES (1, '用户中心', 0, 1, 'home', 'home', 'C', 'system:home', 'userFilled', 0);
INSERT INTO `tb_menu` VALUES (2, 'GPU预约', 0, 2, 'reserve', NULL, 'M', NULL, 'Calendar', 0);
INSERT INTO `tb_menu` VALUES (3, '管理中心', 0, 3, 'admin', 'admin', 'M', NULL, 'Avatar', 0);
INSERT INTO `tb_menu` VALUES (201, '预约中心', 2, 2, 'query', 'reserve/query', 'C', 'system:reserve:query', 'starFilled', 0);
INSERT INTO `tb_menu` VALUES (202, '我的预约', 2, 3, 'my', 'reserve/my', 'C', 'system:reserve:my', 'HomeFilled', 1);
INSERT INTO `tb_menu` VALUES (203, '我的工单', 2, 4, 'order', 'reserve/order', 'C', 'system:reserve:order', 'Service', 0);
INSERT INTO `tb_menu` VALUES (204, 'GPU状态', 2, 1, 'status', 'reserve/status', 'C', 'system:reserve:status', 'Odometer', 0);
INSERT INTO `tb_menu` VALUES (301, '用户管理', 3, 1, 'user', 'admin/user', 'C', 'system:admin:user', 'UserFilled', 0);
INSERT INTO `tb_menu` VALUES (302, 'GPU管理', 3, 2, 'gpu', 'admin/gpu', 'C', 'system:admin:gpu', 'MessageBox', 0);
INSERT INTO `tb_menu` VALUES (303, '服务器管理', 3, 3, 'server', 'admin/server', 'C', 'system:admin:server', 'MessageBox', 0);
INSERT INTO `tb_menu` VALUES (304, '网站基本信息', 3, 4, 'global', 'admin/global', 'C', 'system:admin:global', 'setting', 0);
INSERT INTO `tb_menu` VALUES (305, '工单审核', 3, 5, 'order', 'admin/order', 'C', 'system:admin:order', 'setting', 0);
INSERT INTO `tb_menu` VALUES (306, '公告管理', 3, 6, 'bulletin', 'admin/bulletin', 'C', 'system:admin:bulletin', 'ChatSquare', 0);

-- ----------------------------
-- Table structure for tb_reserve
-- ----------------------------
DROP TABLE IF EXISTS `tb_reserve`;
CREATE TABLE `tb_reserve`  (
  `id` bigint(200) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '预约标题',
  `gpu_id` bigint(200) NULL DEFAULT NULL COMMENT '预约的GPU',
  `start` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '0表示默认，1表示未审核，2表示已审核，3为不通过',
  `comment` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约说明',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '预约人',
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除标志，1为删除，0为未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_reserve
-- ----------------------------
INSERT INTO `tb_reserve` VALUES (1, '预约测试', 1, '2023-10-04 13:54:27', '2023-10-06 13:54:31', 0, '测试', 1, NULL, NULL, NULL, 0);
INSERT INTO `tb_reserve` VALUES (5, '测试', 1, '2023-10-16 00:00:00', '2023-10-18 00:00:00', 0, '', 2, NULL, NULL, NULL, 1);
INSERT INTO `tb_reserve` VALUES (6, '测试', 1, '2023-10-17 00:00:00', '2023-10-20 00:00:00', 2, '', 2, NULL, NULL, NULL, 0);
INSERT INTO `tb_reserve` VALUES (7, '你好', 3, '2024-05-17 00:00:00', '2024-05-19 00:00:00', 0, '', 1, NULL, NULL, NULL, 1);
INSERT INTO `tb_reserve` VALUES (8, '大创使用', 13, '2024-05-17 00:00:00', '2024-05-19 00:00:00', 0, '', 1, NULL, NULL, NULL, 0);
INSERT INTO `tb_reserve` VALUES (9, '跑模型', 14, '2024-05-22 00:00:00', '2024-05-25 00:00:00', 1, '', 1, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名字',
  `role_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色key',
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '管理员', 'admin', 0);
INSERT INTO `tb_role` VALUES (2, '用户', 'user', 0);

-- ----------------------------
-- Table structure for tb_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu`  (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_role_menu
-- ----------------------------
INSERT INTO `tb_role_menu` VALUES (2, 1);
INSERT INTO `tb_role_menu` VALUES (2, 2);
INSERT INTO `tb_role_menu` VALUES (2, 201);
INSERT INTO `tb_role_menu` VALUES (2, 202);
INSERT INTO `tb_role_menu` VALUES (2, 203);
INSERT INTO `tb_role_menu` VALUES (2, 204);

-- ----------------------------
-- Table structure for tb_server
-- ----------------------------
DROP TABLE IF EXISTS `tb_server`;
CREATE TABLE `tb_server`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内网IP，用于部署监测程序',
  `volume` int(20) NULL DEFAULT NULL COMMENT '附带多少张GPU卡',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器名字',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0正常，1停用',
  `create_by` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` bigint(20) NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `del_flag` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_server
-- ----------------------------
INSERT INTO `tb_server` VALUES (1, '192.168.1.6', 8, 'A6000', '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_server` VALUES (2, '192.168.1.5', 8, 'A100', '0', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态，0正常，1停用',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份，0为用户，1为管理员',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别，0男，1女',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '新增人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '新增时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', '$2a$10$9BQ46bCSgA/vKaI.SsqWXOQwCdXVoknFazw1WnFFWxzXkvCEb4V9e', '超级管理员', 'admin333@qq.com', '0', '1', '0', NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_user` VALUES (2, 'test', '$2a$10$9BQ46bCSgA/vKaI.SsqWXOQwCdXVoknFazw1WnFFWxzXkvCEb4V9e', '张三', 'test@163.com', '0', '0', '1', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 1);
INSERT INTO `tb_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
