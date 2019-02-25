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

 Date: 25/02/2019 18:38:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role_acl
-- ----------------------------
DROP TABLE IF EXISTS `role_acl`;
CREATE TABLE `role_acl`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `acl_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_acl
-- ----------------------------
INSERT INTO `role_acl` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
