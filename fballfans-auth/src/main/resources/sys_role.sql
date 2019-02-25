/*
 Navicat Premium Data Transfer

 Source Server         : taijiangdev,tianjindev,xixiudev,wulumuqidev
 Source Server Type    : MySQL
 Source Server Version : 50559
 Source Host           : 192.168.1.108:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50559
 File Encoding         : 65001

 Date: 25/02/2019 18:38:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 1, '2019-02-25 17:24:01', 'ROLE_ADMIN', NULL, NULL);
INSERT INTO `sys_role` VALUES (2, 1, '2019-02-25 17:24:25', 'ROLE_APPROVAL', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
